package com.dataprice.ui.user;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.FeedSettings;
import com.dataprice.model.entity.Role;
import com.dataprice.model.entity.User;
import com.dataprice.service.security.RegisterUserService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class AddUserLayoutFactory {

	
	private class AddUserLayout extends VerticalLayout{

		private UserLayoutFactory userLayoutFactory;
		private TextField username;
		private PasswordField passwordField;
		private PasswordField passwordAgainField;
		private Button saveButton;
		private User user;
		private Binder<User> binder;
		
		
		public AddUserLayout(UserLayoutFactory userLayoutFactory) {
			this.userLayoutFactory = userLayoutFactory;
		}


		public AddUserLayout init() {
			binder = new Binder<>(User.class);
			user =  new User();
			
			saveButton = new Button("Save");
			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			saveButton.setWidth("100%");
			
			username = new TextField("Username");
			username.setWidth("100%");
			
			passwordField = new PasswordField("Password");
			passwordField.setWidth("100%");
			
			passwordAgainField = new PasswordField("Password again");
			passwordAgainField.setWidth("100%");
			
			saveButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					
					try {
						binder.writeBean(user);
					} catch (ValidationException e) {
						Notification.show("ERROR","Error in users",Type.ERROR_MESSAGE);
						return;
					}
					
					
					
					if( !passwordAgainField.getValue().equals(passwordField.getValue()) ) {
						Notification.show("Error", "Passwords do not match!", Type.ERROR_MESSAGE);
						return;
					}
					
					Notification.show("Sucess", "User saved", Type.WARNING_MESSAGE);
					
					registerUserService.save(username.getValue(), passwordField.getValue(),Role.Retailer);
					userLayoutFactory.userSaved();
				}
			});
			
			
			return this;
		}
		
		
		public Component layout() {
	
             FormLayout h1 = new FormLayout(username,passwordField,passwordAgainField,saveButton);
             h1.setWidth("50%");
             h1.setComponentAlignment(saveButton, Alignment.BOTTOM_CENTER);
             h1.setMargin(false);
             
			 return h1;
		}
		
		
		
		public AddUserLayout load() {
			// TODO Auto-generated method stub
			return this;
		}


		public AddUserLayout bind() {
		
			binder.forField(username)
			  .asRequired("username is required")
			  .bind("username");
			
			binder.forField(passwordField)
			  .asRequired("password is required")
			  .bind("password");
			
			binder.readBean(user);
			
			return this;
		}
			
	}
	
	
	@Autowired
	private RegisterUserService registerUserService;
	
	
	public Component createComponent(UserLayoutFactory userLayoutFactory) {
		return new AddUserLayout(userLayoutFactory).init().bind().layout();
	}
	
}
