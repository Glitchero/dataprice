package com.dataprice.ui.products;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.ui.tasks.ShowAllTasksLayoutFactory;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class ProductLayoutFactory extends VerticalLayout implements View{

	@Autowired
	private ShowAllProductsLayoutFactory showProductsLayoutFactory;
	
	@PostConstruct
	void init() {
		removeAllComponents();
		addLayout();
	}

	private void addLayout() {
		setMargin(true);
		Component showProducts = showProductsLayoutFactory.createComponent();
		addComponent(showProducts);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
	
}
