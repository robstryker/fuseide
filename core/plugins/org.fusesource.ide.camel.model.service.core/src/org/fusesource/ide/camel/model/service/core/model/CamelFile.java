/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 

package org.fusesource.ide.camel.model.service.core.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;

/**
 * this object represents the camel xml file. It can be of a schema type
 * like Spring or Blueprint. It can also contain Bean Definitions for things
 * like connection factories, property placeholder beans or loadbalancers etc.
 * 
 * The only children for the camel file is the camel context.
 * 
 * @author lhein
 */
public class CamelFile extends CamelModelElement {
	
	/**
	 * these maps contains endpoints and bean definitions stored using their ID value
	 */
	private Map<String, BeanDef> beanDefinitions = new HashMap<String, BeanDef>();

	/**
	 * the resource the camel file is stored in
	 */
	private IResource resource;
	
	/**
	 * Spring / Blueprint / Routes
	 */
	private CamelSchemaType schemaType;

	/**
	 * 
	 */
	public CamelFile(IResource resource) {
		super(null);
		this.resource = resource;
	}
	
	/**
	 * @return the beanDefinitions
	 */
	public Map<String, BeanDef> getBeanDefinitions() {
		return this.beanDefinitions;
	}
	
	/**
	 * @param beanDefinitions the beanDefinitions to set
	 */
	public void setBeanDefinitions(Map<String, BeanDef> beanDefinitions) {
		this.beanDefinitions = beanDefinitions;
	}
	
	/**
	 * adds the given bean definition to the context 
	 * 
	 * @param def
	 */
	public void addBeanDefinition(BeanDef def) {
		if (this.beanDefinitions.containsKey(def.getId())) return;
		this.beanDefinitions.put(def.getId(), def);
	}
	
	/**
	 * removes the bean definition from context 
	 * 
	 * @param def
	 */
	public void removeBeanDefinition(BeanDef def) {
		this.beanDefinitions.remove(def.getId());
	}
	
	/**
	 * deletes all bean definitions
	 */
	public void clearBeanDefinitions() {
		this.beanDefinitions.clear();
	}
	
	/**
	 * @return the schemaType
	 */
	public CamelSchemaType getSchemaType() {
		return this.schemaType;
	}
	
	/**
	 * @param schemaType the schemaType to set
	 */
	public void setSchemaType(CamelSchemaType schemaType) {
		this.schemaType = schemaType;
	}
	
	/**
	 * @return the resource
	 */
	public IResource getResource() {
		return this.resource;
	}
	
	/**
	 * @param resource the resource to set
	 */
	public void setResource(IResource resource) {
		this.resource = resource;
	}
}
