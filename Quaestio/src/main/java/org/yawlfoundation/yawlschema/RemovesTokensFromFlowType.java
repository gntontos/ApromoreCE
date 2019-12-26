/*
 * Copyright © 2009-2018 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.03 at 05:04:23 PM CET 
//

package org.yawlfoundation.yawlschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RemovesTokensFromFlowType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RemovesTokensFromFlowType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flowSource" type="{http://www.yawlfoundation.org/yawlschema}ExternalNetElementType"/>
 *         &lt;element name="flowDestination" type="{http://www.yawlfoundation.org/yawlschema}ExternalNetElementType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemovesTokensFromFlowType", propOrder = { "flowSource",
		"flowDestination" })
public class RemovesTokensFromFlowType {

	@XmlElement(required = true)
	protected ExternalNetElementType flowSource;
	@XmlElement(required = true)
	protected ExternalNetElementType flowDestination;

	/**
	 * Gets the value of the flowSource property.
	 * 
	 * @return possible object is {@link ExternalNetElementType }
	 * 
	 */
	public ExternalNetElementType getFlowSource() {
		return flowSource;
	}

	/**
	 * Sets the value of the flowSource property.
	 * 
	 * @param value
	 *            allowed object is {@link ExternalNetElementType }
	 * 
	 */
	public void setFlowSource(ExternalNetElementType value) {
		this.flowSource = value;
	}

	/**
	 * Gets the value of the flowDestination property.
	 * 
	 * @return possible object is {@link ExternalNetElementType }
	 * 
	 */
	public ExternalNetElementType getFlowDestination() {
		return flowDestination;
	}

	/**
	 * Sets the value of the flowDestination property.
	 * 
	 * @param value
	 *            allowed object is {@link ExternalNetElementType }
	 * 
	 */
	public void setFlowDestination(ExternalNetElementType value) {
		this.flowDestination = value;
	}

}
