package com.vogella.tasks.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.widgets.ButtonFactory;
import org.eclipse.jface.widgets.LabelFactory;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

import com.vogella.tasks.model.Task;

public class TodoDetailsPart {
	private Text txtSummary;
	private Text txtDescription;
	private DateTime dateTime;
	private Button btnDone;
	
	private java.util.Optional<Task> task = java.util.Optional.ofNullable(null);  // defining new field

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		GridDataFactory gdFactory = GridDataFactory.fillDefaults();
		LabelFactory labelFactory = LabelFactory.newLabel(SWT.NONE);

		labelFactory.text("Summary").create(parent);

		txtSummary = WidgetFactory.text(SWT.BORDER).layoutData(gdFactory.grab(true, false).create()).create(parent);

		labelFactory.text("Description").create(parent);

		txtDescription = WidgetFactory.text(SWT.BORDER | SWT.MULTI | SWT.V_SCROLL)
				.layoutData(gdFactory.align(SWT.FILL, SWT.FILL).grab(true, true).create()).create(parent);

		labelFactory.text("Due Date").create(parent);

		dateTime = WidgetFactory.dateTime(SWT.BORDER).layoutData(labelFactory)
				.layoutData(gdFactory.align(SWT.FILL, SWT.CENTER).grab(false, false).create()).create(parent);

		btnDone = WidgetFactory.button(SWT.CHECK).text("Done").create(parent);
		
		updateUserInterface(task); //gets the current active selected list of tasks to be injected... uses special string key defined by ISerficeConstants.ACTIVE_SELECTION
		
		Button pushButton = ButtonFactory.newButton(SWT.PUSH).text("Press me").create(parent);
		Button checkButton= ButtonFactory.newButton(SWT.CHECK).onSelect(t -> pushButton.setEnabled(!pushButton.getEnabled())).text("Press me").create(parent);
		checkButton.setSelection(true);
	}

	@Focus
	public void setFocus() {
		txtSummary.setFocus();
	}
	
	@Inject
	public void setTasks(@Optional @Named(IServiceConstants.ACTIVE_SELECTION)
	List<Task> tasks) {
		if (tasks == null || tasks.isEmpty() || tasks.get(0) == null) {
			this.task = java.util.Optional.empty();
		} else {
			this.task = java.util.Optional.of(tasks.get(0));
		}
		
		updateUserInterface(this.task);
	}
	
	private void enableUserInterface(boolean enabled) { // allows to enable/disable the UI fields if no task is set
		if (txtSummary != null && txtSummary.isDisposed()) {
			txtSummary.setEnabled(enabled);
			txtDescription.setEnabled(enabled);
			dateTime.setEnabled(enabled);
			btnDone.setEnabled(enabled);
		}
	}
	
	private void updateUserInterface(java.util.Optional<Task> task) { // Method disables the widgets if no data object is available otherwise shows data
		if (!task.isPresent()) {
			enableUserInterface(false);
			return; // nothing left to do
		}
		
		enableUserInterface(true);
		
		// this check makes sure the UI is available, and assumes you have a txt widget called "txtSummary"
		if (txtSummary != null && !txtSummary.isDisposed()) {
			enableUserInterface(true);
			txtSummary.setText(task.get().getSummary());
			txtDescription.setText(task.get().getDescription());
		}
	}
}
