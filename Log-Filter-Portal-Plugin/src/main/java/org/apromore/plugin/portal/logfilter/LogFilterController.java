/*-
 * #%L
 * This file is part of "Apromore Community".
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

package org.apromore.plugin.portal.logfilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apromore.apmlog.APMLog;
import org.apromore.apmlog.filter.APMLogFilter;
import org.apromore.apmlog.filter.APMLogFilterPackage;
import org.apromore.apmlog.filter.rules.LogFilterRule;
import org.apromore.logfilter.LogFilterService;
import org.apromore.logfilter.criteria.LogFilterCriterion;
import org.apromore.logfilter.criteria.factory.LogFilterCriterionFactory;
import org.apromore.logfilter.criteria.model.Action;
import org.apromore.logfilter.criteria.model.Containment;
import org.apromore.logfilter.criteria.model.Level;
import org.apromore.logfilter.criteria.model.LogFilterTypeSelector;
import org.apromore.logfilter.stats.LogStatistics;
import org.apromore.plugin.portal.PortalContext;
import org.apromore.plugin.portal.logfilter.generic.LogFilterOutputResult;
import org.apromore.plugin.portal.logfilter.generic.LogFilterResultListener;
import org.deckfour.xes.model.XLog;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * This is the main window controller for log filter plugin
 * Created by Raffaele Conforti (conforti.raffaele@gmail.com) on 05/08/2018.
 * Modified by Bruce Nguyen (20/08/2019)
 * Modified by Chii Chang (04/09/2019)
 */
public class LogFilterController {
	private static final long serialVersionUID = 1L;
	private PortalContext portalContext;
    private List<LogFilterCriterion> criteria;
    
    private LogFilterService logFilterService;
    private LogFilterCriterionFactory logFilterCriterionFactory;

    private Window filterSelectorW;
    private Listbox criteriaList;
    private XLog log;
    
    private LogFilterResultListener resultListener;

//    private Map<String, Set<String>> directFollowMap = new HashMap<String, Set<String>>();
//    private Map<String, Set<String>> eventualFollowMap = new HashMap<String, Set<String>>();
    private Map<Integer, List<String>> variantEventsMap = new HashMap<Integer, List<String>>();
    private Set<String> eventNameSet;


    public LogFilterController(PortalContext portalContext, XLog log,
    							LogFilterService logFilterService,
    							LogFilterCriterionFactory logFilterCriterionFactory,
    							LogFilterResultListener resultListener) throws IOException {
    	this.log = log;
    	LogStatistics stats = new LogStatistics(this.log);
        /**
         * Get the log with case-variant values
         */
        this.log = stats.getLog();
//        this.directFollowMap = stats.getDirectFollowMap();
//        this.eventualFollowMap = stats.getEventualFollowMap();
        this.eventNameSet = stats.getEventNameSet();
        this.variantEventsMap = stats.getVariantEventsMap();
//        XAttributeMap xm = this.log.get(0).getAttributes();
//        System.out.println(xm);

    	this.logFilterService = logFilterService;
    	this.logFilterCriterionFactory = logFilterCriterionFactory;
    	this.resultListener = resultListener;
    	initialize(portalContext, log, "concept:name", new ArrayList<LogFilterRule>(), stats.getStatistics(), 
    				stats.getMinTimestamp(), stats.getMaxTimetamp());
    }
    
    public LogFilterController(PortalContext portalContext, 
    							LogFilterService logFilterService,
    							LogFilterCriterionFactory logFilterCriterionFactory,
    							XLog log, String classifierAttribute, 
								List<LogFilterRule> originalCriteria, 
								LogFilterResultListener resultListener) throws IOException {
        /**
         * Get the log with case-variant values
         */
        LogStatistics logStats = new LogStatistics(log);
//        this.directFollowMap = logStats.getDirectFollowMap();
//        this.eventualFollowMap = logStats.getEventualFollowMap();
        this.eventNameSet = logStats.getEventNameSet();
        this.variantEventsMap = logStats.getVariantEventsMap();
        
    	this.logFilterService = logFilterService;
    	this.logFilterCriterionFactory = logFilterCriterionFactory;
    	this.resultListener = resultListener;
    	initialize(portalContext, log, classifierAttribute, originalCriteria, logStats.getStatistics(), 
    				logStats.getMinTimestamp(), logStats.getMaxTimetamp());
    }
    
