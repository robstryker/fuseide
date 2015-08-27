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

public interface ICamelManagerService {

	public static final String CAMEL_VERSION_PROPERTY = "camel.version"; //$NON-NLS-1$

	public static final String CAMEL_VERSION_2_15_1 = "2.15.1"; //$NON-NLS-1$
	public static final String CAMEL_VERSION_2_15_2 = "2.15.2"; //$NON-NLS-1$
	public static final String CAMEL_VERSION_2_15_2_RHT = "2.15.2-rht"; //$NON-NLS-1$
	

	/**
	 * This is a list of all service versions that have been publicly
	 * exposed as API at one time or another, and which must resolve
	 * to a functional service object for the future. 
	 * 
	 * This list is primarily exposed only for the unit tests. 
	 */
	public static final String[] ALL_SERVICE_VERSIONS = new String[]{
			CAMEL_VERSION_2_15_1,
			CAMEL_VERSION_2_15_2,
			CAMEL_VERSION_2_15_2_RHT
	};
	
	
	// TODO add some methods
	public void doSomething();
}
