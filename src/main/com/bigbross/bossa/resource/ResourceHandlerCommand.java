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

package com.bigbross.bossa.resource;

import java.io.Serializable;

/**
 * This class implements a generic operation of the <code>Resource</code>
 * class. It will locate two resource wherever they may be in the system.
 * <p>
 * 
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
abstract class ResourceHandlerCommand extends ResourceCommand {

    private String hostRegistryId;
    private String hostId;
    private String resourceRegistryId;
    private String resourceId;
    
    /**
     * Creates a new resource operation. <p>
     *
     * @param host the resource that will perform the operation. 
     * @param resource the resource that manipulated.
     */    
    ResourceHandlerCommand(Resource host, Resource resource) {
        this.hostRegistryId =
            host.getResourceRegistry().getGlobalId();
        this.hostId = host.getId();
        this.resourceRegistryId =
            resource.getResourceRegistry().getGlobalId();
        this.resourceId = resource.getId();
    }

    /**
     * Locates the resources and calls the specific operation. <p>
     * 
     * @see com.bigbross.bossa.resource.ResourceCommand#execute(ResourceManager)
     */
    protected Serializable execute(ResourceManager resourceManager) 
        throws Exception {
        ResourceRegistry hostRegistry =
            resourceManager.getRegistry(hostRegistryId);
        Resource host = hostRegistry.getResource(hostId);
        ResourceRegistry resourceRegistry =
            resourceManager.getRegistry(resourceRegistryId);
        Resource resource = resourceRegistry.getResource(resourceId);
        return execute(host, resource);
    }

    /**
     * Executes a command using the two provided resources. <p>
     */
    protected abstract Serializable execute(Resource host, Resource resource)
        throws Exception;
}