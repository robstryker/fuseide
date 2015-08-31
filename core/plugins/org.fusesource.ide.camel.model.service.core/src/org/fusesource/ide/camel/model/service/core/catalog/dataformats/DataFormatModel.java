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
package org.fusesource.ide.camel.model.service.core.catalog.dataformats;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.fusesource.ide.camel.model.service.core.catalog.CamelModel;
import org.fusesource.ide.camel.model.service.core.catalog.Dependency;
import org.fusesource.ide.camel.model.service.core.catalog.Parameter;
import org.fusesource.ide.camel.model.service.core.internal.CamelModelServiceCoreActivator;
import org.xml.sax.InputSource;

/**
 * @author lhein
 */
@XmlRootElement(name="dataformats")
public class DataFormatModel {

	private CamelModel model;
	private ArrayList<DataFormat> supportedDataFormats;
	
	/**
	 * @return the model
	 */
	public CamelModel getModel() {
		return this.model;
	}
	
	/**
	 * @param model the model to set
	 */
	public void setModel(CamelModel model) {
		this.model = model;
	}
	
	/**
	 * @return the supportedDataFormats
	 */
	@XmlElement(name = "dataformat")
	public ArrayList<DataFormat> getSupportedDataFormats() {
		return this.supportedDataFormats;
	}
	
	/**
	 * @param supportedDataFormats the supportedDataFormats to set
	 */
	public void setSupportedDataFormats(
			ArrayList<DataFormat> supportedDataFormats) {
		this.supportedDataFormats = supportedDataFormats;
	}
		
	/**
	 * creates the model from the given input stream 
	 * 
	 * @param stream	the stream to parse
	 * @return			the created model instance of null on errors
	 */
	public static DataFormatModel getXMLFactoryInstance(InputStream stream) {
		try {
			// create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance(DataFormatModel.class, DataFormat.class, Dependency.class, Parameter.class);
		    Unmarshaller um = context.createUnmarshaller();
		    DataFormatModel model = (DataFormatModel) um.unmarshal(new InputSource(stream));
		    return model;
		} catch (JAXBException ex) {
			CamelModelServiceCoreActivator.pluginLog().logError(ex);
		}
		return null;
	}
}
