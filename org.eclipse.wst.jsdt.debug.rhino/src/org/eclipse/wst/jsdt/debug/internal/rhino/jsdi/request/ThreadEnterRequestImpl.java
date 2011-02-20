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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

/**
 * Rhino implementation of {@link ThreadEnterRequest}
 * 
 * @since 1.0
 */
public class ThreadEnterRequestImpl extends EventRequestImpl implements ThreadEnterRequest {

	private ThreadReference thread;

	/**
	 * Constructor
	 * @param vm
	 */
	public ThreadEnterRequestImpl(VirtualMachineImpl vm) {
		super(vm);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest#addThreadFilter(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public synchronized void addThreadFilter(ThreadReference thread) {
		checkDeleted();
		this.thread = thread;
	}

	/**
	 * Return the underlying {@link ThreadReference}
	 * @return the underlying {@link ThreadReference}
	 */
	public synchronized ThreadReference thread() {
		return thread;
	}
}