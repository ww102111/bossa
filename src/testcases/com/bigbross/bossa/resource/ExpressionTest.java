/*
 * Bossa Workflow System
 *
 * $Id$
 *
 * Copyright (C) 2003,2004 OpenBR Sistemas S/C Ltda.
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

import junit.framework.TestCase;

public class ExpressionTest extends TestCase {

    ResourceRegistry registry;

    Resource a, b, c, x, A, B, C;

    public ExpressionTest(String name) {
        super(name);
    }

    protected void setUp() {
        registry = new ResourceRegistry("null");
        A = registry.createResourceImpl("A", false);
        B = registry.createResourceImpl("B", false);
        C = registry.createResourceImpl("C", false);
        a = registry.createResourceImpl("a", false);
        b = registry.createResourceImpl("b", false);
        c = registry.createResourceImpl("c", false);
        x = registry.createResourceImpl("x", false);
        A.includeImpl(a, false);
        A.includeImpl(x, false);
        B.includeImpl(b, false);
        B.includeImpl(x, false);
        C.includeImpl(c, false);
    }

    public void testReference() {
        Expression resource = registry.compile("C");
        assertTrue(resource.contains(C));

        ResourceRegistry context = new ResourceRegistry("null");
        Resource newC = context.createResourceImpl("C", false);
        newC.includeImpl(x, false);

        assertTrue(resource.contains(c));
        assertTrue(resource.contains(context, c));

        assertFalse(resource.contains(x));
        assertFalse(resource.contains(context, x));
    }

    public void testReferenceCreation() {
        Expression resource = registry.compile("new");
        Resource r = registry.getResource("new");
        assertNotNull(r);
        assertTrue(resource.contains(r));
    }

    public void testLazyReference() {
        Expression resource = registry.compile("$C");
        assertTrue(resource.contains(C));

        ResourceRegistry context = new ResourceRegistry("null");
        Resource newC = context.createResourceImpl("C", false);
        newC.includeImpl(x, false);

        assertFalse(resource.contains(context, C));
        assertTrue(resource.contains(context, newC));

        assertTrue(resource.contains(c));
        assertFalse(resource.contains(context, c));

        assertFalse(resource.contains(x));
        assertTrue(resource.contains(context, x));
    }

    public void testUnion() {
        Expression resource = registry.compile("A+B");
        assertTrue(resource.contains(a));
        assertTrue(resource.contains(b));
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(c));
    }

    public void testIntersection() {
        Expression resource = registry.compile("A^B");
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(a));
        assertFalse(resource.contains(b));
    }

    public void testSubtraction() {
        Expression resource = registry.compile("A-B");
        assertTrue(resource.contains(a));
        assertFalse(resource.contains(b));
        assertFalse(resource.contains(x));
    }

    public void testExpression() {
        Expression resource = registry.compile("A^B+C");
        assertTrue(resource.contains(c));
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(a));
        assertFalse(resource.contains(b));
    }

    public void testGroup() {
        Expression resource = registry.compile("A ^ (B + C)");
        assertTrue(resource.contains(x));
        assertFalse(resource.contains(a));
        assertFalse(resource.contains(b));
        assertFalse(resource.contains(c));
    }

}