    private void initialize(PortalContext portalContext, XLog log, String label, 
    							List<LogFilterRule> originalCriteria, 
    							Map<String, Map<String, Integer>> options_frequency, 
    							long min, long max) throws IOException {
    	this.portalContext = portalContext;
    	this.log = log;
    	this.criteria = logFilterCriterionFactory.convertFilterRulesToFilterCriteria(originalCriteria);
        
        filterSelectorW = (Window) portalContext.getUI().createComponent(getClass().getClassLoader(), "zul/filterCriteria.zul", null, null);
        filterSelectorW.setTitle("Log Filter Criteria");

        criteriaList = (Listbox) filterSelectorW.getFellow("criteria");
        updateList();

        Button okButton = (Button) filterSelectorW.getFellow("filterOkButton");
        Button cancelButton = (Button) filterSelectorW.getFellow("filterCancelButton");
        Button createButton = (Button) filterSelectorW.getFellow("filterCreateButton");
        Button editButton = (Button) filterSelectorW.getFellow("filterEditButton");
        Button moveUpButton = (Button) filterSelectorW.getFellow("filterMoveUpButton");
        Button moveDownButton = (Button) filterSelectorW.getFellow("filterMoveDownButton");
        Button removeButton = (Button) filterSelectorW.getFellow("filterRemoveButton");
        Button removeAllButton = (Button) filterSelectorW.getFellow("filterRemoveAllButton");

        okButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws InterruptedException {
                save();
            }
        });
        cancelButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws InterruptedException {
            	filterSelectorW.detach();
            }
        });        
        createButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                new FilterCriterionDialog(portalContext, label, LogFilterController.this,
                							logFilterCriterionFactory,
                							criteria, options_frequency, min, max);
            }
        });
        editButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (criteriaList.getSelectedIndex() > -1) {
                    new FilterCriterionDialog(portalContext, label, LogFilterController.this,
                    				logFilterCriterionFactory,
                    				criteria, options_frequency, min, max, criteriaList.getSelectedIndex());
                }
            }
        });
        moveUpButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if(criteriaList.getSelectedIndex() > 0) {
                    int pos = criteriaList.getSelectedIndex();
                    LogFilterCriterion criterion = criteria.get(pos);
                    criteria.set(pos, criteria.get(pos - 1));
                    criteria.set(pos - 1, criterion);
                    updateList();
                }
            }
        });
        moveDownButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if(criteriaList.getSelectedIndex() > -1 && criteriaList.getSelectedIndex() < criteria.size() - 1) {
                    int pos = criteriaList.getSelectedIndex();
                    LogFilterCriterion criterion = criteria.get(pos);
                    criteria.set(pos, criteria.get(pos + 1));
                    criteria.set(pos + 1, criterion);
                    updateList();
                }
            }
        });
        removeButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) {
                int selected = criteriaList.getSelectedIndex();
                if(selected > -1) {
                    criteria.remove(selected);
                    criteriaList.removeItemAt(selected);
                }
            }
        });
        removeAllButton.addEventListener("onClick", new EventListener<Event>() {
            @Override
            public void onEvent(Event event) {
                criteria.clear();
                updateList();
            }
        });
        filterSelectorW.doModal();
    }

    private void save() {
        try {
        	//XLog filteredLog = this.logFilterService.filter(this.log, criteria);
        	List<LogFilterRule> filterRules = logFilterCriterionFactory.convertFilterCriteriaToFilterRules(criteria);
        	APMLog apmLog = new APMLog(this.log);
            APMLogFilter apmLogFilter = new APMLogFilter(apmLog);
        	apmLogFilter.filter(filterRules);
            APMLog output = apmLogFilter.getApmLog();
        	if (output.getTraceList().isEmpty()) {
        		Messagebox.show("The log is empty after applying all filter criteria! Please use different criteria.");
        	}
        	else {
    		    filterSelectorW.detach();
    		    //resultListener.onPluginExecutionFinished(new LogFilterOutputResult(filteredLog, criteria));
    		    apmLogFilter.updatePrevious(); //20200212
                APMLogFilterPackage afp = new APMLogFilterPackage("",
                        output, apmLogFilter.getPLog(), this.log, filterRules);
                EventQueue eqCriteria = EventQueues.lookup("apmlog_filter_package", EventQueues.DESKTOP, true);
                eqCriteria.publish(new Event("", null, afp));
    		    
        	}
        }
        catch (Exception ex) {
            Messagebox.show(ex.getMessage());
        }
    }
    
    public void updateList() {
        ListModelList<String> model = new ListModelList<>();
        for(LogFilterCriterion criterion : criteria) {
            String label = criterion.toString();
            for(String type: LogFilterTypeSelector.getStandardCodes()) {
                label = label.replaceAll(type, LogFilterTypeSelector.getNameFromCode(type));
            }

            if(label.contains("Timeframe")) {
//            if(label.contains(LogFilterTypeSelector.getNameFromCode(Type.TIME_TIMESTAMP.toString()))) {
                String tmp_label = label.substring(0, label.indexOf("equal"));
                String s, e;
                if (label.contains("OR <")) {
                	s = label.substring(label.indexOf(">") + 1, label.indexOf(" OR <"));
                	e = label.substring(label.indexOf(" OR <") + 5, label.indexOf("]"));
                }
                else {
                	e = label.substring(label.indexOf("<") + 1, label.indexOf(" OR >"));
                	s = label.substring(label.indexOf(" OR >") + 5, label.indexOf("]"));
                }
                label = tmp_label
                        + " is between "
                        + (new Date(Long.parseLong(s))).toString()
                        + " and "
                        + (new Date(Long.parseLong(e))).toString();
                if(criterion.getLevel() == Level.TRACE) {
                    if(criterion.getAction() == Action.RETAIN) {
                        if(criterion.getContainment()== Containment.CONTAIN_ANY) {
                            label = "Retain all traces containing an event whose Timestamp is between "
                                    + (new Date(Long.parseLong(s))).toString()
                                    + " and "
                                    + (new Date(Long.parseLong(e))).toString();
                        }else if(criterion.getContainment()== Containment.CONTAIN_ALL) {
                            label = "Retain all traces where all events have Timestamp between "
                                    + (new Date(Long.parseLong(s))).toString()
                                    + " and "
                                    + (new Date(Long.parseLong(e))).toString();
                        }
                    }
                }
            }
//            if(label.contains("Duration")) {
//                String d = label.substring(label.indexOf(">") + 1, label.indexOf("]"));
//                if(label.contains("Remove")) {
//                    label = "Remove all traces with a Duration greater than " + d; //TimeConverter.stringify(d);
//                }else {
//                    label = "Retain all traces with a Duration greater than " + d; //TimeConverter.stringify(d);
//                }
//            }
            if(criterion.getAttribute().equals("duration:range")) {
                String s = "";
                String e = "";
                for(String v : criterion.getValue()) {
                    if(v.startsWith(">")) s = v.substring(1);
                    if(v.startsWith("<")) e = v.substring(1);
                }
                if(label.contains("Remove")) {
                    label = "Remove all traces with a duration between " + s + " to " + e;
                }else {
                    label = "Retain all traces with a duration between " + s + " to " + e;
                }
            }
            if(label.contains("Directly-follows relation")) {
                String d = label.substring(label.indexOf("["));
                if(label.contains("Remove")) {
                    label = "Remove all traces containing the direct follow relation " + d;
                }else {
                    label = "Retain all traces which have a directly-follows relation equal to " + d;
                }
            }
            if(label.contains("Eventually-follows relation")) {
                String d = label.substring(label.indexOf("["));
                if(label.contains("Remove")) {
                    label = "Remove all traces containing the eventually follow relation " + d;
                }else {
                    label = "Retain all traces which have an eventually-follows relation equal to " + d;
                }
            }
            model.add(label);
        }
        criteriaList.setModel(model);
    }

//    public Map<String, Set<String>> getDirectFollowMap() {
//        return directFollowMap;
//    }
//
//    public Map<String, Set<String>> getEventualFollowMap() {
//        return eventualFollowMap;
//    }


    public Set<String> getEventNameSet() {
        return eventNameSet;
    }

    public Map<Integer, List<String>> getVariantEventsMap() {
        return variantEventsMap;
    }
}
