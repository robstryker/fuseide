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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import io.fabric8.camel.facade.mbean.CamelRouteMBean;
import org.fusesource.ide.camel.model.AbstractNode;
import org.fusesource.ide.camel.model.RouteSupport;
import org.fusesource.ide.commons.ui.ImageProvider;
import org.fusesource.ide.commons.util.Function1;
import org.fusesource.ide.commons.util.Objects;
import org.fusesource.ide.commons.util.Strings;
import org.fusesource.ide.fabric.FabricPlugin;
import org.fusesource.ide.fabric.camel.Messages;


public class RouteNode extends ProcessorNodeSupport implements ImageProvider {
	private final RoutesNode routesNode;
	private final RouteSupport route;
	private CamelRouteMBean routeMBean;

	public RouteNode(RoutesNode routesNode, RouteSupport route) {
		super(routesNode, route);
		this.routesNode = routesNode;
		this.route = route;
	}

	public RoutesNode getRoutesNode() {
		return routesNode;
	}

	@Override
	public CamelContextNode getCamelContextNode() {
		return getRoutesNode().getCamelContextNode();
	}

	@Override
	public String toString() {
		return Strings.getOrElse(getRouteId(), "Route");
	}

	@Override
	public AbstractNode getAbstractNode() {
		return route;
	}

	@Override
	public Image getImage() {
		return route.getSmallImage();
	}

	@Override
	protected void loadChildren() {
		List<AbstractNode> children = route.getRootNodes();
		for (AbstractNode node : children) {
			addChild(new ProcessorNode(this, this, node));
		}
	}


	public CamelRouteMBean getRouteMBean() {
		return routeMBean;
	}

	public void setRouteMBean(CamelRouteMBean mbean) {
		this.routeMBean = mbean;
		setPropertyBean(this.routeMBean);
	}

	@Override
	public void provideContextMenu(IMenuManager menu) {
		Function1<IMenuManager, Void> fn = new Function1<IMenuManager, Void>() {
			@Override
			public Void apply(IMenuManager mm) {
				if (isMBeanStarted()) {
					Action stopRouteAction = new Action(Messages.StopRouteAction, SWT.CHECK) {
						@Override
						public void run() {
							stopMBean();
						}

					};
					stopRouteAction.setToolTipText(Messages.StopRouteActionToolTip);
					stopRouteAction.setImageDescriptor(FabricPlugin.getDefault().getImageDescriptor("stop_task.gif"));
					mm.add(stopRouteAction);
				} else {
					Action startRouteAction = new Action(Messages.StartRouteAction, SWT.CHECK) {
						@Override
						public void run() {
							startMBean();
						}

					};
					startRouteAction.setToolTipText(Messages.StartRouteActionToolTip);
					startRouteAction.setImageDescriptor(FabricPlugin.getDefault().getImageDescriptor("start_task.gif"));
					mm.add(startRouteAction);
				}
				return null;
			}
		};
		getCamelContextNode().provideContextMenu(menu, fn);
	}

	protected void startMBean() {
		routeMBean.start();

		// TODO how to force a refresh of the dependent views???
	}

	protected void stopMBean() {
		routeMBean.stop();
		// TODO how to force a refresh of the dependent views???
	}

	public boolean isMBeanStarted() {
		String state = routeMBean.getState();
		return Objects.equal("Started", state);
	}

	public String getNodeId() {
		return null;
	}

}
