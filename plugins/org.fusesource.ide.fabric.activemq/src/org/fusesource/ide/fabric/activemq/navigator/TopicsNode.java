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

package org.fusesource.ide.fabric.activemq.navigator;

import io.fabric8.activemq.facade.BrokerFacade;
import io.fabric8.activemq.facade.TopicViewFacade;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.fusesource.ide.commons.tree.RefreshableCollectionNode;
import org.fusesource.ide.commons.ui.ContextMenuProvider;
import org.fusesource.ide.commons.ui.ImageProvider;
import org.fusesource.ide.commons.ui.Shells;
import org.fusesource.ide.commons.util.Strings;
import org.fusesource.ide.fabric.FabricPlugin;
import org.fusesource.ide.fabric.activemq.Messages;


public class TopicsNode extends RefreshableCollectionNode implements ImageProvider, ContextMenuProvider  {

	private final BrokerNode brokerNode;
	private final BrokerFacade facade;

	public TopicsNode(BrokerNode brokerNode) {
		super(brokerNode);
		this.brokerNode = brokerNode;
		this.facade = brokerNode.getFacade();
	}

	@Override
	public String toString() {
		return "Topics";
	}

	@Override
	protected void loadChildren() {
		try {
			Collection<TopicViewFacade> topics = facade.getTopics();
			if (topics != null) {
				for (TopicViewFacade topic : topics) {
					addChild(new TopicNode(this, topic));
				}
			}
		} catch (Exception e) {
			brokerNode.handleException(this, e);
		}
	}

	public BrokerNode getBrokerNode() {
		return brokerNode;
	}

	@Override
	public Image getImage() {
		return FabricPlugin.getDefault().getImage("topic.png");
	}

	@Override
	public void provideContextMenu(IMenuManager menu) {
		Action createTopicAction = new Action(Messages.CreateTopicAction, SWT.CHECK) {
			@Override
			public void run() {
				showCreateTopicDialog();
			}

		};
		createTopicAction.setToolTipText(Messages.CreateTopicActionToolTip);
		createTopicAction.setImageDescriptor(FabricPlugin.getDefault().getImageDescriptor("new_topic.png"));
		menu.add(createTopicAction);

	}

	protected void showCreateTopicDialog() {
		InputDialog dialog = new InputDialog(Shells.getShell(), Messages.CreateTopicDialogTitle,
				Messages.CreateTopicDialogMessage, "", null);
		int result = dialog.open();
		if (result == Window.OK) {
			String topic = dialog.getValue();
			createTopic(topic);
		}
	}

	protected void createTopic(String topic) {
		if (!Strings.isBlank(topic)) {
			try {
				getFacade().getBrokerAdmin().addTopic(topic);
				refresh();
			} catch (Exception e) {
				FabricPlugin.showUserError("Failed to create Topic", "Failed to create topic: " + topic, e);
			}
		}
	}

	protected BrokerFacade getFacade() {
		return getBrokerNode().getFacade();
	}
}
