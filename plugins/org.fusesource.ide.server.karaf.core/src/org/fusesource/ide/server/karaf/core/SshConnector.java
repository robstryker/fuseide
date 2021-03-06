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

package org.fusesource.ide.server.karaf.core;

import org.eclipse.ui.IViewPart;
import org.eclipse.wst.server.core.IServer;
import org.fusesource.ide.server.karaf.core.server.KarafServerBehaviourDelegate;
import org.fusesource.ide.server.view.ITerminalConnectionListener;
import org.fusesource.ide.server.view.SshView;


/**
 * Thread used to ping server to test when it is started.
 */
public class SshConnector implements ITerminalConnectionListener {

	private int port;
	private String host;
	private String userName;
	private String passwd;
	private IServer server;
	private KarafServerBehaviourDelegate behaviorDelegate;
	private static final String TERMINAL_VIEW_LABEL = Messages.shellViewLabel;

	/**
	 * creates/establishes a SSH connection with the provided connection data
	 * 
	 * @param server				the server object
	 * @param behaviorDelegate		the behaviour delegate
	 * @param host					the host name
	 * @param port					the port number
	 * @param user					the user name
	 * @param pass					the user password
	 */
	public SshConnector(IServer server, KarafServerBehaviourDelegate behaviorDelegate, String host, int port, String user, String pass) {
		super();
		this.server = server;
		this.behaviorDelegate = behaviorDelegate;
		this.host = host;
		this.port = port;
		this.userName = user;
		this.passwd = pass;
	}
	
	/**
	 * starts the ssh connection
	 */
	public void start() {
		// open the terminal view
		IViewPart vp = Activator.openTerminalView();
		if (vp == null || vp instanceof SshView == false) {
			Activator.getLogger().error("Unable to open the terminal view!");
			return;
		}
		
		// get the view
		final SshView connectorView = (SshView)vp;
		
		connectorView.setPartName(server.getName());
		
		// add a connection listener
		connectorView.addConnectionListener(this);
		
		// we do wait a moment to avoid those ugly error messages in the shell view
		try {
			Thread.sleep(7000);
		} catch (InterruptedException ie) {
			
		}
		
		// create the connection
		try {
			connectorView.createConnectionIfNotExists(host, port, userName, passwd);
		} catch (Exception ex) {
			Activator.getLogger().error("Unable to connect via SSH", ex);
		}
	}

	/* (non-Javadoc)
	 * @see org.fusesource.ide.server.view.ITerminalConnectionListener#onConnect()
	 */
	@Override
	public void onConnect() {
		this.behaviorDelegate.setLaunched();
		Activator.openTerminalView().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.fusesource.ide.server.view.ITerminalConnectionListener#onDisconnect()
	 */
	@Override
	public void onDisconnect() {
		this.behaviorDelegate.stop(false);
		
		// open the terminal view
		IViewPart vp = Activator.openTerminalView();
		if (vp == null || vp instanceof SshView == false) {
			Activator.getLogger().error("Unable to open the terminal view!");
			return;
		}
		
		// get the view
		final SshView connectorView = (SshView)vp;
		
		// add a connection listener
		connectorView.addConnectionListener(this);
		
		connectorView.setPartName(TERMINAL_VIEW_LABEL);
	}
}
