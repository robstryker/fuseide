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
import java.util.List;
import java.util.Map;

import org.fusesource.ide.camel.model.service.core.catalog.ICamelCatalogElement;

/**
 * @author lhein
 */
public class CamelModelElement {
	
	// children is a list of objects which are no route outputs
	private List<CamelModelElement> childElements;
	
	// input is the element which comes before this one
	private CamelModelElement inputElement;
		
	// output is the route output of this element
	private CamelModelElement outputElement;

	// the catalog element which represents this object
	private ICamelCatalogElement underlyingElement;
	
	// a map containing all the properties of the element
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	/**
	 * 
	 */
	public CamelModelElement(ICamelCatalogElement underlyingElement) {
		this.underlyingElement = underlyingElement;
	}
	
	/**
	 * @return the inputElement
	 */
	public CamelModelElement getInputElement() {
		return this.inputElement;
	}
	
	/**
	 * @param inputElement the inputElement to set
	 */
	public void setInputElement(CamelModelElement inputElement) {
		this.inputElement = inputElement;
	}
	
	/**
	 * @return the outputElement
	 */
	public CamelModelElement getOutputElement() {
		return this.outputElement;
	}
	
	/**
	 * @param outputElement the outputElement to set
	 */
	public void setOutputElement(CamelModelElement outputElement) {
		this.outputElement = outputElement;
	}
	
	/**
	 * @return the childElements
	 */
	public List<CamelModelElement> getChildElements() {
		return this.childElements;
	}
	
	/**
	 * @param childElements the childElements to set
	 */
	public void setChildElements(List<CamelModelElement> childElements) {
		this.childElements = childElements;
	}
	
	/**
	 * deletes all child elements
	 */
	public void clearChildElements() {
		this.childElements.clear();
	}
	
	/**
	 * adds a child element to this element if not already existing
	 * 
	 * @param element
	 */
	public void addChildElement(CamelModelElement element) {
		if (this.childElements.contains(element) == false) {
			this.childElements.add(element);
		}			
	}
	
	/**
	 * removes a child element
	 * 
	 * @param element
	 */
	public void removeChildElement(CamelModelElement element) {
		childElements.remove(element);
	}
	
	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return this.parameters;
	}
	
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * removes the parameter
	 * 
	 * @param name
	 */
	public void removeParameter(String name) {
		this.parameters.remove(name);
	}
	
	/**
	 * returns the parameter with the given name or null if not available
	 * 
	 * @param name	the parameter name
	 * @return	the parameter or null if not available
	 */
	public Object getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * sets the parameter with the given name to the given value. If the 
	 * parameter doesn't exist it will be created
	 * 
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, Object value) {
		this.parameters.put(name, value);
	}
	
	/**
	 * deletes all parameters
	 */
	public void clearParameters() {
		this.parameters.clear();
	}
	
	/**
	 * @return the underlyingElement
	 */
	public ICamelCatalogElement getUnderlyingElement() {
		return this.underlyingElement;
	}
	
	/**
	 * @param underlyingElement the underlyingElement to set
	 */
	public void setUnderlyingElement(ICamelCatalogElement underlyingElement) {
		this.underlyingElement = underlyingElement;
	}
}
