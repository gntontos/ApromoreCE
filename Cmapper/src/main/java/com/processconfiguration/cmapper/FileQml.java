/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2014 - 2017 Queensland University of Technology.
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

package com.processconfiguration.cmapper;

// Java 2 Standard Edition classes
import java.io.File;
import java.net.URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

// Local classes
import com.processconfiguration.qml.QMLType;

/**
 * QML questionnaire stored on the local filesystem.
 */
public class FileQml implements Qml {

    private QMLType qml;
    private URI     uri;

    /**
     * Sole constructor.
     */
    public FileQml(File file) throws Exception {
        this.uri = file.toURI();

        JAXBContext jc = JAXBContext.newInstance("com.processconfiguration.qml");
        Unmarshaller u = jc.createUnmarshaller();
        JAXBElement qmlElement = (JAXBElement) u.unmarshal(file);
        this.qml = (QMLType) qmlElement.getValue();
    }

    /**
     * {@inheritDoc}
     */
    public QMLType getQml() throws Exception {
        return qml;
    }

    /**
     * {@inheritDoc}
     */
    public URI getURI() {
        return uri;
    }
}

