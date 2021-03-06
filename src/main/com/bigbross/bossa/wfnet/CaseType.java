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

package com.bigbross.bossa.wfnet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaException;
import com.bigbross.bossa.resource.ResourceRegistry;

/**
 * This class represents the definition of a process (case type). It
 * also keeps track of all current cases of this case type. <p>
 *
 * @author <a href="http://www.bigbross.com">BigBross Team</a>
 */
public class CaseType implements Serializable {

    private String id;

    private Map transitions;

    private Map places;

    private ResourceRegistry resources;

    private Map cases;

    private Case template;

    private int caseSequence;

    private CaseTypeManager caseTypeManager;

    /**
     * Creates a new case type, without any places or transitions. <p>
     *
     * @param id the id of the new case type.
     */
    public CaseType(String id) {
        this.id = id;
        this.transitions = new HashMap();
        this.places = new HashMap();
        this.resources = new ResourceRegistry(id);
        this.cases = new HashMap();
        this.caseSequence = 0;
        this.caseTypeManager = null;
    }

    /**
     * Returns the id of this case type. <p>
     *
     * @return the id of this case type.
     */
    public String getId() {
        return id;
    }

    /**
     * Creates a place with the specified id and initial marking in this
     * case type. <p>
     *
     * @param id the place id.
     * @param initialMarking the initial marking, the number of tokens in
     *                       this place when a new case starts.
     * @return the created place.
     */
    public Place registerPlace(String id, int initialMarking) {
        Place place = new Place(places.size(), id, initialMarking);
        places.put(id, place);
        return place;
    }

    /**
     * Creates a place with the specified id and initial marking set to
     * zero in this case type. <p>
     *
     * @param id the place id.
     * @return the created place.
     */
    public Place registerPlace(String id) {
        return registerPlace(id, 0);
    }

    /**
     * Returns the place of this case type that has the specified id. <p>
     *
     * @param id the place id.
     * @return the place,
     *         or <code>null</code> if there is no such a place.
     */
    public Place getPlace(String id) {
        return (Place) places.get(id);
    }

    /**
     * Returns all places of this case type. <p>
     *
     * @return a collection of all places of this case type.
     */
    Collection getPlaces() {
        return Collections.unmodifiableCollection(places.values());
    }

    /**
     * Creates a transition with the specified id, resource expression and
     * timeout in this case type. <p>
     *
     * The resource expression defines which resources can perform work
     * items associated with this transition. If it is <code>null</code> or
     * an empty string, every resource can do it. <p>
     *
     * The timeout sets a number of milliseconds this transition will wait
     * before it fires automatically. If the timeout is negative, the
     * transition will never fire automatically. If it is zero, this
     * transition will fire immediately. <p>
     *
     * NOTICE: As of this version of Bossa, the only timeout possible is
     * zero or no timeout. <p>
     *
     * @param id the transition id.
     * @param resource the expression to select the resource responsible by
     *        this transition.
     * @param timeout the number of milliseconds this transition will wait
     *                before it fires automatically.
     * @return the created transition.
     * @see com.bigbross.bossa.resource.Expression
     */
    public Transition registerTransition(String id, String resource,
                                         long timeout) {
        Transition trans = new Transition(this, id, resource, timeout);
        transitions.put(id, trans);
        return trans;
    }

    /**
     * Creates a transition with the specified id and resource expression,
     * but with no timeout, in this case type. <p>
     *
     * NOTICE: As of this version of Bossa, the only timeout possible is
     * zero or no timeout. <p>
     *
     * @param id the transition id.
     * @param resource the expression to select the resource responsible by
     *        this transition.
     * @return the created transition.
     * @see com.bigbross.bossa.resource.Expression
     */
    public Transition registerTransition(String id, String resource) {
        return registerTransition(id, resource, -1);
    }

    /**
     * Returns the transition of this case type that has the specified id. <p>
     *
     * @param id the transition id.
     * @return the transition,
     *         or <code>null</code> if there is no such transition.
     */
    public Transition getTransition(String id) {
        return (Transition) transitions.get(id);
    }

    /**
     * Returns all transitions of this case type. <p>
     *
     * @return a collection of all transitions of this case type.
     */
    Collection getTransitions() {
        return Collections.unmodifiableCollection(transitions.values());
    }

    /**
     * Returns the resource registry with the local resources of this
     * case type. <p>
     *
     * @return the resource registry with the local resources of this
     * case type.
     */
    ResourceRegistry getResourceRegistry() {
        return resources;
    }

    /**
     * Returns all resource groups of this case type. Use these resources to
     * associate (by includes and excludes) system resources with case type
     * resources. <p>
     *
     * @return a list of all resource groups of this case type.
     */
    public List getResources() {
        return resources.getResources();
    }

    /**
     * Returns the next case id for this case type. The case id is a
     * positive integer. <p>
     *
     * @return the next case id.
     */
    int nextCaseId() {
        return caseSequence++;
    }

    /**
     * Creates a new case using the state of the case type template. <p>
     *
     * This method bypasses the usual case opening process and should be used
     * with caution to override the normal way a case is created. <p>
     *
     * @return the newly created case.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     */
    public Case openCase() throws BossaException {
        WFNetTransaction openCase = new OpenCase(this.getId(), null);
        return (Case) getBossa().execute(openCase);
    }

