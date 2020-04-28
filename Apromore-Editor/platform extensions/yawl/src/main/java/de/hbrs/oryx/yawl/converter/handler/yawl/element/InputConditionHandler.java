/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2015 - 2017 Queensland University of Technology.
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
package de.hbrs.oryx.yawl.converter.handler.yawl.element;

import org.oryxeditor.server.diagram.basic.BasicShape;
import org.yawlfoundation.yawl.elements.YExternalNetElement;
import org.yawlfoundation.yawl.elements.YNetElement;

import de.hbrs.oryx.yawl.converter.context.YAWLConversionContext;

/**
 * Converts the input condition of a YAWL net to an Oryx shape
 * 
 * @author Felix Mannhardt (Bonn-Rhein-Sieg University of Applied Sciences)
 * 
 */
public class InputConditionHandler extends ConditionHandler {

    public InputConditionHandler(final YAWLConversionContext context, final YNetElement netElement) {
        super(context, netElement);
    }

    @Override
    public void convert(final String parentId) {
        BasicShape condition = convertCondition(parentId, "InputCondition");
        getContext().putShape(parentId, getNetElement().getID(), condition);
        getContext().addPostsetFlows(parentId, ((YExternalNetElement) getNetElement()).getPostsetFlows());
    }

}
