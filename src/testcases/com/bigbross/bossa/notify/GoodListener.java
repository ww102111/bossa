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
 * This class implements a working listener. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class GoodListener implements Listener {

    private String id;
    private Resource resource;

    public GoodListener(String id, Resource resource) {
        this.id = id;
        this.resource = resource;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#getResource()
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * @see com.bigbross.bossa.notify.Listener#notifyEvent(
     *      java.lang.String, java.util.Date, java.util.Map)
     */
    public void notifyEvent(String id, Date time, Map attributes) {
        String status = (String) attributes.get("status");
        if (status != null) {
            status = status + " ok";
        } else {
            status = "ok";
        }
        attributes.put("status", status);
    }
}