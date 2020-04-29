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
package ee.ut.bpmn.utils;

import java.io.File;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.jbpt.utils.IOUtils;
import org.junit.Test;

import ee.ut.eventstr.freq.FrequencyAwarePrimeEventStructure;
import ee.ut.mining.log.AlphaRelations;
import ee.ut.mining.log.XLogReader;
import ee.ut.mining.log.poruns.PORun;
import ee.ut.mining.log.poruns.PORuns;
import ee.ut.mining.log.poruns.fpes.PORuns2FPES;
import ee.ut.mining.log.poruns.pes.PORuns2PES;

public class LogReaderTest {
	private String model = "Road_Traffic_Fine_Management_Process";
	private String fileNameTemplate = "logs/%s.xes.gz";

	@Test
	public void entryPoint() throws Exception {
		XLog log = XLogReader.openLog(String.format(fileNameTemplate, model));		
		AlphaRelations alphaRelations = new AlphaRelations(log);
		
		File target = new File("target");
		if (!target.exists())
			target.mkdirs();
		
	    long time = System.nanoTime();
		PORuns runs = new PORuns();

		for (XTrace trace: log) {
			PORun porun = new PORun(alphaRelations, trace);
			runs.add(porun);
		}
		
//		IOUtils.toFile(model + "_prefix.dot", runs.toDot());
		runs.mergePrefix();
//		IOUtils.toFile(model + "_merged.dot", runs.toDot());

		FrequencyAwarePrimeEventStructure<Integer> fpes = PORuns2FPES.getPrimeEventStructure(runs, model);
		
		IOUtils.toFile(model + "_fpes.dot", fpes.toDot());
		
	    System.out.println("Overall time: " + (System.nanoTime() - time) / 1000000000.0);

	}
}
