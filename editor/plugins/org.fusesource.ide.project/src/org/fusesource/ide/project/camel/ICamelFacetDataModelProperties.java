/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.project.camel;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;

public interface ICamelFacetDataModelProperties extends IFacetDataModelProperties {

	/*
	 * Our primary keys for setting sar information in the wizard
	 */
	public static final String CAMEL_CONTENT_FOLDER = "ICamelFacetDataModelProperties.Content_Folder"; //$NON-NLS-1$
	public static final String CAMEL_SOURCE_FOLDER = "ICamelFacetDataModelProperties.Source_Folder"; //$NON-NLS-1$
	public static final String CAMEL_PROJECT_VERSION = "ICamelFacetDataModelProperties.Project.Version"; //$NON-NLS-1$
	public static final String CREATE_BLUEPRINT_DESCRIPTOR = "ICamelFacetDataModelProperties.Project.Descriptor.Blueprint.Create"; //$NON-NLS-1$
	
	/**
	 * A boolean representing whether we have the freedom to completely tear apart and rebuild a project structure
	 */
	public static final String UPDATE_PROJECT_STRUCTURE = "camel.project.structure.update"; //$NON-NLS-1$
	
	
	/* Store settings inside the project .settings folder */
	public static final QualifiedName QNAME_CAMEL_CONTENT_FOLDER = new QualifiedName("camel", CAMEL_CONTENT_FOLDER); //$NON-NLS-1$
	public static final QualifiedName QNAME_CAMEL_SRC_FOLDER = new QualifiedName("camel", CAMEL_SOURCE_FOLDER); //$NON-NLS-1$

	
	
	/*
	 * Other constants
	 */
	public final static String CAMEL_EXTENSION = ".jar";//$NON-NLS-1$
	public final static String CAMEL_PROJECT_FACET = "jst.camel";//$NON-NLS-1$
	public final static String CAMEL_PROJECT_FACET_TEMPLATE = "template.jst.camel.2.15.util";//$NON-NLS-1$
	public final static String OSGI_INF = "OSGI-INF";//$NON-NLS-1$
	
	public final static String DEFAULT_CAMEL_CONFIG_RESOURCE_FOLDER = "camelcontent";//$NON-NLS-1$
	public final static String DEFAULT_CAMEL_SOURCE_FOLDER = "src";//$NON-NLS-1$
	public final static String DEFAULT_CLAASES_OUTPUT_FOLDER = "build/classes"; //$NON-NLS-1$
	
	public final static String CAMEL_PROJECT_NATURE = "org.fusesource.ide.project.camel.nature";//$NON-NLS-1$
}
