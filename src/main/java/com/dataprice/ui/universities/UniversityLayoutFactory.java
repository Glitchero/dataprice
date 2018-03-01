package com.dataprice.ui.universities;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class UniversityLayoutFactory extends VerticalLayout implements View,UniversitySavedListener{

	private TabSheet tabSheet;
	
	@Autowired
	private AddUniversityLayoutFactory addUniversityLayoutFactory;
	
	@Autowired
	private ShowUniversitiesLayoutFactory showUniversitiesLayoutFactory;
	
	
	@PostConstruct
	void init() {
		removeAllComponents();
		addLayout();
	}

	private void addLayout() {
		 setMargin(true);
		 tabSheet = new TabSheet();
		 tabSheet.setWidth("100%");
		 
		 Component addUniversityTab = addUniversityLayoutFactory.createComponent(this);
		 Component showAllUniversitiesTab = showUniversitiesLayoutFactory.createComponent();
		 Component showStatisticsTab = new Label("Statistics");
		 
		 tabSheet.addTab(addUniversityTab,"ADD");
		 tabSheet.addTab(showAllUniversitiesTab,"SHOW ALL");
		 tabSheet.addTab(showStatisticsTab,"STATISTICS");
		 
		 addComponent(tabSheet);
	}

	@Override
	public void universitySaved() {
		showUniversitiesLayoutFactory.refreshTable();
		
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	
	
}
