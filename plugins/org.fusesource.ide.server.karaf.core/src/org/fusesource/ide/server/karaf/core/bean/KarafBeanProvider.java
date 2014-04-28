/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.server.karaf.core.bean;

import org.jboss.ide.eclipse.as.core.server.bean.IServerBeanTypeProvider;
import org.jboss.ide.eclipse.as.core.server.bean.ServerBeanType;

public class KarafBeanProvider implements IServerBeanTypeProvider {

	public static final ServerBeanTypeKaraf2x KARAF_2x = new ServerBeanTypeKaraf2x();

	public KarafBeanProvider() {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.ide.eclipse.as.core.server.bean.IServerBeanTypeProvider#
	 * getServerBeanTypes()
	 */
	@Override
	public ServerBeanType[] getServerBeanTypes() {
		return new ServerBeanType[] { KARAF_2x };
	}

}
