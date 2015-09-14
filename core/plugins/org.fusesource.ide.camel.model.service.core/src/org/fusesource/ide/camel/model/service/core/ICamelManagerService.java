/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.fusesource.ide.camel.model.service.core;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.fusesource.ide.camel.model.service.core.catalog.CamelModel;
import org.fusesource.ide.camel.model.service.core.model.CamelFile;

public interface ICamelManagerService {

	public static final String CAMEL_VERSION_PROPERTY = "camel.version"; //$NON-NLS-1$

	/**
	 * creates and returns the camel model for a specific version
	 * 
	 * @return	the camel model
	 */
	CamelModel getCamelModel();
	
	/**
	 * returns the schema provider 
	 * 
	 * @return
	 */
	CamelSchemaProvider getCamelSchemaProvider();

	/**
	 * loads a camel context file from the given resource
	 * 
	 * @param res		the resource
	 * @param monitor	the progress monitor
	 * @return	the camel context file object containing all data
	 * @throws IOException	on load errors
	 */
	CamelFile loadCamelFile(IResource res, IProgressMonitor monitor) throws IOException;
	
	/**
	 * saves the given camel file to the given resource
	 *  
	 * @param camelFile		the camel file
	 * @param res			the resource to save to
	 * @param monitor		the progress monitor
	 * @throws IOException	on errors
	 */
	void saveCamelFile(CamelFile camelFile, IResource res, IProgressMonitor monitor) throws IOException;
}
