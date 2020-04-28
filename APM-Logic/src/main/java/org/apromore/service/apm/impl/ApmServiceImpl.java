/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2017 Queensland University of Technology.
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

package org.apromore.service.apm.impl;

import hub.top.petrinet.PetriNet;
import nl.rug.ds.bpm.variability.SpecificationToXML;
import nl.rug.ds.bpm.variability.VariabilitySpecification;
import org.apromore.service.apm.APMService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ApmServiceImpl implements APMService {
    String text = "";
    String xml = "";

    @Override
    public String[] getSpecification(PetriNet[] nets, String silentPrefix, Boolean viresp, Boolean viprec, Boolean veiresp, Boolean veresp, Boolean vconf, Boolean vpar) {
        VariabilitySpecification vs = new VariabilitySpecification(nets, "silent");

        return SpecificationToXML.getOutput(vs, "silent", viresp, viprec, veiresp, veresp, vconf, vpar);
    }
}