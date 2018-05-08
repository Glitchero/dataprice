package com.dataprice.ui.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.User;
import com.dataprice.repository.security.UserRepository;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;


@org.springframework.stereotype.Component
public class ShowAllUserLayoutFactory {

	private List<User> users;
	private Grid<User> userTable;
	
	
	private class ShowAllUserLayout extends VerticalLayout{

		
		
		public ShowAllUserLayout init() {
			userTable = new Grid<>(User.class);
			
			userTable.removeAllColumns();
			userTable.addColumn(u -> u.getUsername()).setCaption("Nombre de Usuario");
			userTable.addColumn(u -> u.getRole()).setCaption("Rol del Usuario");

			userTable.setItems(users);
			userTable.setWidth("50%");
			
			
			return this;
		}
		
		
		public Component layout() {
			HorizontalLayout h1 = new HorizontalLayout(userTable);
			h1.setMargin(false);
			h1.setSizeFull();
			h1.setComponentAlignment(userTable, Alignment.MIDDLE_CENTER);
			return h1;
		}
		
		
		
		public ShowAllUserLayout load() {
			users = userRepository.getAllUsers();
			return this;
		}
		
		
		
		
	}
	
	@Autowired
	private UserRepository userRepository;
	
	public Component createComponent() {
		return new ShowAllUserLayout().load().init().layout();
	}


	public void refresh() {
		userTable.setItems(users);
	}
	
}
