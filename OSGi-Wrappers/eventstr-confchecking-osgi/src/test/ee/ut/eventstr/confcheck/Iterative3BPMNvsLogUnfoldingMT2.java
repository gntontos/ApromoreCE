/*-
 * #%L
 * This file is part of "Apromore Community".
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
package ee.ut.eventstr.confcheck;

import hub.top.petrinet.PetriNet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.jbpt.utils.IOUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import ee.ut.bpmn.BPMNProcess;
import ee.ut.bpmn.utils.BPMN2Reader;
import ee.ut.bpmn.utils.Petrifier;
import ee.ut.eventstr.NewUnfoldingPESSemantics;
import ee.ut.eventstr.PrimeEventStructure;
import ee.ut.eventstr.SinglePORunPESSemantics;
import ee.ut.eventstr.comparison.PrunedOpenPartialSynchronizedProduct;
import ee.ut.mining.log.ConcurrencyRelations;
import ee.ut.mining.log.XLogReader;
import ee.ut.mining.log.poruns.AbstractingShort12LoopsPORun;
import ee.ut.mining.log.poruns.PORun;
import ee.ut.mining.log.poruns.PORuns;
import ee.ut.mining.log.poruns.pes.PORuns2PES;
import ee.ut.nets.unfolding.BPstructBP.MODE;
import ee.ut.nets.unfolding.Unfolder_PetriNet;
import ee.ut.nets.unfolding.Unfolding2PES;

public class Iterative3BPMNvsLogUnfoldingMT2 {
	Lock lock = new ReentrantLock();
	Condition condition = lock.newCondition();
	
	AtomicInteger currentSink = new AtomicInteger();
	
	AtomicInteger finishedThreadCount = new AtomicInteger(0);
	
	final int totalThreadCount = 8;
	
	@Test
	public void test() throws Exception {
		String logfilename = 
//				"nolooplog"
//				"innerlog"
//				"outerlog"
//				"nestedlog"
//				"overlappinglog"
				"cpreal"
//				"base"
				;
		
		String bpmnfilename = 
//				"noloop"
//				"inner"
//				"outer"
//				"nested"
//				"overlapping"
				"cp_lgb"
//				"I1"
				;
		
		String logfiletemplate = 
				"models/RunningExample/%s.mxml"
//				"models/AtomicTest/%s.mxml"
				;
		
		String bpmnfolder = 
				"models/RunningExample/"
//				"models/simple/"
//				"models/AtomicTest/"
				;
		
		long totalStartTime = System.nanoTime();
		
		new Thread(new PSPgenerator(logfilename, logfiletemplate, bpmnfilename, bpmnfolder, true)).start();
		for (int i = 1; i < totalThreadCount; i++) {
			new Thread(new PSPgenerator(logfilename, logfiletemplate, bpmnfilename, bpmnfolder, false)).start();
		}
		
		// wait for the threads to finish
	    lock.lock();
	    while (finishedThreadCount.get() < totalThreadCount) {
	    	condition.await();
	    }
	    lock.unlock();

	    System.out.println("Total time: " + (System.nanoTime() - totalStartTime) / 1000 / 1000 + "ms");
	}
	
	public NewUnfoldingPESSemantics<Integer> getUnfoldingPESExample(String filename, String folder) throws JDOMException, IOException {
		BPMNProcess<Element> model = BPMN2Reader.parse(new File(folder + filename + ".bpmn"));
		Petrifier<Element> petrifier = new Petrifier<Element>(model);
		PetriNet net = petrifier.petrify(model.getSources().iterator().next(), model.getSinks().iterator().next());
		System.out.println(model.getLabels());
		
		Set<String> labels = new HashSet<String>();
		for (Integer node: model.getVisibleNodes())
			labels.add(model.getName(node));

		labels.remove("start");
		
		Unfolder_PetriNet unfolder = new Unfolder_PetriNet(net, MODE.ESPARZA);
		unfolder.computeUnfolding();
		PetriNet bp = unfolder.getUnfoldingAsPetriNet();
		
		IOUtils.toFile("net.dot", net.toDot());
		IOUtils.toFile("bp.dot", bp.toDot());
		Unfolding2PES pes = new Unfolding2PES(unfolder.getSys(), unfolder.getBP(), labels);
		NewUnfoldingPESSemantics<Integer> pessem = new NewUnfoldingPESSemantics<Integer>(pes.getPES(), pes);
		IOUtils.toFile("bpmnpes.dot", pessem.toDot());
		return pessem;
	}
	
	public PrimeEventStructure<Integer> getPES2() {
		Multimap<Integer, Integer> adj = HashMultimap.create();
		adj.put(0, 1);
		adj.put(1, 2);
		adj.put(1, 3);
		adj.put(2, 4);
		adj.put(3, 5);
		adj.put(4, 6);
		adj.put(6, 7);
		adj.put(7, 8);
		adj.put(8, 9);
		adj.put(5, 10);
		adj.put(9, 10);
		adj.put(10, 11);
		
		
		Multimap<Integer, Integer> conc = HashMultimap.create();
		conc.putAll(2, Arrays.asList(3, 5));
		conc.putAll(4, Arrays.asList(3, 5));
		conc.putAll(6, Arrays.asList(3, 5));
		conc.putAll(7, Arrays.asList(3, 5));
		conc.putAll(8, Arrays.asList(3, 5));
		conc.putAll(9, Arrays.asList(3, 5));
		conc.putAll(3, Arrays.asList(2, 4, 6, 7, 8, 9));
		conc.putAll(5, Arrays.asList(2, 4, 6, 7, 8, 9));

		List<String> labels = Arrays.asList("_0_", "A", "C", "D","B","D","C","F","G", "H", "I", "_1_");

		PrimeEventStructure<Integer> pes = PORuns2PES.getPrimeEventStructure(adj, conc, Arrays.asList(0), Arrays.asList(11), labels, "PES2");
		
		IOUtils.toFile("simple.dot", pes.toDot());
		return pes;
	}

	public PrimeEventStructure<Integer> getLogPESExample(String logfilename, String logfiletemplate) throws Exception {
		XLog log = XLogReader.openLog(String.format(logfiletemplate, logfilename));
		
		Multimap<String, String> concurrency = HashMultimap.create();
		concurrency.put("B", "D"); concurrency.put("D", "B");
		concurrency.put("C", "D"); concurrency.put("D", "C");
		concurrency.put("E", "D"); concurrency.put("D", "E");
		concurrency.put("F", "D"); concurrency.put("D", "F");
		concurrency.put("G", "D"); concurrency.put("D", "G");
		concurrency.put("H", "D"); concurrency.put("D", "H");
		
		ConcurrencyRelations alphaRelations = new ConcurrencyRelations() {
			public boolean areConcurrent(String label1, String label2) {
				return concurrency.containsEntry(label1, label2);
			}
		};
		
		File target = new File("target");
		if (!target.exists())
			target.mkdirs();
		
		System.out.println("Alpha relations created");
		
		PORuns runs = new PORuns();
		System.out.println("PO runs initialized");
		
		Set<Integer> eventlength = new HashSet<Integer>();
		Set<Integer> succ;
		
		for (XTrace trace: log) {
			PORun porun = new AbstractingShort12LoopsPORun(alphaRelations, trace);
			runs.add(porun);
			
			succ = new HashSet<Integer>(porun.asSuccessorsList().values());
			eventlength.add(succ.size());
				
			runs.add(porun);
		}
		System.out.println("PO runs created");
				
		IOUtils.toFile(logfilename + "_prefix.dot", runs.toDot());
		runs.mergePrefix();
		IOUtils.toFile(logfilename + "_merged.dot", runs.toDot());
		
		PrimeEventStructure<Integer> pes = PORuns2PES.getPrimeEventStructure(runs, logfilename);
		
		return pes;
	}
	
	class PSPgenerator implements Runnable {
		private SinglePORunPESSemantics<Integer> logpessem;
		private PrunedOpenPartialSynchronizedProduct<Integer> psp;
		private PrimeEventStructure<Integer> logpes;
		private NewUnfoldingPESSemantics<Integer> bpmnpes;
		private List<Integer> allSinks;
		
		public PSPgenerator(String logfilename, String logfiletemplate, String bpmnfilename, String bpmnfolder, Boolean initialize) throws Exception {
			this.logpes = getLogPESExample(logfilename, logfiletemplate);
			this.bpmnpes = getUnfoldingPESExample(bpmnfilename, bpmnfolder);
			
			allSinks = new ArrayList<Integer>(logpes.getSinks());
			if (initialize) currentSink.set(allSinks.size() - 1);
		}
		
		@Override
		public void run() {
			int sink;
			int sinkindex;
			double startingTime;
			Boolean finishedSinks = false;
			
			while (!finishedSinks) {
				sinkindex = currentSink.getAndDecrement();
				
				if (sinkindex >= 0) {
					sink = allSinks.get(sinkindex);
	
					startingTime = System.nanoTime();
		        	logpessem = new SinglePORunPESSemantics<Integer>(logpes, sink);			
					psp = new PrunedOpenPartialSynchronizedProduct<Integer>(logpessem, bpmnpes);
					
					if (sinkindex != 684) {
						psp.perform()
							.prune()
						;
						System.out.println(">>> Trace " + sinkindex + ", Sink " + sink + ", Time: " + (System.nanoTime() - startingTime) / 1000000 + ", " + psp.matchings.cost);
					} 
					else {
						System.out.println("Skipped trace" + (sinkindex + 1));
					}
				}
				else {
					finishedSinks = true;
				}
			}		
			
			lock.lock();
			condition.signalAll();
			lock.unlock();
			
			finishedThreadCount.incrementAndGet();
		}
	}
}
