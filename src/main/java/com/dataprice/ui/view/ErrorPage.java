package com.dataprice.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;

@SpringView
@Scope("prototype")
public class ErrorPage extends VerticalLayout implements View {

	@PostConstruct
	void init() {
		addComponent(new Label("Opps, es esta vista no hay contenido"));
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}
