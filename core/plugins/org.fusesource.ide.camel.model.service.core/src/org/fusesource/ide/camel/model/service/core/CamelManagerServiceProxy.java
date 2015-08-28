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
import java.text.MessageFormat;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author André Dietisheim
 */
public class CamelManagerServiceProxy extends ServiceTracker<ICamelManagerService, ICamelManagerService>
		implements ICamelManagerService {

	private String serviceVersion;
	public CamelManagerServiceProxy(BundleContext context, String serviceVersion) throws InvalidSyntaxException {
		super(
				context,
				context.createFilter(MessageFormat
						.format("(&(objectClass={0})(camel.version={1}))", ICamelManagerService.class.getCanonicalName(), serviceVersion)), null); //$NON-NLS-1$
		this.serviceVersion = serviceVersion;
		open();
	}
	
	public String getServiceVersion() {
		return serviceVersion;
	}

    private ICamelManagerService checkedGetService() throws CamelManagerException {
    	ICamelManagerService service = getService();
		if (service == null) {
			throw new CamelManagerException("Could not acquire Camel Management service"); //$NON-NLS-1$
		}
		return service;
	}

    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getComponentModelURL()
     */
    @Override
    public URL getComponentModelURL() {
    	return checkedGetService().getComponentModelURL();
    }
    
    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getDataFormatModelURL()
     */
    @Override
    public URL getDataFormatModelURL() {
    	return checkedGetService().getDataFormatModelURL();
    }
    
    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getEipModelURL()
     */
    @Override
    public URL getEipModelURL() {
    	return checkedGetService().getEipModelURL();
    }
    
    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.model.service.core.ICamelManagerService#getLanguageModelURL()
     */
    @Override
    public URL getLanguageModelURL() {
    	return checkedGetService().getLanguageModelURL();
    }
}
