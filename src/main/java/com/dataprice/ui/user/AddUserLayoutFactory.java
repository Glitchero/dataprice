package com.dataprice.ui.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.FeedSettings;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Role;
import com.dataprice.model.entity.User;
import com.dataprice.repository.security.UserRepository;
import com.dataprice.service.security.RegisterUserService;
import com.dataprice.utils.StringUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class AddUserLayoutFactory {

	
	private class AddUserLayout extends VerticalLayout implements SelectionListener<User>{

		private UserLayoutFactory userLayoutFactory;
	//	private TextField username;
		private PasswordField passwordField;
		private PasswordField passwordAgainField;
		private Button saveButton;
		private User user;
		private Binder<User> binder;
		private Grid<User> userTable;
		private List<User> users;
	//	private CheckBox checkbox1;
		
		public AddUserLayout(UserLayoutFactory userLayoutFactory) {
			this.userLayoutFactory = userLayoutFactory;
		}


		public AddUserLayout init() {
			binder = new Binder<>(User.class);
			user =  new User();
			
		//	checkbox1 = new CheckBox("Admin");
			
			saveButton = new Button(StringUtils.USER_CHANGE_PASSWORD.getString());
			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			saveButton.setWidth("100%");
			
	//		username = new TextField("Username");
	//		username.setWidth("100%");
	//		username.setCaption("admin");
	//		username.setEnabled(false);
			
			passwordField = new PasswordField(StringUtils.USER_PASSWORD.getString());
			passwordField.setWidth("100%");
			
			passwordAgainField = new PasswordField(StringUtils.USER_NEW_PASSWORD.getString());
			passwordAgainField.setWidth("100%");
			
			saveButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					
					try {
						binder.writeBean(user);
					} catch (ValidationException e) {
						Notification.show(StringUtils.ERROR.getString(),StringUtils.ERROR.getString(),Type.ERROR_MESSAGE);
						return;
					}
					
					
					
					if( !passwordAgainField.getValue().equals(passwordField.getValue()) ) {
						Notification.show(StringUtils.ERROR.getString(), StringUtils.USER_DO_NOT_MATCH.getString(), Type.ERROR_MESSAGE);
						return;
					}
					
					Notification.show(StringUtils.SUCESS.getString(), StringUtils.USER_SUCESS.getString(), Type.WARNING_MESSAGE);
				//	if (checkbox1.getValue()==true) {
						registerUserService.save("admin", passwordField.getValue(),Role.Admin);
				//	}else {
				//		registerUserService.save(username.getValue(), passwordField.getValue(),Role.Retailer);
				//	}
					userLayoutFactory.userSaved();
					refresh();
				}
			});
			
			userTable = new Grid<>(User.class);
			
			userTable.removeAllColumns();
			userTable.addColumn(u -> u.getUsername()).setCaption(StringUtils.USER_USER.getString());
			userTable.addColumn(u -> u.getRole()).setCaption(StringUtils.USER_ROLE.getString());

			userTable.setItems(users);
			userTable.setWidth("50%");
			userTable.setHeight("80px");
			userTable.addSelectionListener(this);
			
			return this;
		}
		
		
		@Override
		public void selectionChange(SelectionEvent<User> event) {
			/**
			 SingleSelectionModel<User> singleSelect = (SingleSelectionModel<User>) userTable.getSelectionModel();
			 if (singleSelect.getSelectedItem().isPresent()) {
				 username.setValue(singleSelect.getSelectedItem().get().getUsername());
				 passwordField.setValue("");
				 passwordAgainField.setValue("");	
				 if (singleSelect.getSelectedItem().get().getRole().toString().equals("Admin"))  {
					 checkbox1.setValue(true);
				 }else {
					 checkbox1.setValue(false);
				 }				 
			 }
			 */				 
		}
		
		
		public Component layout() {
	         HorizontalLayout h2 = new HorizontalLayout();
	         
             FormLayout h1 = new FormLayout(passwordField,passwordAgainField,saveButton);
          // h1.setWidth("50%");
             
             h1.setComponentAlignment(saveButton, Alignment.BOTTOM_CENTER);
             h1.setMargin(false);
             h2.addComponent(h1);
             h2.addComponent(userTable);
			 return h2;
		}
		
		
		
		public AddUserLayout load() {
			users = userRepository.getAllUsers();
			return this;
		}


		public AddUserLayout bind() {
		
		//	binder.forField(username)
		//	  .asRequired("username is required")
		//	  .bind("username");
			
			binder.forField(passwordField)
			  .asRequired("password is required")
			  .bind("password");
			
			binder.readBean(user);
			
			return this;
		}
		
		public void refresh() {
			users = userRepository.getAllUsers();
			userTable.setItems(users);
		}
			
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RegisterUserService registerUserService;
	
	
	public Component createComponent(UserLayoutFactory userLayoutFactory) {
		return new AddUserLayout(userLayoutFactory).load().init().bind().layout();
	}
	
	
	
}
