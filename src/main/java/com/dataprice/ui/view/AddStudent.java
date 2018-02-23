package com.dataprice.ui.view;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class AddStudent extends VerticalLayout implements View{

	@PostConstruct
	void init() {
		Label title = new Label();
		title.setCaption("AddStudent");
		title.setValue("Add Student working");
		addComponent(title);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
	
}
