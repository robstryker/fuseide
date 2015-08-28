/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.fusesource.ide.foundation.core.xml.namespace;

import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.internal.content.Activator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class FindNamespaceHandlerSupport extends DefaultHandler {

	private final Set<String> namespaces;
	private boolean namespaceFound = false;

	public FindNamespaceHandlerSupport(Set<String> namespaces) {
		this.namespaces = namespaces;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	
		if (!namespaceFound) {
			if (uri != null && namespaces.contains(uri)) {
				namespaceFound = true;
				throw new StopParsingException();
			}
		}
	}

	public boolean isNamespaceFound() {
		return namespaceFound;
	}

	public boolean parseContents(InputSource contents) throws IOException, ParserConfigurationException, SAXException {
		// Parse the file into we have what we need (or an error occurs).
		try {
			SAXParserFactory factory = Activator.getDefault().getFactory();
			if (factory == null)
				return false;
			final SAXParser parser = createParser(factory);
			// to support external entities specified as relative URIs (see bug 63298)
			contents.setSystemId("/"); //$NON-NLS-1$
			parser.parse(contents, this);
		} catch (StopParsingException e) {
			// Abort the parsing normally. Fall through...
		}
		return true;
	}

	private final SAXParser createParser(SAXParserFactory parserFactory) throws ParserConfigurationException, SAXException,
			SAXNotRecognizedException, SAXNotSupportedException {
				parserFactory.setNamespaceAware(true);
				final SAXParser parser = parserFactory.newSAXParser();
				final XMLReader reader = parser.getXMLReader();
				//reader.setProperty("http://xml.org/sax/properties/lexical-handler", this); //$NON-NLS-1$
				// disable DTD validation (bug 63625)
				try {
					//	be sure validation is "off" or the feature to ignore DTD's will not apply
					reader.setFeature("http://xml.org/sax/features/validation", false); //$NON-NLS-1$
					reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$
				} catch (SAXNotRecognizedException e) {
					// not a big deal if the parser does not recognize the features
				} catch (SAXNotSupportedException e) {
					// not a big deal if the parser does not support the features
				}
				return parser;
			}

}