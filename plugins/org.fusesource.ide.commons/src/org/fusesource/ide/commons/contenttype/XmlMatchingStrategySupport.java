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

package org.fusesource.ide.commons.contenttype;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.fusesource.ide.commons.Activator;
import org.fusesource.ide.commons.util.IFiles;
import org.xml.sax.InputSource;


public abstract class XmlMatchingStrategySupport implements IEditorMatchingStrategy {

	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		if (input instanceof IFileEditorInput) {
			IFile ifile = ((IFileEditorInput) input).getFile();
			return matches(ifile);
		}
		return false;
	}

	public boolean matches(IFile ifile) {
		try {
			File file = IFiles.toFile(ifile);
			if (file != null) {
				// lets parse the XML and look for the namespaces 
				FindNamespaceHandlerSupport handler = createNamespaceFinder();
				handler.parseContents(new InputSource(new FileInputStream(file)));
				return handler.isNamespaceFound();
			}
		} catch (Exception e) {
			Activator.getLogger().error("** Load failed. Using default model. **", e);
		}
		return false;
	}

	protected abstract FindNamespaceHandlerSupport createNamespaceFinder();

}