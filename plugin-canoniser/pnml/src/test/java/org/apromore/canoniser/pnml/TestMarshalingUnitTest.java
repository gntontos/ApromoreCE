/*-
 * #%L
 * This file is part of "Apromore Community".
 * 
 * Copyright (C) 2012 - 2017 Queensland University of Technology.
 * Copyright (C) 2012 Felix Mannhardt.
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

package org.apromore.canoniser.pnml;

import org.apromore.anf.AnnotationsType;
import org.apromore.canoniser.pnml.internal.PNML2Canonical;
import org.apromore.canoniser.pnml.internal.pnml2canonical.NamespaceFilter;
import org.apromore.cpf.CanonicalProcessType;
import org.apromore.pnml.PnmlType;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Testing the Marshaling of the PNML format.
 */
@Ignore
public class TestMarshalingUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMarshalingUnitTest.class.getName());

    @Test
    @SuppressWarnings("unchecked")
    public void testMarshaling() throws Exception {
        ByteArrayOutputStream anf = new ByteArrayOutputStream();
        ByteArrayOutputStream cpf = new ByteArrayOutputStream();

        JAXBContext jc = JAXBContext.newInstance("org.apromore.pnml");
        Unmarshaller u = jc.createUnmarshaller();

        XMLReader reader = XMLReaderFactory.createXMLReader();
        NamespaceFilter inFilter = new NamespaceFilter("pnml.apromore.org", true);
        inFilter.setParent(reader);
        InputSource is = new InputSource(new FileInputStream(new File("src/test/resources/PNML_models/woped_cases_original_pnml/01_Ballgame.pnml")));
        SAXSource source = new SAXSource(inFilter, is);

        JAXBElement<PnmlType> pnml = (JAXBElement<PnmlType>) u.unmarshal(source);
        PnmlType pkg = pnml.getValue();
        PNML2Canonical pnml2canonical = new PNML2Canonical(pkg, false, false);

        jc = JAXBContext.newInstance("org.apromore.anf");
        Marshaller m_anf = jc.createMarshaller();
        m_anf.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        JAXBElement<AnnotationsType> cproc_anf = new org.apromore.anf.ObjectFactory().createAnnotations(pnml2canonical.getANF());
        m_anf.marshal(cproc_anf, anf);

        jc = JAXBContext.newInstance("org.apromore.cpf");
        Marshaller m_cpf = jc.createMarshaller();
        m_cpf.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        JAXBElement<CanonicalProcessType> cproc_cpf = new org.apromore.cpf.ObjectFactory().createCanonicalProcess(pnml2canonical.getCPF());
        m_cpf.marshal(cproc_cpf, cpf);
    }

    @Test
    public void validateXML() throws Exception {
        File xmlFile = new File("src/test/resources/PNML_models/woped_cases_original_pnml/01_Ballgame.pnml");
        File xmlSchema = new File("../../Apromore-Schema/pnml-schema/src/main/xsd/pnml.xsd");
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaSource = new StreamSource(xmlSchema);
            Schema schema = schemaFactory.newSchema(schemaSource);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setSchema(schema);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);
        } catch (SAXParseException spe) {
            // error generated by the parser
            String message = "** Parser error! **\n  URI: " + spe.getSystemId() + "\n  Line: " + spe.getLineNumber() + "\n  Message: " + spe.getMessage();
            Exception x = spe;
            if (spe.getException() != null) {
                x = spe.getException();
            }
            throw new Exception(message, x);
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            throw new Exception("Error during parsing.", x);
        } catch (ParserConfigurationException pce) {
            throw new Exception("Parser with specified options can't be built.", pce);
        } catch (IOException ioe) {
            throw new Exception("Error reading file.", ioe);
        }
        LOGGER.debug("If nothing else occured: Success!");
    }

}
