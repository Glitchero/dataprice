package com.dataprice.ui.reports;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class ReportsLayoutFactory extends VerticalLayout implements View{

	@PostConstruct
	void init() {
		//removeAllComponents();
		//addLayout();
		Label title = new Label();
		title.setCaption("Reports");
		title.setValue("Reports section");
		addComponent(title);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
	
}
