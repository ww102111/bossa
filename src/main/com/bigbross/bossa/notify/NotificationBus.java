/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003 OpenBR Sistemas S/C Ltda.
 *
 * This file is part of Bossa.
 *
 * Bossa is free software; you can redistribute it and/or modify it
 * under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package com.bigbross.bossa.notify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.resource.Resource;


/**
 * This class manages all event notifications inside Bossa. The events are
 * notified to listeners registered in the bus. This registration can happen
 * either at the bus creation or at a later time. <p>
 * 
 * The listeners registered after the bus is created will not be preserved
 * if this bus is serialized. Due to this behaviour, objects outside the Bossa
 * core that are registered as listeners may lose notification of events if
 * a failure crashes the system. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class NotificationBus implements Serializable {

    private List persistentListeners;

    private transient Map transientListeners;

    /**
     * Creates a new notification bus with some persistent listeners. <p>
     *  
     * @param persistentListeners a list of the persistent listeners.
     */
    public NotificationBus(List persistentListeners) {
        this.persistentListeners = persistentListeners != null ? 
                                   persistentListeners : new ArrayList();
        this.transientListeners = new HashMap();
    }

    /**
     * Creates a new empty notification bus. <p>
     */
    public NotificationBus() {
        this(null);
    }

    /**
     * Registers a new listener of the notification bus. <p>
     * 
     * @param listener the object that will act as a listener of the
     *                 notification bus.
     * @return <code>true</code> if the listener is registered,
     *         <code>false</code> if there is already a listener registered
     *         with the same id.
     */    
    public boolean registerListener(Listener listener) {
        if (transientListeners.containsKey(listener.getId())) {
            return false;
        }
        transientListeners.put(listener.getId(), listener);
        return true;
    }
    
    /**
     * Removes the listener from the notification bus, if present. <p>
     * 
     * @param id the id of the listener.
     */
    public void removeListener(String id) {
        transientListeners.remove(id);
    }

    /**
     * Informs the occurrence of an event to all registered listeners. <p>
     * 
     * An event has an id and some attributes that are dependent on the event
     * type. See the constructor of the <code>Event</code> class for more
     * information. <p>
     * 
     * @param event the event.
     * @see com.bigbross.bossa.notify.Event#Event
     */
    public void notifyEvent(Event event) {
        Iterator[] iterators = {persistentListeners.iterator(),
                                transientListeners.values().iterator()};
        for (int i = 0; i < iterators.length; i++) {
            Iterator it = iterators[i];
            while (it.hasNext()) {
                Listener l = (Listener) it.next();
                singleListenerNotify(event, l);
            }
        }
    }

    /**
     * Informs the occurrence of an event to a single listener. <p>
     * 
     * @param event the event.
     * @param l the listener.
     */
    private void singleListenerNotify(Event event, Listener l) {
        if (l.interested(event.getType())) {
            if (event.getType() == Event.WFNET_EVENT) {
                Resource resource =
                    (Resource) event.getAttributes().get("resource");
                if (resource != null && l.getResource() != null &&
                    ! resource.contains(l.getResource())) {
                    return;        
                }
            }
            try {
                l.notifyEvent(event);
            } catch (Exception e) {
                /* We ignore listeners exceptions. */
            }
        }
    }
}