    /**
     * Creates a new case using the provided state as the initial token count.
     * If the state is <code>null</code>, the state of the case type
     * template is used instead. <p>
     *
     * This method bypasses the usual case opening process and should be used
     * with caution to override the normal way a case is created. <p>
     *
     * @param state the initial token count as a map (<code>String</code>,
     *              <code>Integer</code>), indexed by the place id.
     *              This state map must have a token count for every place.
     * @return the newly created case.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     */
    public Case openCase(Map state) throws BossaException {
        WFNetTransaction openCase = new OpenCase(this.getId(), state);
        return (Case) getBossa().execute(openCase);
    }

    /**
     * Creates a new case using the provided state as the initial token count.
     * If the state is <code>null</code>, the state of the case type
     * template is used instead. <p>
     *
     * This method will not persist the result of its activation and should
     * be used only internally as a part of a persistent transaction. <p>
     *
     * @param state the initial token count as a map (<code>String</code>,
     *              <code>Integer</code>), indexed by the place id.
     *              This state map must have a token count for every place.
     * @return the newly created case.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     */
    Case openCaseImpl(Map state) throws BossaException {
        if (state == null) {
            state = template.getState();
        }
        Case caze = new Case(this, state, template.getAttributes());
        cases.put(new Integer(caze.getId()), caze);
        resources.registerSubContext(caze.getResourceRegistry());
        WFNetEvents queue = new WFNetEvents();
        queue.newCaseEvent(getBossa(), WFNetEvents.ID_OPEN_CASE, caze);
        queue.notifyAll(getBossa());
        return caze;
    }

    /**
     * Closes an open case. <p>
     *
     * @param caze the case to be closed.
     * @return <code>true</code> if the case was open and could be closed,
     *         <code>false</code> otherwise.
     */
    boolean closeCase(Case caze) {
        if (cases.remove(new Integer(caze.getId())) != null) {
            resources.removeSubContext(caze.getResourceRegistry());
            WFNetEvents queue = new WFNetEvents();
            queue.newCaseEvent(getBossa(), WFNetEvents.ID_CLOSE_CASE, caze);
            queue.notifyAll(getBossa());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Builds the template case that will spawn all other cases. Call this
     * method after you have finished building the case type. <p>
     *
     * @param attributes the attributes of the template case.
     * @exception SetAttributeException if the underlying expression
     *            evaluation system has problems setting an attribute.
     * @exception EvaluationException if an expression evaluation error
     *            occurs.
     */
    public void buildTemplate(Map attributes)
        throws BossaException {

        if (template != null) {
            return;
        }

        Map state = new HashMap(places.size());
        for (Iterator i = places.values().iterator(); i.hasNext(); ) {
            Place p = (Place) i.next();
            state.put(p.getId(), new Integer(p.getInitialMarking()));
        }
        template = new Case(this, state, attributes);

        for (Iterator i = getTransitions().iterator(); i.hasNext(); ) {
            Transition t = (Transition) i.next();
            ArrayList edges = new ArrayList();
            edges.addAll(t.getInputEdges());
            edges.addAll(t.getOutputEdges());
            for (Iterator j = edges.iterator(); j.hasNext(); ) {
                ((Edge) j.next()).weight(template);
            }
        }
    }

    /**
     * Sets the case type manager this case type is registered into. <p>
     *
     * @param caseTypeManager the case type manager this case type is
     *        registered into.
     */
    void setCaseTypeManager(CaseTypeManager caseTypeManager) {
        this.caseTypeManager = caseTypeManager;
    }

    /**
     * Returns the case type manager this case type is registered into. <p>
     *
     * @return the case type manager this case type is registered into.
     */
    CaseTypeManager getCaseTypeManager() {
        return caseTypeManager;
    }

    /**
     * Returns the bossa engine this case type is part, if any. <p>
     *
     * @return the bossa engine this case type is part, <code>null</code> if
     *         not part of a bossa engine.
     */
    Bossa getBossa() {
        if (getCaseTypeManager() != null) {
            return getCaseTypeManager().getBossa();
        } else {
            return null;
        }
    }

    /**
     * Returns a case by its id. <p>
     *
     * @param id the case id.
     * @return the case with the provided id, <code>null</code> if
     *         this case does not exist.
     */
    public Case getCase(int id) {
        if (id == 0) {
            return template;
        } else {
            return (Case) cases.get(new Integer(id));
        }
    }

    /**
     * Returns all cases of this case type. <p>
     *
     * @return a list of all active cases.
     */
    public List getCases() {
        ArrayList caseList = new ArrayList();
        caseList.addAll(cases.values());
        return caseList;
    }

    /**
     * Returns the list of all work items of the cases of this case type. <p>
     *
     * @return the list of work itens of this case type.
     */
    public List getWorkItems() {
        return getWorkItems(false);
    }

    /**
     * Returns the list of all work items of the cases of this case type.
     * If desired, the initial work item(s) can be returned. Opening an
     * initial work item will create a new case. <p>
     *
     * @param getInitial set to <code>true</code> to get the initial work
     *                   items and to <code>false</code> to only get the
     *                   standard work items.
     * @return the list of work itens of this case type.
     */
    public List getWorkItems(boolean getInitial) {
        ArrayList items = new ArrayList();
        if (getInitial) {
            items.addAll(template.getWorkItems());
        }
        Iterator i = cases.values().iterator();
        while (i.hasNext()) {
            items.addAll(((Case) i.next()).getWorkItems());
        }
        return items;
    }

    /**
     * Returns the list of all activities of the cases of this case type. <p>
     *
     * @return the list of activities of this case type.
     */
    public List getActivities() {
        ArrayList acts = new ArrayList();
        Iterator i = cases.values().iterator();
        while (i.hasNext()) {
            acts.addAll(((Case) i.next()).getActivities());
        }
        return acts;
    }
}
