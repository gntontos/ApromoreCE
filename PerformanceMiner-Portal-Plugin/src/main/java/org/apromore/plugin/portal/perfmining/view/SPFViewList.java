/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2017 Bruce Nguyen.
 * %%
 * Copyright (C) 2018 - 2020 The University of Melbourne.
 * Copyright (C) 2020 Apromore Pty Ltd.
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apromore.plugin.portal.perfmining.view;

import org.apromore.plugin.portal.perfmining.view.queue.SPFQueueArrivalRateView;
import org.apromore.plugin.portal.perfmining.view.queue.SPFQueueCIPView;
import org.apromore.plugin.portal.perfmining.view.queue.SPFQueueDepartureRateView;
import org.apromore.plugin.portal.perfmining.view.queue.SPFQueueTISView;
import org.apromore.plugin.portal.perfmining.view.service.SPFArrivalRateView;
import org.apromore.plugin.portal.perfmining.view.service.SPFCIPView;
import org.apromore.plugin.portal.perfmining.view.service.SPFDepartureRateView;
import org.apromore.plugin.portal.perfmining.view.service.SPFExitRateView;
import org.apromore.plugin.portal.perfmining.view.service.SPFFlowEfficiencyView;
import org.apromore.plugin.portal.perfmining.view.system.SPFMainView;
import org.apromore.service.perfmining.models.SPF;
import org.apromore.service.perfmining.models.Stage;
 
public class SPFViewList {
    private SPFViewTreeNode root;
    
    public SPFViewList(SPF spf) {
        SPFViewTreeNode[] stageNodes = new SPFViewTreeNode[spf.getStages().size() + 1];
        
        // SPF Overall nodes
        int i=0;
        stageNodes[i++] = new SPFViewTreeNode(new SPFMainView(spf, "", "", "SPF", false));
//        stageNodes[i++] = new SPFViewTreeNode(new SPFMultipleTISView(spf, "", "", "All TIS View", false));
//        stageNodes[i++] = new SPFViewTreeNode(new SPFAllFlowEfficiencyView(spf, "", "", "All Flow Efficiency", false));
        
        for (int j=0;j<spf.getStages().size();j++) {
            Stage stage = spf.getStages().get(j);
            //Queue
            SPFViewTreeNode[] queueNodes = new SPFViewTreeNode[4];
            queueNodes[0] = new SPFViewTreeNode(
                            new SPFQueueArrivalRateView(spf, stage.getName(), "Queue", "Arrival Rate", false));
            queueNodes[1] = new SPFViewTreeNode(
                            new SPFQueueDepartureRateView(spf, stage.getName(), "Queue", "Departure Rate", false));
            queueNodes[2] = new SPFViewTreeNode(
                            new SPFQueueCIPView(spf, stage.getName(), "Queue", "Cases in Process", false));
            queueNodes[3] = new SPFViewTreeNode(
                            new SPFQueueTISView(spf, stage.getName(), "Queue", "Time in Stage", false));
            SPFViewTreeNode queueNode = new SPFViewTreeNode(new SPFView(spf, stage.getName(), "Queue", "", true), queueNodes);
            
            //Service
            SPFViewTreeNode[] serviceNodes = new SPFViewTreeNode[6];
            serviceNodes[0] = new SPFViewTreeNode(
                                new SPFArrivalRateView(spf, stage.getName(), "Service", "Arrival Rate", false));
            serviceNodes[1] = new SPFViewTreeNode(
                                new SPFDepartureRateView(spf, stage.getName(), "Service", "Departure Rate", false));
            serviceNodes[2] = new SPFViewTreeNode(
                                new SPFExitRateView(spf, stage.getName(), "Service", "Exit Rate", false));
            serviceNodes[3] = new SPFViewTreeNode(
                                new SPFCIPView(spf, stage.getName(), "Service", "Cases in Process", false));
            serviceNodes[4] = new SPFViewTreeNode(
                                new SPFQueueTISView(spf, stage.getName(), "Service", "Time in Stage", false));
            serviceNodes[5] = new SPFViewTreeNode(
                                new SPFFlowEfficiencyView(spf, stage.getName(), "Service", "Flow Efficiency", false));
            SPFViewTreeNode serviceNode = new SPFViewTreeNode(new SPFView(spf, stage.getName(), "Service", "", true), serviceNodes);
            
            SPFViewTreeNode stageNode = new SPFViewTreeNode(new SPFView(spf, stage.getName(), "", "", true), 
                                                               new SPFViewTreeNode[] {queueNode, serviceNode});
            stageNodes[i+j] = stageNode;
        }
        
        root = new SPFViewTreeNode(new SPFView(spf, "", "", "System", true), stageNodes);
    }
    public SPFViewTreeNode getRoot() {
        return root;
    }
}
