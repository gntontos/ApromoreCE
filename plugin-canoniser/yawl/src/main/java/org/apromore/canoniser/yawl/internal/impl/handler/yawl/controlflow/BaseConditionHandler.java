/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2012 - 2017 Queensland University of Technology.
 * Copyright (C) 2012 Felix Mannhardt.
 * %%
 * Copyright (C) 2018 - 2020 The University of Melbourne.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.canoniser.yawl.internal.impl.handler.yawl.controlflow;

import org.apromore.cpf.EventType;
import org.apromore.cpf.StateType;
import org.yawlfoundation.yawlschema.ExternalConditionFactsType;
import org.yawlfoundation.yawlschema.ExternalNetElementType;

/**
 * Baseclass for converting a YAWL condition
 * 
 * @author <a href="mailto:felix.mannhardt@smail.wir.h-brs.de">Felix Mannhardt (Bonn-Rhein-Sieg University oAS)</a>
 * 
 * @param <T>
 *            has to be used as OutputCondition has a different type in JaxB
 */
public abstract class BaseConditionHandler<T> extends ExternalNetElementHandler<T> {

    /*
     * (non-Javadoc)
     * 
     * @see org.apromore.canoniser.yawl.internal.impl.handler.yawl.controlflow.ExternalNetElementHandler#createState()
     */
    @Override
    protected StateType createState() {
        return createState(null);
    }

    protected EventType createEvent(final ExternalNetElementType eventElement, final String eventName) {
        final EventType event = CPF_FACTORY.createEventType();
        event.setId(generateUUID(CONTROLFLOW_ID_PREFIX, eventElement.getId()));
        event.setOriginalID(eventElement.getId());
        event.setName(eventName);
        getConvertedParent().getNode().add(event);
        return event;
    }

    protected StateType createState(final ExternalConditionFactsType condition) {
        final StateType state = CPF_FACTORY.createStateType();
        if (condition == null) {
            state.setId(generateUUID());
            state.setOriginalID(null);
        } else {
            state.setId(generateUUID(CONTROLFLOW_ID_PREFIX, condition.getId()));
            state.setOriginalID(condition.getId());
            state.setName(condition.getName());
        }
        getConvertedParent().getNode().add(state);
        return state;
    }

}
