/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lsp4xml.uriresolver;

import java.io.IOException;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * URI resolver API
 */
public interface URIResolverExtension extends XMLEntityResolver {

	/**
	 * @param baseLocation - the location of the resource that contains the uri
	 * @param publicId     - an optional public identifier (i.e. namespace name), or
	 *                     null if none
	 * @param systemId     - an absolute or relative URI, or null if none
	 * @return an absolute URI representation of the 'logical' location of the
	 *         resource
	 */
	public String resolve(String baseLocation, String publicId, String systemId);

	@Override
	default XMLInputSource resolveEntity(XMLResourceIdentifier rid) throws XNIException, IOException {
		XMLInputSource is = null;
		String id = rid.getPublicId();
		if (id == null) {
			id = rid.getNamespace();
		}

		String location = null;
		if (id != null || rid.getLiteralSystemId() != null) {
			location = this.resolve(rid.getBaseSystemId(), id, rid.getLiteralSystemId());
		}

		if (location != null) {
			is = new XMLInputSource(rid.getPublicId(), location, location);
		}
		return is;
	}
}
