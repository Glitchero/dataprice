package com.dataprice.ui.classification;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

@SpringView
public class CategoryMainLayout extends VerticalLayout implements View {

	@Autowired
	private CategoryLayoutFactory categoryLayoutFactory;
	


	private void addLayout() {
		setMargin(true);

		Component categoryTab = categoryLayoutFactory.createComponent();
		
		addComponent(categoryTab);
	}
	
	@PostConstruct
	void init() {
			removeAllComponents();
			addLayout();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}