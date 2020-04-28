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

package ee.ut.mining.log.poruns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.deckfour.xes.model.XTrace;

import com.google.common.collect.Multimap;

import ee.ut.mining.log.ConcurrencyRelations;

public class AbstractingShortLoopsPORun extends PORun {
	
	public AbstractingShortLoopsPORun(ConcurrencyRelations alphaRelations, XTrace trace, String traceId) {
		super(alphaRelations, trace, traceId);
		Multimap<Integer, Integer> successors = asSuccessorsList();
		Multimap<Integer, Integer> predecessors = asPredecessorsList();
		Stack<Integer> open = new Stack<>();
		Set<Integer> visited = new HashSet<>();
		open.push(getSource());
		
		String prevLabel = null;
		List<Integer> list = null;
		List<List<Integer>> lists = new ArrayList<>();
		Set<Integer> verticesToAdjust = new TreeSet<>();
		while (!open.isEmpty()) {
			Integer curr = open.pop();
			visited.add(curr);
			String label = labels.get(curr);
			if (label == null) {
				System.err.println("found an event without a label (null value instead)");
				continue;
			}
			if (!label.equals(prevLabel)) {
				if (list != null && list.size() > 1) {
					lists.add(list);
					verticesToAdjust.addAll(list);
					verticesToAdjust.remove(list.get(list.size()-1));
				}
				list = new ArrayList<>();
				prevLabel = label;
			}
			list.add(curr);
			for (Integer succ: successors.get(curr))
				if (!visited.contains(succ) && !open.contains(succ) && visited.containsAll(predecessors.get(succ)))
					open.push(succ);
		}
		
		if (!lists.isEmpty())
			System.out.println("Loops: " + lists);
		System.out.println("To adjust: " + verticesToAdjust);
		
		for (Integer vertex: verticesToAdjust) {
			int index = vertexIndexMap.get(vertex);
			Arrays.fill(adjmatrix[index], false);
		}
		
		for (List<Integer> seq: lists) {
			Integer first = seq.get(0);
			Integer last = seq.get(seq.size()-1);
			adjmatrix[vertexIndexMap.get(first)][vertexIndexMap.get(last)] = true;
			verticesToAdjust.remove(first);
//			labels.put(first, labels.get(first) + "_LStart");
//			labels.put(last, labels.get(last) + "_LEnd");
//			System.out.printf("%d -> %d\n", first, last);
		}
//		System.out.println("To remove: " + verticesToAdjust);
		
		for (Integer v: verticesToAdjust)
			labels.remove(v);
	}
}
