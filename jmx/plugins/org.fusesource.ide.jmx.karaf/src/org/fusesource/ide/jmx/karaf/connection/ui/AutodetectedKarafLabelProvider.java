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

package org.fusesource.ide.jmx.karaf.connection.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.fusesource.ide.jmx.karaf.KarafJMXPlugin;
import org.fusesource.ide.jmx.karaf.KarafJMXSharedImages;
import org.jboss.tools.jmx.jvmmonitor.core.IActiveJvm;
import org.jboss.tools.jmx.local.ProcessInformationStore;
import org.jboss.tools.jmx.local.ui.JVMLabelProviderDelegate;

public class AutodetectedKarafLabelProvider implements JVMLabelProviderDelegate {
	private static final String KARAF_HOME_PREFIX = " -Dkaraf.home=";
	private static final String KARAF_HOME_POSTFIX = " ";
	
	private static final String KARAF_TYPE_KARAF 	= "Apache Karaf";
	private static final String KARAF_TYPE_FUSE  	= "JBoss Fuse";
	private static final String KARAF_TYPE_FABRIC8 	= "Fabric8";
	private static final String KARAF_TYPE_AMQ		= "JBoss A-MQ";
	private static final String KARAF_TYPE_SMX		= "Apache ServiceMix";
	
	protected static final Map<String,String> karafSubTypeMap = new HashMap<String, String>();
	static {
		karafSubTypeMap.put("default", KARAF_TYPE_KARAF);
		karafSubTypeMap.put("esb-version.jar", KARAF_TYPE_FUSE);
		karafSubTypeMap.put("fabric-version.jar", KARAF_TYPE_FABRIC8);
		karafSubTypeMap.put("mq-version.jar", KARAF_TYPE_AMQ);
		karafSubTypeMap.put("servicemix-version.jar", KARAF_TYPE_SMX);
	}
	
	public boolean accepts(IActiveJvm jvm) {
		return isKaraf(jvm);
	}
	public Image getImage(IActiveJvm jvm) {
		String karafHomeFolder = getKarafHomeFolder(jvm);
		String karafSubType = getKarafSubtype(karafHomeFolder);
		Image i = null;
		if (karafSubType != null) {
			if (karafSubType.equalsIgnoreCase(KARAF_TYPE_FUSE)) {
				i = KarafJMXPlugin.getDefault().getSharedImages().image(KarafJMXSharedImages.FUSE_PNG);
			} else if (karafSubType.equalsIgnoreCase(KARAF_TYPE_FABRIC8)) {
				i = KarafJMXPlugin.getDefault().getSharedImages().image(KarafJMXSharedImages.FABRIC_PNG);
			} else if (karafSubType.equalsIgnoreCase(KARAF_TYPE_AMQ)) {
				i = KarafJMXPlugin.getDefault().getSharedImages().image(KarafJMXSharedImages.MQ_PNG);
			} else if (karafSubType.equalsIgnoreCase(KARAF_TYPE_SMX)) {
				i = KarafJMXPlugin.getDefault().getSharedImages().image(KarafJMXSharedImages.SMX_PNG);
			} else if (karafSubType.equalsIgnoreCase(KARAF_TYPE_KARAF)) {
				i = KarafJMXPlugin.getDefault().getSharedImages().image(KarafJMXSharedImages.KARAF_PNG);
			} else {
				i = KarafJMXPlugin.getDefault().getSharedImages().image(KarafJMXSharedImages.CONTAINER_PNG);
			}
		}
		return i;
	}
	
	public String getDisplayString(IActiveJvm jvm) {
		String karafHomeFolder = getKarafHomeFolder(jvm);
		String karafSubType = getKarafSubtype(karafHomeFolder);
		String displayName = jvm.getMainClass();
		if (karafSubType == null) {
			return null;
		} else {
			displayName = karafSubType;
		}
		return displayName;
	}
	
	/**
	 * Returns true if this process is a karaf container 
	 */
	static boolean isKaraf(IActiveJvm jvm) {
		String displayName = jvm.getMainClass();
		return equal("org.apache.karaf.main.Main", displayName);
	}

	private static boolean equal(Object a, Object b) {
		if (a == b) {
			return true;
		}
		return a != null && a.equals(b);
	}
	static String getKarafHomeFolder(IActiveJvm jvm) {
		String karafHomeFolder = null;
		if (!jvm.isRemote()) {
			String pInfo = ProcessInformationStore.getDefault().queryProcessInformation(jvm.getPid());
			if (pInfo != null) {
				int start = pInfo.indexOf(KARAF_HOME_PREFIX);
				if (start != -1) {
					int end   = pInfo.indexOf(KARAF_HOME_POSTFIX, start+KARAF_HOME_PREFIX.length()+1);
					if (end != -1) {
						karafHomeFolder = pInfo.substring(start + KARAF_HOME_PREFIX.length(), end);
					}
				}
			}
		}
		return karafHomeFolder;
	}

	static String getKarafSubtype(String karafHomeFolder) {
		String karafSubType = null;
		if (karafHomeFolder != null) {
			File libFolder = new File(String.format("%s%slib%s", karafHomeFolder, File.separator, File.separator));
			if (libFolder.exists() && libFolder.isDirectory()) {
				File[] jars = libFolder.listFiles(new FileFilter() {
					public boolean accept(File f) {
						return f.isFile() && (f.getName().toLowerCase().endsWith("-version.jar"));
					}
				});
				if (jars != null && jars.length==1) {
					File f = jars[0];
					if (karafSubTypeMap.containsKey(f.getName())) {
						karafSubType = karafSubTypeMap.get(f.getName());
					} 
				} else {
					karafSubType = karafSubTypeMap.get("default");
				}
			}
		}
		return karafSubType;
	}
}