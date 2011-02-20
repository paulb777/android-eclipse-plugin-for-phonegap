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
package org.eclipse.wst.jsdt.debug.internal.ui.breakpoints.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.ui.IHelpContextIds;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;
import org.eclipse.wst.jsdt.debug.internal.ui.breakpoints.Messages;

/**
 * Standard breakpoint editor that includes suspend policy and hit count options
 * 
 * @since 1.0
 */
public class StandardJavaScriptBreakpointEditor extends AbstractJavaScriptBreakpointEditor {
	
	private IJavaScriptBreakpoint fBreakpoint;
	private Button fHitCountButton;
	private Text fHitCountText;
	private Button fSuspendThread;
	private Button fSuspendTarget;
	
	/**
     * Property id for hit count enabled state.
     */
    public static final int PROP_HIT_COUNT_ENABLED = 0x1005;
    
	/**
     * Property id for breakpoint hit count.
     */
    public static final int PROP_HIT_COUNT = 0x1006;  
    
	/**
     * Property id for suspend policy.
     */
    public static final int PROP_SUSPEND_POLICY = 0x1007;
    
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.ui.breakpoints.AbstractJavaScriptBreakpointEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IHelpContextIds.STANDARD_BREAKPOINT_EDITOR);
		return createStandardControls(parent);
	}
	
	/**
	 * @param parent
	 * @return
	 */
	protected Control createStandardControls(Composite parent) {
		Composite composite = SWTFactory.createComposite(parent, parent.getFont(), 4, 1, 0, 0, 0);
		fHitCountButton = SWTFactory.createCheckButton(composite, processMnemonics(Messages.hit_count), null, false, 1);
		fHitCountButton.setLayoutData(new GridData());
		fHitCountButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				boolean enabled = fHitCountButton.getSelection();
				fHitCountText.setEnabled(enabled);
				if(enabled) {
					fHitCountText.setFocus();
				}
				setDirty(PROP_HIT_COUNT_ENABLED);
			}
		});
		fHitCountText = SWTFactory.createSingleText(composite, 1);
		GridData gd = (GridData) fHitCountText.getLayoutData();
		gd.minimumWidth = 50;
		fHitCountText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setDirty(PROP_HIT_COUNT);
			}
		});
		SWTFactory.createLabel(composite, "", 1); // spacer //$NON-NLS-1$
		Composite radios = SWTFactory.createComposite(composite, composite.getFont(), 2, 1, GridData.FILL_HORIZONTAL, 0, 0);
		fSuspendThread = SWTFactory.createRadioButton(radios, processMnemonics(Messages.suspend_thread));
		fSuspendThread.setLayoutData(new GridData());
		fSuspendTarget = SWTFactory.createRadioButton(radios, processMnemonics(Messages.suspend_target));
		fSuspendTarget.setLayoutData(new GridData());
		fSuspendThread.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setDirty(PROP_SUSPEND_POLICY);
			}
		});	
		fSuspendTarget.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setDirty(PROP_SUSPEND_POLICY);
			}
		});
		composite.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
		return composite;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.breakpoints.AbstractJavaBreakpointEditor#setInput(java.lang.Object)
	 */
	public void setInput(Object breakpoint) throws CoreException {
		if (breakpoint instanceof IJavaScriptBreakpoint) {
			setBreakpoint((IJavaScriptBreakpoint) breakpoint);
		} else {
			setBreakpoint(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.breakpoints.AbstractJavaBreakpointEditor#getInput()
	 */
	public Object getInput() {
		return fBreakpoint;
	}
	
	/**
	 * Sets the breakpoint to edit. The same editor can be used iteratively for different breakpoints.
	 * 
	 * @param breakpoint the breakpoint to edit or <code>null</code> if none
	 * @exception CoreException if unable to access breakpoint attributes
	 */
	protected void setBreakpoint(IJavaScriptBreakpoint breakpoint) throws CoreException {
		fBreakpoint = breakpoint;
		boolean enabled = false;
		boolean hasHitCount = false;
		String text = Constants.EMPTY_STRING;
		boolean suspendThread = true;
		if (breakpoint != null) {
			enabled = true;
			int hitCount = breakpoint.getHitCount();
			if (hitCount > 0) {
				text = new Integer(hitCount).toString();
				hasHitCount = true;
			}
			suspendThread= breakpoint.getSuspendPolicy() == IJavaScriptBreakpoint.SUSPEND_THREAD;
		}
		fHitCountButton.setEnabled(enabled);
		fHitCountButton.setSelection(enabled & hasHitCount);
		fHitCountText.setEnabled(hasHitCount);
		fHitCountText.setText(text);
		fSuspendThread.setEnabled(enabled);
		fSuspendTarget.setEnabled(enabled);
		fSuspendThread.setSelection(suspendThread);
		fSuspendTarget.setSelection(!suspendThread);
		setDirty(false);
	}
	
	/**
	 * Returns the current breakpoint being edited or <code>null</code> if none.
	 * 
	 * @return breakpoint or <code>null</code>
	 */
	protected IJavaScriptBreakpoint getBreakpoint() { 
		return fBreakpoint;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.breakpoints.AbstractJavaBreakpointEditor#setFocus()
	 */
	public void setFocus() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.breakpoints.AbstractJavaBreakpointEditor#doSave()
	 */
	public void doSave() throws CoreException {
		if (fBreakpoint != null) {
			int suspendPolicy = IJavaScriptBreakpoint.SUSPEND_THREAD;
			if(fSuspendTarget.getSelection()) {
				suspendPolicy = IJavaScriptBreakpoint.SUSPEND_TARGET;
			}
			fBreakpoint.setSuspendPolicy(suspendPolicy);
			int hitCount = -1;
			if (fHitCountButton.getSelection()) {
				try {
					hitCount = Integer.parseInt(fHitCountText.getText());
				} 
				catch (NumberFormatException e) {
					throw new CoreException(new Status(
							IStatus.ERROR,
							JavaScriptDebugUIPlugin.PLUGIN_ID,
							IStatus.ERROR,
							Messages.hit_count_must_be_positive,
							e));
				}
			}
			fBreakpoint.setHitCount(hitCount);
		}
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.breakpoints.AbstractJavaBreakpointEditor#getStatus()
	 */
	public IStatus getStatus() {
		if (fHitCountButton.getSelection()) {
			String hitCountText = fHitCountText.getText();
			int hitCount = -1;
			try {
				hitCount = Integer.parseInt(hitCountText);
			} catch (NumberFormatException e1) {
				return new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, IStatus.ERROR, Messages.hit_count_must_be_positive, null);
			}
			if (hitCount < 1) {
				return new Status(IStatus.ERROR, JavaScriptDebugUIPlugin.PLUGIN_ID, IStatus.ERROR, Messages.hit_count_must_be_positive, null);
			}
		}
		return Status.OK_STATUS;
	}

	/**
	 * Creates and returns a check box button with the given text.
	 * 
	 * @param parent parent composite
	 * @param text label
	 * @param propId property id to fire on modification
	 * @return check box
	 */
	protected Button createSusupendPropertyEditor(Composite parent, String text, final int propId) {
		Button button = new Button(parent, SWT.CHECK);
		button.setFont(parent.getFont());
		button.setText(text);
		GridData gd = new GridData(SWT.BEGINNING);
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setDirty(propId);
			}
		});
		return button;
	}
}
