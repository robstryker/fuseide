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

package org.fusesource.ide.server.fuse.ui.runtime.fuseesb;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.fusesource.ide.server.karaf.core.runtime.IKarafRuntime;
import org.fusesource.ide.server.karaf.ui.runtime.AbstractKarafRuntimeComposite;
import org.fusesource.ide.server.karaf.ui.runtime.KarafWizardDataModel;
import org.fusesource.ide.server.karaf.ui.runtime.v2x.KarafRuntimeWizardFragment2x;


/**
 * @author lhein
 */
public class FuseESBRuntimeWizardFragment6x extends
		KarafRuntimeWizardFragment2x {
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.server.karaf.ui.runtime.AbstractKarafRuntimeWizardFragment#getRuntimeComposite(org.eclipse.swt.widgets.Composite, org.eclipse.wst.server.ui.wizard.IWizardHandle, org.fusesource.ide.server.karaf.ui.runtime.KarafWizardDataModel)
	 */
	@Override
	protected AbstractKarafRuntimeComposite getRuntimeComposite(
			Composite parent, IWizardHandle handle, KarafWizardDataModel model) {
		return new FuseESBRuntimeComposite6x(parent, handle, model);
	}

	/**
	 * updates the model from runtime.
	 */
	protected void populateModel() {
		IRuntimeWorkingCopy workingCopy = getRuntimeWorkingCopy();
		if (workingCopy != null) {
			// workCopy will be instance of ServerDelegate classs.
			// We need to get the params, so IFuseESBRuntime will be enough.
			IKarafRuntime karafRuntime = (IKarafRuntime) workingCopy
					.loadAdapter(IKarafRuntime.class,
							new NullProgressMonitor());
			if (karafRuntime != null) {
				model.setKarafInstallDir(karafRuntime.getLocation().toOSString());
			}
		}
	}
}