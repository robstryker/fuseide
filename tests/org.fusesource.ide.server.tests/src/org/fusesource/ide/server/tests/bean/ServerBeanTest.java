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

package org.fusesource.ide.server.tests.bean;

import java.util.Collection;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.fusesource.ide.server.karaf.core.bean.KarafBeanProvider;
import org.fusesource.ide.server.tests.FuseServerTestActivator;
import org.fusesource.ide.server.tests.util.MockRuntimeCreationUtil;
import org.fusesource.ide.server.tests.util.ParametizedTestUtil;
import org.jboss.ide.eclipse.as.core.server.bean.ServerBean;
import org.jboss.ide.eclipse.as.core.server.bean.ServerBeanLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ServerBeanTest extends TestCase {

	public static final HashMap<String, String> typeToVersion;
	static {
		typeToVersion = new HashMap<String,String>();
		typeToVersion.put(MockRuntimeCreationUtil.KARAF_20, "2.0.0");
		typeToVersion.put(MockRuntimeCreationUtil.KARAF_21, "2.1.6");
		typeToVersion.put(MockRuntimeCreationUtil.KARAF_22, "2.2.11");
		typeToVersion.put(MockRuntimeCreationUtil.KARAF_23, "2.3.5");
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		return ParametizedTestUtil.asCollection(MockRuntimeCreationUtil.SUPPORTED_RUNTIMES);
	}
	
	private String fRuntimeType;
	public ServerBeanTest(String runtimeType) {
		fRuntimeType = runtimeType;
	}

	
	@Test
	public void testKaraf() throws Exception {
		IPath dest = FuseServerTestActivator.getDefault()
				.getStateLocation().append(fRuntimeType);
		MockRuntimeCreationUtil.createRuntimeMock(
				fRuntimeType, dest);
		ServerBeanLoader l = new ServerBeanLoader(dest.toFile());
		ServerBean b = l.getServerBean();
		assertTrue(b.getBeanType() == KarafBeanProvider.KARAF_2x);
		assertEquals(b.getFullVersion(), typeToVersion.get(fRuntimeType));
		assertEquals(b.getVersion(), ServerBeanLoader.getMajorMinorVersion(typeToVersion.get(fRuntimeType)));
	}
}
