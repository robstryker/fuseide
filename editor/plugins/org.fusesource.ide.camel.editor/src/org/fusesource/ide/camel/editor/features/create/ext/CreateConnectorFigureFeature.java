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
package org.fusesource.ide.camel.editor.features.create.ext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.m2e.core.MavenPlugin;
import org.fusesource.ide.camel.editor.Activator;
import org.fusesource.ide.camel.editor.ConnectorsMessages;
import org.fusesource.ide.camel.editor.editor.RiderDesignEditor;
import org.fusesource.ide.camel.model.AbstractNode;
import org.fusesource.ide.camel.model.ConnectorEndpoint;
import org.fusesource.ide.camel.model.Endpoint;
import org.fusesource.ide.camel.model.connectors.Component;
import org.fusesource.ide.camel.model.connectors.ComponentDependency;
import org.fusesource.ide.commons.util.Strings;

/**
 * @author lhein
 */
public class CreateConnectorFigureFeature extends CreateFigureFeature<Endpoint> {
    
    private final ConnectorEndpoint endpoint;
    protected final Component component;
    
    /**
     * creates a component create feature
     * 
     * @param fp
     * @param component
     */
    public CreateConnectorFigureFeature(IFeatureProvider fp, Component component) {
        super(fp, Strings.humanize(component.getTitle()), component.getDescription(), Endpoint.class);
        this.endpoint = new ConnectorEndpoint(component.getSyntax() != null ? component.getSyntax() : String.format("%s:", component.getSchemes().get(0).getScheme())); // we use the first found protocol string
        setExemplar(this.endpoint);
        this.component = component;
    }
    
    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.features.create.CreateFigureFeature#getIconName()
     */
    @Override
    protected String getIconName() {
        return String.format("%s.png", this.component.getTitle().replaceAll("-", "_"));
    }
        
    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.features.create.CreateFigureFeature#createNode()
     */
    @Override
    protected AbstractNode createNode() {
        return new ConnectorEndpoint(this.endpoint);
    }
        
    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.features.create.CreateFigureFeature#create(org.eclipse.graphiti.features.context.ICreateContext)
     */
    @Override
    public Object[] create(ICreateContext context) {
        // add maven dependency to pom.xml if needed
        try {
            updateMavenDependencies(component.getDependencies());
        } catch (CoreException ex) {
            Activator.getLogger().error("Unable to add the component dependency to the project maven configuration file.", ex);
        }
        // and then let the super class continue the work
        return super.create(context);
    }
    
    /**
	 * @return the component
	 */
	public Component getConnector() {
		return this.component;
	}
}