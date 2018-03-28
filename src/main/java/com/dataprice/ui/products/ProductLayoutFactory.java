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
public class ProductLayoutFactory extends VerticalLayout implements View,ProductEditListener,ProductSaveListener{

	@Autowired
	private ShowAllProductsLayoutFactory showProductsLayoutFactory;
	
	@Autowired
	private EditProductLayoutFactory editProductLayoutFactory;
	
	
	@PostConstruct
	void init() {
		removeAllComponents();
		addLayout();
	}

	private void addLayout() {
		//setMargin(true);
		Component showProducts = showProductsLayoutFactory.createComponent(this);
		Component editProducts = editProductLayoutFactory.createComponent(this);
		showProducts.setSizeFull();
		addComponent(showProducts);
		addComponent(editProducts);
		
	
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}


	@Override
	public void productEdited(Object item) {
		editProductLayoutFactory.editData(item);
		
	}

	@Override
	public void productSaved() {
		showProductsLayoutFactory.refresh();
		
	}


}
