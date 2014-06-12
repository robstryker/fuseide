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

package org.fusesource.ide.fabric.camel.navigator;

import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.fusesource.ide.commons.tree.Refreshable;
import org.fusesource.ide.commons.tree.RefreshableCollectionNode;
import org.fusesource.ide.commons.ui.ContextMenuProvider;
import org.fusesource.ide.commons.ui.ImageProvider;
import org.fusesource.ide.commons.util.Objects;
import org.fusesource.ide.fabric.FabricPlugin;
import org.jboss.tools.jmx.core.tree.Node;



public class EndpointSchemeNode extends RefreshableCollectionNode implements ImageProvider, ContextMenuProvider, Refreshable {
	private final EndpointsNode endpointsNode;
	private final String scheme;
	

	public EndpointSchemeNode(EndpointsNode endpointsNode, String scheme) {
		super(endpointsNode);
		this.endpointsNode = endpointsNode;
		this.scheme = scheme;
	}


	public EndpointsNode getEndpointsNode() {
		return endpointsNode;
	}

	
	@Override
	protected void loadChildren() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void refresh() {
		endpointsNode.refresh();
	}


	@Override
	public void provideContextMenu(IMenuManager menu) {
		endpointsNode.addCreateEndpointAction(menu, getScheme() + "://");
	}

	@Override
	public String toString() {
		return scheme;
	}


	public String getScheme() {
		return scheme;
	}

	@Override
	public Image getImage() {
		return FabricPlugin.getDefault().getImage("endpoint_folder.png");
	}


	/**
	 * Returns true if there is an endpoint child with the given URI
	 */
	public boolean containsUri(String uri) {
		List<Node> list = getChildrenList();
		for (Node node : list) {
			if (node instanceof EndpointNode) {
				EndpointNode enode = (EndpointNode) node;
				if (Objects.equal(uri, enode.getEndpointUri())) {
					return true;
				}
			}
		}
		return false;
	}


}
