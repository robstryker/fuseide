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

/**
 * this object represents the camel context. It can contains various endpoint 
 * definitions and the only children are routes.
 * 
 * @author lhein
 */
public class CamelContextElement extends CamelModelElement {
	
	/**
	 * contains endpoint definitions stored using their ID value
	 */
	private Map<String, EndpointDef> endpointDefinitions = new HashMap<String, EndpointDef>();
	
	/**
	 * contains the dataformat definitions stored using their ID value
	 */
	private Map<String, CamelModelElement> dataformats = new HashMap<String, CamelModelElement>();
	
	/**
	 * 
	 */
	public CamelContextElement(CamelFile camelFile) {
		super(null);
	}
	
	/**
	 * @return the endpointDefinitions
	 */
	public Map<String, EndpointDef> getEndpointDefinitions() {
		return this.endpointDefinitions;
	}
	
	/**
	 * @param endpointDefinitions the endpointDefinitions to set
	 */
	public void setEndpointDefinitions(Map<String, EndpointDef> endpointDefinitions) {
		this.endpointDefinitions = endpointDefinitions;
	}

	/**
	 * adds an endpoint definition
	 * 
	 * @param def
	 */
	public void addEndpointDefinition(EndpointDef def) {
		if (this.endpointDefinitions.containsKey(def.getId())) return;
		this.endpointDefinitions.put(def.getId(), def);
	}
	
	/**
	 * removes the endpoint from the context
	 * 
	 * @param def
	 */
	public void removeEndpointDefinition(EndpointDef def) {
		this.endpointDefinitions.remove(def.getId());
	}
	
	/**
	 * deletes all endpoint definitions
	 */
	public void clearEndpointDefinitions() {
		this.endpointDefinitions.clear();
	}
	
	/**
	 * @return the dataformats
	 */
	public Map<String, CamelModelElement> getDataformats() {
		return this.dataformats;
	}
	
	/**
	 * @param dataformats the dataformats to set
	 */
	public void setDataformats(Map<String, CamelModelElement> dataformats) {
		this.dataformats = dataformats;
	}
	
	/**
	 * adds a dataformat to the context
	 * 
	 * @param df
	 */
	public void addDataFormat(CamelModelElement df) {
		if (this.dataformats.containsKey(df.getParameter("id"))) return;
		this.dataformats.put((String)df.getParameter("id"), df);
	}
	
	/**
	 * removes the dataformat from the context
	 * 
	 * @param df
	 */
	public void removeDataFormat(CamelModelElement df) {
		this.dataformats.remove(df.getParameter("id"));
	}
	
	/**
	 * deletes all data formats
	 */
	public void clearDataFormats() {
		this.dataformats.clear();
	}
}
