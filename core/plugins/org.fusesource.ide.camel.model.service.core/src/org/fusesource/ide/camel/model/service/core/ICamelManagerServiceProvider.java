/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.fusesource.ide.camel.model.service.core;


public interface ICamelManagerServiceProvider {
	/**
	 * Get a management service
	 * @param runtime
	 * @return
	 */
	public ICamelManagerService getManagerService();
	
	/**
	 * Get the service id
	 * @return
	 */
	public String getManagerServiceId();
}
