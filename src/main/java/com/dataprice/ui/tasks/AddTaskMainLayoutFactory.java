package com.dataprice.ui.tasks;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.*;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.utils.Gender;
import com.dataprice.utils.NotificationsMessages;

@org.springframework.stereotype.Component
public class AddTaskMainLayoutFactory {

	    private class AddTaskMainLayout extends VerticalLayout implements Button.ClickListener{
		

	    private TextField taskName;
	    private ComboBox retail;
	    private TextField seed;
	   // private TextField status;
	    private Button saveButton;
	    private Button clearButton;

	    private Binder<Task> binder;
	
	    private Task task;
	
	    private TaskSavedListener taskSavedListener;
	    
	    public AddTaskMainLayout(TaskSavedListener taskSavedListener) {
			this.taskSavedListener = taskSavedListener;
		}

		public AddTaskMainLayout init() {

	    	binder = new Binder<>(Task.class);

	    	task = new Task();
	    	
	    	taskName = new TextField("TaskName");
	    	retail = new ComboBox("Retail");
	    	seed = new TextField("Seed");
		//	status = new TextField("Status");
			
			saveButton = new Button("save");
			clearButton = new Button("clear");

			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			clearButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			
			saveButton.addClickListener(this);
			clearButton.addClickListener(this);

			//gender.setEmptySelectionAllowed(false);
			retail.setItems("Liverpool","Sears");

			return this;
		    
	    }
	
		public AddTaskMainLayout bind() {
			binder.forField(taskName)
			  .asRequired("taskName is required")
			  .bind("taskName");
			
			binder.forField(retail)
			  .asRequired("retail is required")
			  .bind("retail");

			binder.forField(seed)
			  .asRequired("Seed is required")
			  .bind("seed");
			
		//	binder.forField(status)
		//	  .asRequired("Status must be set")
		//	  .bind("status");
			
			binder.readBean(task);
			
			return this;
		}	
		
	    public Component layout() {		
	    	setMargin(true);

			GridLayout gridLayout = new GridLayout(2, 3);
			gridLayout.setSizeUndefined();
			gridLayout.setSpacing(true);

			gridLayout.addComponent(taskName, 0, 0);
			gridLayout.addComponent(retail, 1, 0);

			gridLayout.addComponent(seed, 0, 1);
		//	gridLayout.addComponent(status, 1, 1);


			gridLayout.addComponent(new HorizontalLayout(saveButton, clearButton), 0, 2);

			return gridLayout;

	    }

		@Override
		public void buttonClick(ClickEvent event) {
                 if (event.getSource()==this.saveButton)	{
                	 save();
                 }else {
                	 clearField();
                 }
		}

		private void save() {
			try {
				binder.writeBean(task);
			} catch (ValidationException e) {
				Notification.show("ERROR","Task is not saved",Type.ERROR_MESSAGE);
				return;
			}
			//System.out.println(task);
			addtaskService.saveTask(task);
			taskSavedListener.taskSaved();
			clearField();
			Notification.show("SAVE","Task is saved",Type.WARNING_MESSAGE);
		}

		private void clearField() {

			taskName.setValue("");
			seed.setValue("");
		//	status.setValue("");
			retail.setValue(null);
		}
	  
	}
	
	@Autowired
    private AddTaskService addtaskService;
	    
	public Component createComponent(TaskSavedListener taskSavedListener) {
		return new AddTaskMainLayout(taskSavedListener).init().bind().layout();
	}
	
}