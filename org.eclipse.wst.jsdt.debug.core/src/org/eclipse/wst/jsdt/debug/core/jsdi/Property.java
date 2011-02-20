/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.core.jsdi;

/**
 * Abstract description of an object property.
 * 
 * @see Mirror
 * @since 1.0
 * @noextend This interface is not intended to be extended by clients.
 */
public interface Property extends Mirror {

	/**
	 * Returns the name of this {@link Property} or <code>null</code> if one could not be determined.
	 * 
	 * @return the name of this {@link Property} or <code>null</code>.
	 */
	public String name();
	
	/**
	 * Returns the value of this {@link Property}.
	 * 
	 * @return the value of this {@link Property}.
	 */
	public Value value();
}
