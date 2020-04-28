/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2013 - 2017 Queensland University of Technology.
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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.11.03 at 05:04:23 PM CET 
//

package org.yawlfoundation.yawlschema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for LayoutFactsType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="LayoutFactsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="locale" type="{http://www.yawlfoundation.org/yawlschema}LayoutLocaleType"/>
 *         &lt;element name="specification" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="size" type="{http://www.yawlfoundation.org/yawlschema}LayoutDimensionType"/>
 *                   &lt;element name="net" type="{http://www.yawlfoundation.org/yawlschema}LayoutNetFactsType" maxOccurs="unbounded"/>
 *                   &lt;element name="labelFontSize" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                 &lt;attribute name="defaultBgColor" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LayoutFactsType", propOrder = { "locale", "specification" })
public class LayoutFactsType {

	@XmlElement(required = true)
	protected LayoutLocaleType locale;
	@XmlElement(required = true)
	protected List<LayoutFactsType.Specification> specification;

	/**
	 * Gets the value of the locale property.
	 * 
	 * @return possible object is {@link LayoutLocaleType }
	 * 
	 */
	public LayoutLocaleType getLocale() {
		return locale;
	}

	/**
	 * Sets the value of the locale property.
	 * 
	 * @param value
	 *            allowed object is {@link LayoutLocaleType }
	 * 
	 */
	public void setLocale(LayoutLocaleType value) {
		this.locale = value;
	}

	/**
	 * Gets the value of the specification property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the specification property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSpecification().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link LayoutFactsType.Specification }
	 * 
	 * 
	 */
	public List<LayoutFactsType.Specification> getSpecification() {
		if (specification == null) {
			specification = new ArrayList<LayoutFactsType.Specification>();
		}
		return this.specification;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="size" type="{http://www.yawlfoundation.org/yawlschema}LayoutDimensionType"/>
	 *         &lt;element name="net" type="{http://www.yawlfoundation.org/yawlschema}LayoutNetFactsType" maxOccurs="unbounded"/>
	 *         &lt;element name="labelFontSize" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
	 *       &lt;/sequence>
	 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
	 *       &lt;attribute name="defaultBgColor" type="{http://www.w3.org/2001/XMLSchema}integer" />
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "size", "net", "labelFontSize" })
	public static class Specification {

		@XmlElement(required = true)
		protected LayoutDimensionType size;
		@XmlElement(required = true)
		protected List<LayoutNetFactsType> net;
		protected BigInteger labelFontSize;
		@XmlAttribute(required = true)
		@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
		@XmlSchemaType(name = "NCName")
		protected String id;
		@XmlAttribute
		protected BigInteger defaultBgColor;

		/**
		 * Gets the value of the size property.
		 * 
		 * @return possible object is {@link LayoutDimensionType }
		 * 
		 */
		public LayoutDimensionType getSize() {
			return size;
		}

		/**
		 * Sets the value of the size property.
		 * 
		 * @param value
		 *            allowed object is {@link LayoutDimensionType }
		 * 
		 */
		public void setSize(LayoutDimensionType value) {
			this.size = value;
		}

		/**
		 * Gets the value of the net property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the net property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getNet().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link LayoutNetFactsType }
		 * 
		 * 
		 */
		public List<LayoutNetFactsType> getNet() {
			if (net == null) {
				net = new ArrayList<LayoutNetFactsType>();
			}
			return this.net;
		}

		/**
		 * Gets the value of the labelFontSize property.
		 * 
		 * @return possible object is {@link BigInteger }
		 * 
		 */
		public BigInteger getLabelFontSize() {
			return labelFontSize;
		}

		/**
		 * Sets the value of the labelFontSize property.
		 * 
		 * @param value
		 *            allowed object is {@link BigInteger }
		 * 
		 */
		public void setLabelFontSize(BigInteger value) {
			this.labelFontSize = value;
		}

		/**
		 * Gets the value of the id property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getId() {
			return id;
		}

		/**
		 * Sets the value of the id property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setId(String value) {
			this.id = value;
		}

		/**
		 * Gets the value of the defaultBgColor property.
		 * 
		 * @return possible object is {@link BigInteger }
		 * 
		 */
		public BigInteger getDefaultBgColor() {
			return defaultBgColor;
		}

		/**
		 * Sets the value of the defaultBgColor property.
		 * 
		 * @param value
		 *            allowed object is {@link BigInteger }
		 * 
		 */
		public void setDefaultBgColor(BigInteger value) {
			this.defaultBgColor = value;
		}

	}

}
