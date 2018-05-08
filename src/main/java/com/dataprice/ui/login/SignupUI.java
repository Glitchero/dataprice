package com.dataprice.ui.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SpringUI(path=SignupUI.PATH)
@Theme("valo")
public class SignupUI extends UI {
	
	public static final String PATH = "/signup";

	@Autowired
	private SignupFormFactory signupFormFactory;
	
	@Override
	protected void init(VaadinRequest request) {
	  	//setContent(signupFormFactory.createComponent());
	  	setContent(new Label("Opps, regrese a la página de inicio"));
	}
}