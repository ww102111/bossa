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

import java.util.Date;
import java.util.Map;

import com.bigbross.bossa.resource.Resource;

/**
 * This interface should be implemented by a class that wants to be registered
 * in the notification bus. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public interface Listener {
    
    /**
     * Returns the id of this listener. This id must be unique with respect
     * to the desired notification bus. <p>
     *  
     * @return the id of this listener.
     */
    String getId();
    
    /**
     * Returns the resource used to filter the events passed to this
     * listener. If the resource is <code>null</code>, all events will be
     * passed to this listener. <p>
     * 
     * @return the resource used as filter.
     */
    Resource getResource();
    
    /**
     * Notifies this listener of an event. <p>
     * 
     * @param id the id of the event.
     * @param time the time the notification bus noticed the event.
     * @param attributes the attributes of this event.
     */
    void notifyEvent(String id, Date time, Map attributes);
}