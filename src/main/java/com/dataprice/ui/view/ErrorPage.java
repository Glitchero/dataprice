package com.dataprice.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@SpringView
public class ErrorPage extends VerticalLayout implements View {

	@PostConstruct
	void init() {
		addComponent(new Label("This is the error view"));
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}
