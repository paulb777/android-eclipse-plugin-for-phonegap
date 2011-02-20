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
 * Abstract representation of a function object with-respect-to Javascript debugging.
 * 
 * @see ObjectReference
 * @since 1.0
 * @noextend This interface is not intended to be extended by clients.
 */
public interface FunctionReference extends ObjectReference {

	/**
	 * Returns the name of the function as it has been defined at the creation time of this value.
	 * 
	 * @return the name of the function
	 */
	public String functionName();
	
	/**
	 * Returns the source code for the body of the function.<br>
	 * <br>
	 * This method can return <code>null</code>
	 * 
	 * @return the source code for the function body
	 */
	public String functionBody();
}
