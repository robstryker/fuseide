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

import org.fusesource.ide.foundation.core.xml.namespace.BlueprintNamespaceHandler;
import org.fusesource.ide.foundation.core.xml.namespace.FindNamespaceHandlerSupport;

/**
 * @author lhein
 */
public class BlueprintXmlMatchingStrategy extends XmlMatchingStrategySupport {

	/* (non-Javadoc)
	 * @see org.fusesource.ide.commons.contenttype.XmlMatchingStrategySupport#createNamespaceFinder()
	 */
	@Override
	protected FindNamespaceHandlerSupport createNamespaceFinder() {
		return new BlueprintNamespaceHandler();
	}
}
