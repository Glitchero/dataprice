package com.dataprice.ui.universities;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Student;
import com.dataprice.model.entity.University;
import com.dataprice.service.adduniversity.AddUniversityService;
import com.dataprice.utils.NotificationsMessages;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class AddUniversityLayoutFactory {

	@Autowired
	private AddUniversityService addUniversityService;
	
	
	private class AddUniversityLayout extends VerticalLayout implements Button.ClickListener {

		private TextField universityName;
		private TextField universityCountry;
		private TextField universityCity;
		private Button saveButton;
		private Binder<University> binder;
		private University university;
		
		private UniversitySavedListener universitySavedListener;

		public AddUniversityLayout(UniversitySavedListener universitySavedListener) {
			this.universitySavedListener = universitySavedListener;
		}



		public AddUniversityLayout init() {

			binder = new Binder<>(University.class);
			
			university = new University();
			
			
			universityName = new TextField("Name");
			universityCountry = new TextField("Country");
			universityCity = new TextField("City");
			
			saveButton = new Button("Save", this);
			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
	

			return this;
		}
		


		public Component layout() {
			
			setWidth("100%");

			GridLayout grid = new GridLayout(2,4);
			grid.setHeightUndefined();
			grid.setSpacing(true);
			
			grid.addComponent(universityName,0,0,1,0);
			grid.addComponent(universityCountry,0,1,1,1);
			grid.addComponent(universityCity,0,2,1,2);
			grid.addComponent(new HorizontalLayout(saveButton),0,3,0,3);

			return grid;
		}
		
		public AddUniversityLayout bind() {
		
			binder.forField(universityName)
			  // Shorthand for requiring the field to be non-empty
			  .asRequired("universityName is required")
			  .bind("universityName");
			

			binder.forField(universityCountry)
			  .asRequired("universityCountry is required")
			  .bind("universityCountry");
			
			
			binder.forField(universityCity)
			  .asRequired("universityCity is required")
			  .bind("universityCity");
			
			binder.readBean(university);
			
			return this;
		}
		
		

		public void buttonClick(ClickEvent event) {
			
			try {
				binder.writeBean(university);
			} catch (ValidationException e) {
				Notification.show("ERROR","Validation error",Type.ERROR_MESSAGE);
				return;
			}
			clearFields();
			
			addUniversityService.addUniversity(university);
			universitySavedListener.universitySaved();
			Notification.show("SAVE","University saved",Type.WARNING_MESSAGE);
		}

		private void clearFields() {
			universityName.setValue("");
			universityCountry.setValue("");
			universityCity.setValue("");
		}



	
	}
	

	public Component createComponent(UniversitySavedListener universitySavedListener) {
		return new AddUniversityLayout(universitySavedListener).init().bind().layout();
	}
	
}
