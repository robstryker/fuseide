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

package org.fusesource.ide.jmx.commons.views.messages;

import org.fusesource.ide.foundation.core.util.Strings;
import org.fusesource.ide.foundation.core.xml.XmlEscapeUtility;
import org.fusesource.ide.jmx.commons.messages.Exchanges;
import org.fusesource.ide.jmx.commons.messages.IExchange;



public class MessageUIHelper {
	

	public static String getBody(IExchange selectedExchange) {
		String body = Strings.getOrElse(Exchanges.getBody(selectedExchange), "");
		
		// lets XML unescape it...
		return XmlEscapeUtility.unescape(body);
	}		
}
