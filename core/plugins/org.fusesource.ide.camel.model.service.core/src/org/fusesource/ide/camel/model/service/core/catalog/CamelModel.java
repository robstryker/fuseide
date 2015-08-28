/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.fusesource.ide.camel.model.service.core.catalog;

import org.fusesource.ide.camel.model.service.core.CamelServiceManagerUtil;
import org.fusesource.ide.camel.model.service.core.catalog.components.ComponentModel;
import org.fusesource.ide.camel.model.service.core.catalog.dataformats.DataFormatModel;
import org.fusesource.ide.camel.model.service.core.catalog.eips.EipModel;
import org.fusesource.ide.camel.model.service.core.catalog.languages.LanguageModel;
import org.fusesource.ide.camel.model.service.core.internal.CamelModelServiceCoreActivator;

/**
 * @author lhein
 */
public class CamelModel {
	
	private String camelVersion;
	
	private ComponentModel componentModel;
	private DataFormatModel dataformatModel;
	private EipModel eipModel;
	private LanguageModel languageModel;
	
	/**
	 * creates a model skeleton for the given camel version
	 * the initializing of the model will happen lazy on access
	 * 
	 * @param camelVersion	the camel version of the model
	 */
	public CamelModel(String camelVersion) {
		this.camelVersion = camelVersion;
	}
	
	/**
	 * @return the camelVersion
	 */
	public String getCamelVersion() {
		return this.camelVersion;
	}
	
	/**
	 * @return the componentModel
	 */
	public synchronized ComponentModel getComponentModel() {
		if (this.componentModel == null) {
			try {
				this.componentModel = ComponentModel.getFactoryInstance(CamelServiceManagerUtil.getManagerService(camelVersion).getComponentModelURL().openStream(), this);
			} catch (Exception ex) {
				CamelModelServiceCoreActivator.pluginLog().logError(ex);
			}
		}
		return this.componentModel;
	}
	
	/**
	 * @return the dataformatModel
	 */
	public synchronized DataFormatModel getDataformatModel() {
		if (this.dataformatModel == null) {
			try {
				this.dataformatModel = DataFormatModel.getFactoryInstance(CamelServiceManagerUtil.getManagerService(camelVersion).getDataFormatModelURL().openStream(), this);
			} catch (Exception ex) {
				CamelModelServiceCoreActivator.pluginLog().logError(ex);
			}
		}
		return this.dataformatModel;
	}
	
	/**
	 * @return the eipModel
	 */
	public synchronized EipModel getEipModel() {
		if (this.eipModel == null) {
			try {
				this.eipModel = EipModel.getFactoryInstance(CamelServiceManagerUtil.getManagerService(camelVersion).getEipModelURL().openStream(), this);
			} catch (Exception ex) {
				CamelModelServiceCoreActivator.pluginLog().logError(ex);
			}
		}
		return this.eipModel;
	}
	
	/**
	 * @return the languageModel
	 */
	public synchronized LanguageModel getLanguageModel() {
		if (this.languageModel == null){
			try {
				this.languageModel = LanguageModel.getFactoryInstance(CamelServiceManagerUtil.getManagerService(camelVersion).getLanguageModelURL().openStream(), this);
			} catch (Exception ex) {
				CamelModelServiceCoreActivator.pluginLog().logError(ex);
			}
		}
		return this.languageModel;
	}
}
