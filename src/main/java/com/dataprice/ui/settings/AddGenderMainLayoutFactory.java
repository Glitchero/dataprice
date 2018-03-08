package com.dataprice.ui.settings;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Gender;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class AddGenderMainLayoutFactory {
	
	private class AddGenderMainLayout extends VerticalLayout implements Button.ClickListener{
		
		private TextField genderName;
		
		private Button saveButton;
		 
		private Binder<Gender> binder;
		
		private Gender gender;
		
		private GenderSavedListener genderSavedListener;
		
		 public AddGenderMainLayout(GenderSavedListener genderSavedListener) {
				this.genderSavedListener = genderSavedListener;
			}
		 
		
		 public AddGenderMainLayout init() {

		    	binder = new Binder<>(Gender.class);

		    	gender = new Gender();
		    	
		    	genderName = new TextField("GenderName");
		    
				saveButton = new Button("save");

				saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				saveButton.addClickListener(this);

				return this;
			    
		    }
		 
		 
		 public AddGenderMainLayout bind() {
				binder.forField(genderName)
				  .asRequired("genderName is required")
				  .bind("genderName");
				
				binder.readBean(gender);
				
				return this;
			}	
		 
		 
		 public Component layout() {		
		    	setMargin(true);

				HorizontalLayout horizontalLayout = new HorizontalLayout();
				horizontalLayout.addComponent(genderName);
				horizontalLayout.addComponent(saveButton);

				return horizontalLayout;

		    }

		 
		 @Override
			public void buttonClick(ClickEvent event) {
	                	 save();
			}
		 
		 private void save() {
				try {
					binder.writeBean(gender);
				} catch (ValidationException e) {
					Notification.show("ERROR","Gender is not saved",Type.ERROR_MESSAGE);
					return;
				}
				addGenderService.saveGender(gender);
				genderSavedListener.genderSaved();
				clearField();
				Notification.show("SAVE","Gender is saved",Type.WARNING_MESSAGE);
			}
		
		 
		 private void clearField() {
				genderName.setValue("");
		 }
		 
		 
	}

	@Autowired
    private AddGenderService addGenderService;
	
	public Component createComponent(GenderSavedListener genderSavedListener) {
		return new AddGenderMainLayout(genderSavedListener).init().bind().layout();
	}
	
	
}
