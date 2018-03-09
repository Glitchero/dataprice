package com.dataprice.ui.classification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.ui.tasks.AddTaskMainLayoutFactory;
import com.dataprice.ui.tasks.ShowAllTasksLayoutFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class ClassificationLayoutFactory extends VerticalLayout implements View{

	@Autowired
	private GenderLayoutFactory genderLayoutFactory;
	
	@Autowired
	private CategoryLayoutFactory categoryLayoutFactory;
	
	private TabSheet tabSheet;

	private void addLayout() {
		setMargin(true);
		tabSheet = new TabSheet();
		tabSheet.setWidth("100%");
		Component genderTab = genderLayoutFactory.createComponent();
		Component categoryTab = categoryLayoutFactory.createComponent();
		
		tabSheet.addTab(genderTab,"Generos");
		tabSheet.addTab(categoryTab,"Categorias");
			
		addComponent(tabSheet);
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
