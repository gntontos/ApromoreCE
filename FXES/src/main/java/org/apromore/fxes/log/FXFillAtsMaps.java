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
package org.apromore.fxes.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Alireza Ostovar (Alirezaostovar@gmail.com)
 *
 */
public class FXFillAtsMaps extends DefaultHandler {
	
	private Map<String, Integer> trAttrsIndM = new HashMap<String, Integer>(), 
			evAttrsIndM = new HashMap<String, Integer>();
	private Map<String, FXAttributeType> trAttrsTypeM = new HashMap<String, FXAttributeType>(), 
			evAttrsTypeM = new HashMap<String, FXAttributeType>();
	private Stack<FXTagType> stack;
	
	
	public FXFillAtsMaps() 
	{
		stack = new Stack<FXTagType>();
	}

	@Override
	public void startElement(String uri, String lN, String qN,
			Attributes attrs) throws SAXException {
		String tgNme = lN.trim();
		if (tgNme.length() == 0) {
			tgNme = qN;
		}
		
		if (tgNme.equalsIgnoreCase("string")
				|| tgNme.equalsIgnoreCase("date")
				|| tgNme.equalsIgnoreCase("int")
				|| tgNme.equalsIgnoreCase("float")
				|| tgNme.equalsIgnoreCase("boolean")) {

			
			String k = attrs.getValue("key");			
			if (k == null || k.isEmpty()) {
				return;
			}
			String v = attrs.getValue("value");
			if (v == null || v.isEmpty()) {
				return;
			}
			
							
			FXTagType pk = stack.peek();
			
			if(pk == FXTagType.event)
			{
				if(evAttrsIndM.get(k) == null)
				{
					evAttrsIndM.put(k, evAttrsIndM.size());
					evAttrsTypeM.put(k, FXAttributeType.getAttributeType(tgNme));
				}			
			}else if(pk == FXTagType.trace)
			{
				if(trAttrsIndM.get(k) == null)
				{
					trAttrsIndM.put(k, trAttrsIndM.size());
					trAttrsTypeM.put(k, FXAttributeType.getAttributeType(tgNme));
				}	
			} else if (tgNme.equalsIgnoreCase("log")) {
				stack.push(FXTagType.log);
				
			}  else if (tgNme.equalsIgnoreCase("global")) {
				
				String scope = attrs.getValue("scope");
				if(scope != null && scope.equalsIgnoreCase("trace"))
				{
					stack.push(FXTagType.glt);
				}else
					stack.push(FXTagType.gle);
				
			} 
		
		} else if (tgNme.equalsIgnoreCase("event")) {	
			
			stack.push(FXTagType.event);
			
		} else if (tgNme.equalsIgnoreCase("trace")) {
			stack.push(FXTagType.trace);
			
		} else if (tgNme.equalsIgnoreCase("log")) {
			stack.push(FXTagType.log);
			
		}  else if (tgNme.equalsIgnoreCase("global")) {
			
			String scope = attrs.getValue("scope");
			if(scope != null && scope.equalsIgnoreCase("trace"))
			{
				stack.push(FXTagType.glt);
			}else
				stack.push(FXTagType.gle);
			
		} 
	}
	
	@Override
	public void endElement(String uri, String lN, String qN)
			throws SAXException {
		String tgNme = lN.trim();
		if (tgNme.length() == 0) {
			tgNme = qN;
		}
		
		if (tgNme.equalsIgnoreCase("event")) {
			stack.pop();			
		} else if (tgNme.equalsIgnoreCase("trace")) {
			stack.pop();
		} else if (tgNme.equalsIgnoreCase("log")) {
			stack.pop();				
		} else if (tgNme.equalsIgnoreCase("global")) {
			stack.pop();
		}
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		
	}

	Map<String, Integer> gettrAttrsIndM() {
		return trAttrsIndM;
	}

	Map<String, Integer> getevAttrsIndM() {
		return evAttrsIndM;
	}

	Map<String, FXAttributeType> gettrAttrsTypeM() {
		return trAttrsTypeM;
	}

	Map<String, FXAttributeType> getevAttrsTypeM() {
		return evAttrsTypeM;
	}
	
	
	
}