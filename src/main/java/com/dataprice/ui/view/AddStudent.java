package com.dataprice.ui.view;

import javax.annotation.PostConstruct;

import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class AddStudent extends VerticalLayout implements View{

	private TabSheet tabSheet;
	private void addLayout() {
		setMargin(true);
		tabSheet = new TabSheet();
		tabSheet.setWidth("100%");
		Component addStudentMainTab = new Label("First Tab content");
		Component showStudentsTab = new Label("Show students tabs");
		tabSheet.addTab(addStudentMainTab,StudentsStringUtils.MAIN_MENU.getString());
		tabSheet.addTab(showStudentsTab,StudentsStringUtils.SHOW_ALL_STUDENTS.getString());
		
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
