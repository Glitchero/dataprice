package com.dataprice.ui.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class UserLayoutFactory extends VerticalLayout implements View,SaveUserListener{

	@Autowired
	private AddUserLayoutFactory addUserLayoutFactory;
	
	
	@Autowired
	private ShowAllUserLayoutFactory showAllUserLayoutFactory;
	
	
	@PostConstruct
	void init() {

		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
	
	}
	
	private void addLayout() {
		Component addUser = addUserLayoutFactory.createComponent(this);
				
	    Component showUser = showAllUserLayoutFactory.createComponent();
		
	    HorizontalLayout h1 = new HorizontalLayout(addUser,showUser);
	    h1.setSizeFull();
	    h1.setComponentAlignment(addUser, Alignment.MIDDLE_CENTER);
	    h1.setComponentAlignment(showUser, Alignment.MIDDLE_CENTER);
	    h1.setMargin(false);
	    
	    Label mainTittle = new Label("<b><font size=\"5\">Administre los usuarios </font></b>",ContentMode.HTML);	

	    Label subTittle = new Label("<font size=\"2\">Agregue usuarios de tipo retailers al sistema </font>",ContentMode.HTML);	
		
	    addComponent(mainTittle);
	    addComponent(subTittle);
		addComponent(h1);
	  //  addComponent(showUser);
	    
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	@Override
	public void userSaved() {
		showAllUserLayoutFactory.refresh();
		
	}

	
}
