 
package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ExitHandler {
	@Execute
	public void execute(IWorkbench workbench, Shell shell) {
		boolean result = MessageDialog.openConfirm(shell, "Close", "Close application?");

		if (result) {
			workbench.close();
		}
	}
		
}