package com.dataprice.ui.classification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class BrandMainLayout extends VerticalLayout implements View{

	@Autowired
	private BrandLayoutFactory brandLayoutFactory;
	
	private void addLayout() {
		setMargin(true);

		Component brandTab = brandLayoutFactory.createComponent();
		
		addComponent(brandTab);
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
