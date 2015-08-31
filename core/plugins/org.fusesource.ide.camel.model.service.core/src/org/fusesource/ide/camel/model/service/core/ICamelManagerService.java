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

import org.fusesource.ide.camel.model.service.core.catalog.CamelModel;

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
}
