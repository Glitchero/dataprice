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
public class SettingsLayoutFactory extends VerticalLayout implements View{

	@Autowired
	private AddSettingsMainLayoutFactory addSettingsLayoutFactory;
	
	
	
	private TabSheet tabSheet;

	private void addLayout() {
	
		Component addStudentMainTab = addSettingsLayoutFactory.createComponent();
	//	Component showStudentsTab = showGendersLayoutFactory.createComponent();
		
		addComponent(addStudentMainTab);
	//	addComponent(showStudentsTab);
	}

	@PostConstruct
	void init() {

		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
	
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	
}
