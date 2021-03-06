/*-
 * #%L
 * This file is part of "Apromore Community".
 * 
 * Copyright (C) 2012 - 2017 Queensland University of Technology.
 * Copyright (C) 2012 Felix Mannhardt.
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
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
package org.apromore.canoniser.yawl.internal.impl.handler.yawl.resource;

import org.apromore.canoniser.exception.CanoniserException;
import org.apromore.canoniser.yawl.internal.utils.ConversionUtils;
import org.apromore.cpf.NonhumanType;
import org.apromore.cpf.NonhumanTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yawlfoundation.yawlschema.ExternalTaskFactsType;
import org.yawlfoundation.yawlschema.WebServiceGatewayFactsType;

public class AutomatedTaskResourceingHandler extends ResourceingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutomatedTaskResourceingHandler.class);

    /*
     * (non-Javadoc)
     *
     * @see org.apromore.canoniser.yawl.internal.impl.handler.yawl.resource.ResourceingHandler#convert()
     */
    @Override
    public void convert() throws CanoniserException {

        String automaticName = "Unkown Service or Codelet";

        final ExternalTaskFactsType parentTask = (ExternalTaskFactsType) getOriginalParent();

        if (parentTask.getDecomposesTo() != null) {
            final WebServiceGatewayFactsType decomposition = (WebServiceGatewayFactsType) getContext().getDecompositionByID(
                    parentTask.getDecomposesTo().getId());
            if (decomposition.getCodelet() != null) {
                automaticName = decomposition.getCodelet();
            } else if (decomposition.getYawlService() != null) {
                automaticName = decomposition.getYawlService().getOperationName() + " using WSDL " + decomposition.getYawlService().getWsdlLocation();
            }
        }

        final NonhumanType resourceType = CPF_FACTORY.createNonhumanType();
        resourceType.setType(NonhumanTypeEnum.SOFTWARE_SYSTEM);
        resourceType.setId(generateUUID());
        resourceType.setOriginalID(null);
        resourceType.setName(automaticName);
        getContext().getCanonicalResult().getResourceType().add(resourceType);
        createResourceReference(resourceType, null);
        LOGGER.debug("Converted YAWL codelet or service {} to CPF NonHumanType {}", automaticName, ConversionUtils.toString(resourceType));
    }

}
