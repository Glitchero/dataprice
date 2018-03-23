package com.dataprice.ui.classification;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

@SpringView
public class GenderMainLayout extends VerticalLayout implements View {

	@Autowired
	private GenderLayoutFactory genderLayoutFactory;
	


	private void addLayout() {
		setMargin(true);

		Component genderTab = genderLayoutFactory.createComponent();
		addComponent(genderTab);
		
	}
	
	@PostConstruct
	void init() {
			removeAllComponents();
			//this.setHeight("100%");
			addLayout();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}