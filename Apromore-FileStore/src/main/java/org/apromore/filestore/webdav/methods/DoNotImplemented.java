/*-
 * #%L
 * This file is part of "Apromore Community".
 * 
 * Copyright (C) 2014 - 2017 Queensland University of Technology.
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

package org.apromore.filestore.webdav.methods;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apromore.filestore.webdav.IMethodExecutor;
import org.apromore.filestore.webdav.ITransaction;
import org.apromore.filestore.webdav.WebDavStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoNotImplemented implements IMethodExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoNotImplemented.class.getName());
    private boolean _readOnly;


    public DoNotImplemented(boolean readOnly) {
        _readOnly = readOnly;
    }

    public void execute(ITransaction transaction, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOGGER.trace("-- " + req.getMethod());

        if (_readOnly) {
            resp.sendError(WebDavStatus.SC_FORBIDDEN);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

}
