package com.dataprice.ui.settings;

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
public class SettingsLayoutFactory extends VerticalLayout implements View,GenderSavedListener{

	@Autowired
	private AddGenderMainLayoutFactory addGenderLayoutFactory;
	
//	@Autowired
//	private ShowAllGendersLayoutFactory showGendersLayoutFactory;
	
	
	private TabSheet tabSheet;

	private void addLayout() {
		setMargin(true);
		tabSheet = new TabSheet();
		tabSheet.setWidth("100%");
		Component addStudentMainTab = addGenderLayoutFactory.createComponent(this);
	//	Component showStudentsTab = showGendersLayoutFactory.createComponent();
		
		addComponent(addStudentMainTab);
	//	addComponent(showStudentsTab);
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

	@Override
	public void genderSaved() {
		//showGendersLayoutFactory.refreshTable();
		
	}
	
}
