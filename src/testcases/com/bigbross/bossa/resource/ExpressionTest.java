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
import java.util.Set;

import junit.framework.TestCase;

public class ExpressionTest extends TestCase {

    ResourceManager manager;

    Resource a, b, c, x, A, B, C;

    public ExpressionTest(String name) {
	super(name);
    }

    protected void setUp() {
	System.out.println("Setting up a expression test.");
        manager = new ResourceManager();
        A = manager.createResourceImpl("A");
        B = manager.createResourceImpl("B");
        C = manager.createResourceImpl("C");
        a = manager.createResourceImpl("a");
        b = manager.createResourceImpl("b");
        c = manager.createResourceImpl("c");
        x = manager.createResourceImpl("x");
        A.includeImpl(a);
        A.includeImpl(x);
        B.includeImpl(b);
        B.includeImpl(x);
        C.includeImpl(c);
    }

    public void testReference() {
        Container resource = manager.compile("C");
        assertTrue(resource.contains(C));
        assertTrue(resource.contains(c));
        assertFalse(resource.contains(x));
    }

    public void testUnion() {
        Container resource = manager.compile("A+B");
        assertTrue(resource.contains(a));
        assertTrue(resource.contains(b));
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(c));
    }

    public void testIntersection() {
        Container resource = manager.compile("A^B");
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(a));
        assertFalse(resource.contains(b));
    }

    public void testSubtraction() {
        Container resource = manager.compile("A-B");
        assertTrue(resource.contains(a));
        assertFalse(resource.contains(b));
        assertFalse(resource.contains(x));
    }

    public void testExpression() {
        Container resource = manager.compile("A^B+C");
        assertTrue(resource.contains(c));
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(a));
        assertFalse(resource.contains(b));
    }

    public void testGroup() {
        Container resource = manager.compile("A^(B+C)");
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(a));
        assertFalse(resource.contains(b));
        assertFalse(resource.contains(c));
    }

}
