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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class ResourceManagerTest extends TestCase {

    public ResourceManagerTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a resource manager test.");
    }

    public void testCreateResource() {
        ResourceManager resourceManager = new ResourceManager();

        assertNotNull(resourceManager.createResourceImpl("testResource"));
        assertNull(resourceManager.createResourceImpl("testResource"));
    }
    
    public void testGetResource() {
        ResourceManager resourceManager = new ResourceManager();
        assertNull(resourceManager.getResource("testResource"));

        Resource test = resourceManager.createResourceImpl("testResource");
        assertNotNull(test);

        assertSame(test, resourceManager.getResource("testResource"));
        assertSame(resourceManager, test.getResourceManager());
    }

    public void testRemoveResource() {
        ResourceManager resourceManager = new ResourceManager();

        assertNotNull(resourceManager.createResourceImpl("testResource"));

        Resource test = resourceManager.getResource("testResource");
        assertNotNull(test);
        assertTrue(resourceManager.removeResourceImpl(test));

        assertNotNull(resourceManager.createResourceImpl("testResource"));
    }

}
