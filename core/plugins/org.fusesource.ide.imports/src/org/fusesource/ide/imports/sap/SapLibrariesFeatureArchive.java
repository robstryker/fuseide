package org.fusesource.ide.imports.sap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class SapLibrariesFeatureArchive extends SAPArchive {

	private static final String FEATURE_LICENSE = "Red Hat, Inc. licenses these features and plugins to you under\n" +
		"certain open source licenses (or aggregations of such licenses), which\n" +
		"in a particular case may include the Eclipse Public License, the GNU\n" +
		"Lesser General Public License, and/or certain other open source\n" +
		"licenses. For precise licensing details, consult the corresponding\n" +
		"source code, or contact Red Hat, Attn: General Counsel,\n" + 
		"100 East Davie St., Raleigh NC 27601 USA.\n";

	private static final String FEATURE_DESCRIPTION = "Provides SAP JCo3 and IDoc3 Libraries for JBoss Fuse SAP Tooling Suite installations.";

	private static final String FEATURE_PROVIDER_NAME = "JBoss by Red Hat";

	private static final String FEATURE_VERSION = "3.0.0";

	private static final String FEATURE_LABEL = "SAP JCo3 and IDoc3 Libraries";

	private static final String FEATURE_ID = "com.sap.conn";

	private static final String FEATURE_XML_ENTRY_NAME = "feature.xml";

	private static final String FEATURE_PROPERTIES_FILE_NAME = "feature.properties";
	private static final String FEATURE_PROPERTIES_FILE = 
			"###############################################################################\n" +
			"# Copyright (c) 2010-2015 Red Hat, Inc. and others.\n" +
			"# All rights reserved. This program and the accompanying materials \n" +
			"# are made available under the terms of the Eclipse Public License v1.0\n" +
			"# which accompanies this distribution, and is available at\n" +
			"# http://www.eclipse.org/legal/epl-v10.html\n" +
			"# \n" +
			"# Contributors:\n" +
			"#     JBoss by Red Hat - Initial implementation.\n" +
			"##############################################################################\n" +
			"# feature.properties\n" +
			"# contains externalized strings for feature.xml\n" +
			"# \"%foo\" in feature.xml corresponds to the key \"foo\" in this file\n" +
			"# java.io.Properties file (ISO 8859-1 with \"\\\" escapes)\n" +
			"# This file should be translated.\n" +
			"\n" +
			"# \"featureName\" property - name of the feature\n" +
			"featureName=SAP Libraries\n" +
			"\n" +
			"# \"providerName\" property - name of the company that provides the feature\n" +
			"providerName=JBoss by Red Hat\n" +
			"\n" +
			"# \"updateSiteName\" property - label for the update site\n" +
			"updateSiteName=SAP Libraries Update Site\n" +
			"\n" +
			"devUpdateSiteName=SAP Libraries Development Update Site\n" +
			"\n" +
			"# \"description\" property - description of the feature\n" +
			"description=Provides SAP JCo3 and IDoc3 Libraries for JBoss Fuse SAP Tooling Suite installations.\n" +
			"copyright=JBoss, Home of Professional Open Source Copyright (c) Red Hat, Inc., and individual contributors as indicated by the @authors tag, 2006-2015. See the copyright.txt in the distribution for a full listing of individual contributors.\n ";

	private static final String LICENSE_FILE_NAME = "license.html";
	private static final String LICENSE_FILE = 
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">\n" +
			"<html>\n" +
			"\n" +
			"<body>\n" +
			"<p>Red Hat, Inc. licenses these features and plugins to you under\n" +
			"certain open source licenses (or aggregations of such licenses), which\n" +
			"in a particular case may include the Eclipse Public License, the GNU\n" +
			"Lesser General Public License, and/or certain other open source\n" +
			"licenses. For precise licensing details, consult the corresponding\n" +
			"source code, or contact Red Hat, Attn: General Counsel,\n" + 
			"100 East Davie St., Raleigh NC 27601 USA.\n" + 
			"</p>\n" +
			"</body>\n" +
			"</html>";

	private JCo3ImportSettings jco3ImportSettings;
	
	private IDoc3ImportSettings idoc3ImportSettings;

	public JCo3ImportSettings getJco3ImportSettings() {
		return jco3ImportSettings;
	}

	public void setJco3ImportSettings(JCo3ImportSettings jco3ImportSettings) {
		this.jco3ImportSettings = jco3ImportSettings;
	}

	public IDoc3ImportSettings getIdoc3ImportSettings() {
		return idoc3ImportSettings;
	}

	public void setIdoc3ImportSettings(IDoc3ImportSettings idoc3ImportSettings) {
		this.idoc3ImportSettings = idoc3ImportSettings;
	}

	public void buildSAPLibrariesFeature()  throws IOException {

		JarOutputStream target = null;
		long lastModified = new Date().getTime();

		try {
			// Create Jar output stream.
			String bundleFilename = ImportUtils.getFeatureBundleFilename();
			target = new JarOutputStream(new FileOutputStream(bundleFilename));

			// Create and populate manifest file.
			byte[] manifest = createBundleManifestFile();
			addJarEntry(target, JarFile.MANIFEST_NAME, manifest, lastModified);
			
			// Create and populate feature.xml
			byte[] featureXml = createFeatureXmlFile();
			addJarEntry(target, FEATURE_XML_ENTRY_NAME, featureXml, lastModified);
			
			// Create and populate feature.properties.
			addJarEntry(target, FEATURE_PROPERTIES_FILE_NAME, FEATURE_PROPERTIES_FILE.getBytes(MANIFEST_ENCODING), lastModified);
			
			// Create and populate license.html.
			addJarEntry(target, LICENSE_FILE_NAME, LICENSE_FILE.getBytes(MANIFEST_ENCODING), lastModified);
				
		} catch (Exception e) {
			
		} finally {
			if (target != null) {
				target.close();
			}
		}
	}
	
	private byte[] createFeatureXmlFile() throws Exception {

		// Define Feature
		Feature feature = new Feature();
		feature.setId(FEATURE_ID);
		feature.setLabel(FEATURE_LABEL);
		feature.setVersion(FEATURE_VERSION);
		feature.setProviderName(FEATURE_PROVIDER_NAME);

		// Add Feature Description
		Feature.Description description = new Feature.Description();
		description.setContent(FEATURE_DESCRIPTION);
		feature.setDescription(description);
		
		// Add Feature Copyright
		Feature.Copyright copyright = new Feature.Copyright();
		copyright.setContent("Copyright 2015 Red Hat, Inc.");
		feature.setCopyright(copyright);
		
		// Add Feature License
		Feature.License license = new Feature.License();
		license.setContent(FEATURE_LICENSE);
		feature.setLicense(license);
		
		// Add Plug-ins to Feature
		//
		List<Feature.Plugin> plugins = new ArrayList<Feature.Plugin>();

		// Add IDoc3 Plug-in to Feature
		Feature.Plugin plugin = new Feature.Plugin();
		plugin.setId(idoc3ImportSettings.getBundleSymbolicName());
		plugin.setVersion(idoc3ImportSettings.getArchiveVersion());
		plugin.setUnpack(false);
		plugins.add(plugin);
		
		// Add JCo3 Plug-in to Feature
		plugin = new Feature.Plugin();
		plugin.setId(jco3ImportSettings.getBundleSymbolicName());
		plugin.setVersion(jco3ImportSettings.getArchiveVersion());
		plugin.setUnpack(false);
		plugins.add(plugin);

		// Add JCo3 Plug-in Fragment to Feature
		plugin = new Feature.Plugin();
		plugin.setId(jco3ImportSettings.getFragmentSymbolicName());
		plugin.setVersion(jco3ImportSettings.getArchiveVersion());
		plugin.setOs(jco3ImportSettings.getJco3Archive().getType().getEclipseOS());
		plugin.setWs(jco3ImportSettings.getJco3Archive().getType().getEclipseWS());
		plugin.setArch(jco3ImportSettings.getJco3Archive().getType().getEclipseArch());
		plugin.setFragment(true);
		plugin.setUnpack(false);
		plugins.add(plugin);
		
		feature.setPlugins(plugins);
		//
		// Add Plug-ins to Feature - End
		
		// Marshal XML Document
		JAXBContext context = JAXBContext.newInstance(Feature.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_ENCODING, MANIFEST_ENCODING);
		StringWriter stringWriter = new StringWriter();
		m.marshal(feature, stringWriter);
		
		return stringWriter.toString().getBytes(MANIFEST_ENCODING);
	}

	private byte[] createBundleManifestFile() throws IOException {
		StringBuilder manifest = new StringBuilder();
		writeAttribute(manifest, Attributes.Name.MANIFEST_VERSION.toString(), MANIFEST_VERSION_VALUE);
		return manifest.toString().getBytes(MANIFEST_ENCODING);
	}
}
