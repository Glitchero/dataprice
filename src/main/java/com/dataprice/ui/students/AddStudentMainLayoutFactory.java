package com.dataprice.ui.students;

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
import com.dataprice.service.addstudent.AddStudentService;
import com.dataprice.utils.Gender;
import com.dataprice.utils.NotificationsMessages;
import com.dataprice.utils.StudentsStringUtils;

@org.springframework.stereotype.Component
public class AddStudentMainLayoutFactory {

	    private class AddStudentMainLayout extends VerticalLayout implements Button.ClickListener{
		

	    private TextField firstName;
	    private TextField lastName;
	    private TextField age;
	    private ComboBox gender;
	    private Button saveButton;
	    private Button clearButton;

	    private Binder<Student> binder;
	
	    private Student student;
	
	    private StudentSavedListener studentSavedListener;
	    
	    public AddStudentMainLayout(StudentSavedListener studentSavedListener) {
			this.studentSavedListener = studentSavedListener;
		}

		public AddStudentMainLayout init() {

	    	binder = new Binder<>(Student.class);

	    	student = new Student();
	    	
			firstName = new TextField(StudentsStringUtils.FIRST_NAME.getString());
			lastName = new TextField(StudentsStringUtils.LAST_NAME.getString());
			age = new TextField(StudentsStringUtils.AGE.getString());
			gender = new ComboBox(StudentsStringUtils.GENDER.getString());
			
			saveButton = new Button(StudentsStringUtils.SAVE.getString());
			clearButton = new Button(StudentsStringUtils.CANCEL.getString());

			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			clearButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			
			saveButton.addClickListener(this);
			clearButton.addClickListener(this);

			//gender.setEmptySelectionAllowed(false);
			gender.setItems(Gender.MALE.getString(),Gender.FEMALE.getString());

			return this;
		    
	    }
	
		public AddStudentMainLayout bind() {
			binder.forField(firstName)
			  // Shorthand for requiring the field to be non-empty
			  .asRequired("firstName is required")
			  .bind("firstName");
			

			binder.forField(lastName)
			  .asRequired("lastName is required")
			  .bind("lastName");

			binder.forField(age)
			  .asRequired("age is required")
			  .withNullRepresentation("")
			  .withConverter(new StringToIntegerConverter("Please enter a number"))
			  // Validator will be run with the converted value
			  .withValidator(age -> age >= 0 && age < 100,
			    "Person must be between 0 and 100 age")
			  .bind("age");
			
			binder.forField(gender)
			  .asRequired("Gender must be set")
			  .bind("gender");
			
			binder.readBean(student);
			
			return this;
		}	
		
	    public Component layout() {		
	    	setMargin(true);

			GridLayout gridLayout = new GridLayout(2, 3);
			gridLayout.setSizeUndefined();
			gridLayout.setSpacing(true);

			gridLayout.addComponent(firstName, 0, 0);
			gridLayout.addComponent(lastName, 1, 0);

			gridLayout.addComponent(age, 0, 1);
			gridLayout.addComponent(gender, 1, 1);


			gridLayout.addComponent(new HorizontalLayout(saveButton, clearButton), 0, 2);

			age.clear();
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
				binder.writeBean(student);
			} catch (ValidationException e) {
				Notification.show(NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_TITLE.getString(),NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_DESCRIPTION.getString(),Type.ERROR_MESSAGE);
				return;
			}
			//System.out.println(student);
			addStudentService.saveStudent(student);
			studentSavedListener.studentSaved();
			clearField();
			Notification.show(NotificationsMessages.STUDENT_SAVE_SUCCESS_TITLE.getString(), NotificationsMessages.STUDENT_SAVE_SUCCESS_DESCRIPTION.getString(), Type.WARNING_MESSAGE);
		}

		private void clearField() {

			firstName.setValue("");
			lastName.setValue("");
			age.setValue("");
			gender.setValue(null);
		}
	  
	}
	
	@Autowired
    private AddStudentService addStudentService;
	    
	public Component createComponent(StudentSavedListener studentSavedListener) {
		return new AddStudentMainLayout(studentSavedListener).init().bind().layout();
	}
	
}
