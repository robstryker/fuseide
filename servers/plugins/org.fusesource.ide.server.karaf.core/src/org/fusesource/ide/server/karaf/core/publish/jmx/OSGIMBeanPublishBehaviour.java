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
package org.fusesource.ide.server.karaf.core.publish.jmx;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.eclipse.wst.server.core.IServer;
import org.fusesource.ide.server.karaf.core.Activator;
import org.fusesource.ide.server.karaf.core.server.subsystems.OSGiBundleState;

import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;

/**
 * publisher using the osgi.core:type=framework mbean
 * 
 * @author lhein
 */
public class OSGIMBeanPublishBehaviour implements IJMXPublishBehaviour {
	
	private static final String OSGI_FRAMEWORK_MBEAN = "osgi.core:type=framework,*";
	private static final String OSGI_BUNDLESTATE_MBEAN = "osgi.core:type=bundleState,*";
	
	private ObjectName objectNameBundleState;
	private ObjectName objectNameFramework;
	
	@Override
	public long getBundleId(MBeanServerConnection mbsc,
			String bundleSymbolicName, String version) {
		try {
			TabularData tabData = (TabularData)mbsc.invoke(this.objectNameBundleState, "listBundles", null, null);
			final Collection<?> rows = tabData.values();
			for (Object row : rows) {
				if (row instanceof CompositeData) {
					CompositeData cd = (CompositeData) row;
					String bsn = cd.get("SymbolicName").toString();
					String id = cd.get("Identifier").toString();
					String ver = cd.get("Version").toString();
					if (version != null) {
						if (bsn.equals(bundleSymbolicName) && ver.equals(version)) {
							return Long.parseLong(id);
						}	
					} else {
						// if we don't have a version we take the first best
						if (bsn.equals(bundleSymbolicName)) {
							return Long.parseLong(id);
						}
					}
				}
			}
		} catch (Exception ex) {
			Activator.getLogger().error(ex);
		}
		return -1;
	}
	
	@Override
	public long installBundle(MBeanServerConnection mbsc, String bundlePath) {
		String bundleUrl = bundlePath;
		try {
			bundleUrl = new File(bundlePath).toURL().toExternalForm();
		} catch(MalformedURLException murle) {
			murle.printStackTrace();		
		}
		
		try {
			Object retVal = mbsc.invoke(this.objectNameFramework, "installBundle", new Object[] { bundleUrl } , new String[] {String.class.getName() }); 
			if (retVal instanceof Long) {
				long bid = (Long)retVal;
				// also start the bundle
				mbsc.invoke(this.objectNameFramework, "startBundle", new Object[] { bid }, new String[] { "long" });
				return bid;
			} else {
				Activator.getLogger().error(retVal.toString());
			}
		} catch (Exception ex) {
			Activator.getLogger().error(ex);
		}
		return -1;
	}
	
	@Override
	public boolean updateBundle(MBeanServerConnection mbsc, long bundleId,
			String bundlePath) {
		try {
			mbsc.invoke(this.objectNameFramework, "updateBundleFromURL", new Object[] { bundleId, bundlePath } , new String[] {"long", String.class.getName() }); 
			return true;
		} catch (Exception ex) {
			Activator.getLogger().error(ex);
		}
		return false;
	}
	
	@Override
	public boolean uninstallBundle(MBeanServerConnection mbsc, long bundleId) {
		try {
			mbsc.invoke(this.objectNameFramework, "uninstallBundle", new Object[] { bundleId } , new String[] { "long" }); 
			return true;
		} catch (Exception ex) {
			Activator.getLogger().error(ex);
		}
		return false;
	}
	
	@Override
	public int getBundleStatus(MBeanServerConnection mbsc, long bundleId) {
		try {
			TabularData tabData = (TabularData)mbsc.invoke(this.objectNameBundleState, "listBundles", null, null);
			final Collection<?> rows = tabData.values();
			for (Object row : rows) {
				if (row instanceof CompositeData) {
					CompositeData cd = (CompositeData) row;
					String id = cd.get("Identifier").toString();
					String state = cd.get("State").toString();
					long longID = Long.parseLong(id); 
					if (bundleId == longID) {
						return OSGiBundleState.getStatusForString(state);
					}	
				}
			}
		} catch (Exception ex) {
			Activator.getLogger().error(ex);
		}
		return IServer.STATE_UNKNOWN;
	}
	
	@Override
	public boolean canHandle(MBeanServerConnection mbsc) {
		try {
	    	this.objectNameBundleState = new ObjectName(OSGI_BUNDLESTATE_MBEAN);
	    	this.objectNameFramework = new ObjectName(OSGI_FRAMEWORK_MBEAN);
	    	
	    	Set mbeans = mbsc.queryMBeans(this.objectNameBundleState, null);
	    	if (mbeans.size() != 1) {
		    	// no bundleState mbean found - can't handle the jmx connection
		    	return false;
		    } else {
		    	// remember the mbean
		    	Object oMbean = mbeans.iterator().next();
		    	if (oMbean instanceof ObjectInstance) {
		    		ObjectInstance oi = (ObjectInstance)oMbean;
		    		this.objectNameBundleState = oi.getObjectName();
		    	}
		    }
	    	mbeans = mbsc.queryMBeans(this.objectNameFramework, null); 	    
		    if (mbeans.size() != 1) {
		    	// no framework mbean found - can't handle the jmx connection
		    	return false;
		    } else {
		    	// remember the mbean
		    	Object oMbean = mbeans.iterator().next();
		    	if (oMbean instanceof ObjectInstance) {
		    		ObjectInstance oi = (ObjectInstance)oMbean;
		    		this.objectNameFramework = oi.getObjectName();
		    	}
		    }
	    	return true;
		} catch (Exception ex) {
			Activator.getLogger().error(ex);
		}
	    return false;
	}
}
