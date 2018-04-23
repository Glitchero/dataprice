package com.dataprice.ui.tasks;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

@SpringView
public class TaskExportImportLayoutFactory extends VerticalLayout implements View {

	@Autowired
	private TaskExportImportFactory taskExportImportFactory;
	
	@PostConstruct
	void init() {
		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
	}

	private void addLayout() {
		Component exportImport = taskExportImportFactory.createComponent();
		addComponent(exportImport);
		
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}

