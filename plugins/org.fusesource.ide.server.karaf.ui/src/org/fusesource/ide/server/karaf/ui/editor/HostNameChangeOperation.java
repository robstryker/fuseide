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

package org.fusesource.ide.server.karaf.ui.editor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.fusesource.ide.server.karaf.core.server.IServerConfigurationWorkingCopy;


/**
 * @author lhein
 */
public class HostNameChangeOperation extends AbstractOperation {
	
	private final IServerWorkingCopy copy;
	private final String newHostName;
	private final String oldHostName;
	
	public HostNameChangeOperation(IServerConfigurationWorkingCopy copy, String newHostName, String label) {
		super(label);
		this.copy = (IServerWorkingCopy)copy;
		oldHostName = copy.getPassword();
		this.newHostName = newHostName;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		copy.setHost(newHostName);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		copy.setHost(oldHostName);
		return Status.OK_STATUS;
	}
}
