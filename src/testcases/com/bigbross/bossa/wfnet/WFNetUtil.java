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

package com.bigbross.bossa.wfnet;

import java.util.Map;

import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.Resource;
import com.bigbross.bossa.resource.ResourceUtil;

public class WFNetUtil {

    public static CaseType createCaseType(String id) {
     
        CaseType caseType = new CaseType(id);

        Place A = caseType.registerPlace("A");
        Place B = caseType.registerPlace("B");
        Place C = caseType.registerPlace("C");
        Place D = caseType.registerPlace("D");
        Place E = caseType.registerPlace("E");
        Place F = caseType.registerPlace("F");
        Place G = caseType.registerPlace("G");
        Place H = caseType.registerPlace("H");

        Transition a = caseType.registerTransition("a", "requesters");
        Transition b = caseType.registerTransition("b", "sales");
        Transition c = caseType.registerTransition("c", "directors");
        Transition d = caseType.registerTransition("d", "sales");
        Transition e = caseType.registerTransition("e", "sales");
        Transition f = caseType.registerTransition("f", "requesters");

	caseType.buildMap();

        a.input(A,  "1");
        a.output(B, "1");

        b.input(B,  "1");
        b.output(C, "!SOK");
        b.output(D, "SOK && DIR");
        b.output(E, "SOK && !DIR");

        c.input(D,  "1");
        c.output(B, "ADIR == 'BACK'");
        c.output(E, "ADIR == 'OK'");
        c.output(H, "ADIR == 'CANCEL'");

        d.input(E,  "1");
        d.output(F, "1");

        e.input(F,  "1");
        e.output(G, "1");

        f.input(C,  "1");
        f.output(B, "1");
        f.output(H, "1");

        try {        
            caseType.buildTemplate(new int[] {1,0,0,0,0,0,0,0});
        } catch (EvaluationException exception) {
            exception.printStackTrace();
        }

        return caseType;
    }

    public static Case createCase() {
        return createCase(new int[] {1,0,0,0,0,0,0,0});
    }

    public static Case createCase(int[] marking) {
        try {
            return createCaseType("test").openCase(marking);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
        return null;    
    }

    public static boolean fire(Case caze, String workItemId, Map attributes) {
        return fire(caze, workItemId, attributes, ResourceUtil.createResource("jdoe"));
    }

    public static boolean fire(Case caze, String workItemId, Map attributes, Resource resource) {
        try {
            Activity act = caze.open(caze.getWorkItem(workItemId), resource);
            if (act != null) {
                return caze.close(act, attributes);
            }
        } catch (BossaException e) {
            e.printStackTrace();
        }
	return false;
    }

    public static void prepareWorkTest(CaseTypeManager caseTypeManager) {
        CaseType caseType = createCaseType("test");
        caseTypeManager.registerCaseTypeImpl(caseType);
    }

    public static void createActWorkTest(CaseTypeManager caseTypeManager) {
        WorkItem wi = (WorkItem) caseTypeManager.getWorkItems(true).get(0);
        Resource joe = caseTypeManager.getBossa().getResourceManager().getResource("joe");
        try {
            wi.getCase().open(wi, joe);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }
    }

}