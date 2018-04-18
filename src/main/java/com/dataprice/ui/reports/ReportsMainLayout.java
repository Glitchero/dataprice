package com.dataprice.ui.reports;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class ReportsMainLayout extends VerticalLayout implements View{
	
	@Autowired
	private ReportsLayoutFactory reportsLayoutFactory;
	
	@PostConstruct
	void init() {
		
	//	Label title = new Label();
	//	title.setCaption("Reports");
	//	title.setValue("Reports section");
	//	addComponent(title);
		
        Component reportTab = reportsLayoutFactory.createComponent();
		addComponent(reportTab);
		
		
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
	
}