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
package au.ltl.domain;

import java.util.LinkedList;

import org.codehaus.jackson.map.ObjectMapper;

public class Actions {

	private String ruleName; //name of the rule(is the description given by the user). 
							// If it is the conjunction of rules, its value will be "All Constraints"
	
	private String taskId; //Id of the original Task in the BPMN model to which the action are referring to.
						   //If an action in the actionList is an add, this add must be performed in the edge
						   //which target task is the one with id=taskId.
						   
	private int numberOfTraces; // total number of traces passing through the task with id=taskId
	
	private LinkedList<Action> actionList; //in this list there all the actions performed by the traces passing through 
										   // the task.
	
	private LinkedList<Repair> repairList; //in this list there all the repairs that can be performed on the task.
										   // This list is meaningful only if ruleName="All Constraints"
	
	
	public Actions(String ruleName, String taskId, int numberOfTraces, LinkedList<Action> actionList) {
		super();
		this.ruleName = ruleName;
		this.taskId = taskId;
		this.numberOfTraces=numberOfTraces;
		this.actionList = actionList;
		this.repairList = new LinkedList<Repair>();
		
	}
	
	public int getNumberOfTraces() {
		return numberOfTraces;
	}

	public void setNumberOfTraces(int numberOfTraces) {
		this.numberOfTraces = numberOfTraces;
	}

	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public LinkedList<Action> getActionList() {
		return actionList;
	}
	public void setActionList(LinkedList<Action> actionList) {
		this.actionList = actionList;
	}
	public LinkedList<Repair> getRepairList() {
		return repairList;
	}
	public void setRepairList(LinkedList<Repair> repairList) {
		this.repairList = repairList;
	}

	public static String toJSON(Actions actions) {
		try {
			ObjectMapper mapper = new ObjectMapper();

			return mapper.writeValueAsString(actions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	@Override
	public String toString() {
		return "Actions [ruleName=" + ruleName + ", taskId=" + taskId + ", numberOfTraces=" + numberOfTraces
				+ ", actionList=" + actionList + ", repairList=" + repairList + "]";
	}
	
	
}
