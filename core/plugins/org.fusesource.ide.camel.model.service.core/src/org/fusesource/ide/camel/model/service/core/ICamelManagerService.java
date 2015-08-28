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

import java.net.URL;

public interface ICamelManagerService {

	public static final String CAMEL_VERSION_PROPERTY = "camel.version"; //$NON-NLS-1$

	/**
	 * returns the url to the components model xml file
	 * 
	 * @return
	 */
	URL getComponentModelURL();
	
	/**
	 * returns the url to the eip model xml file
	 * 
	 * @return
	 */
	URL getEipModelURL();

	/**
	 * returns the url to the language model xml file
	 * 
	 * @return
	 */
	URL getLanguageModelURL();

	/**
	 * returns the url to the data formats model xml file
	 * 
	 * @return
	 */
	URL getDataFormatModelURL();
}
