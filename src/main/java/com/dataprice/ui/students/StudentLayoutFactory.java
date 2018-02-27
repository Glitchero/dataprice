package com.dataprice.ui.students;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class StudentLayoutFactory extends VerticalLayout implements View, StudentSavedListener{

	@Autowired
	private AddStudentMainLayoutFactory mainLayoutFactory;
	
	@Autowired
	private ShowAllStudentsLayoutFactory showStudentsLayoutFactory;
	
	private TabSheet tabSheet;
	
	private void addLayout() {
		setMargin(true);
		tabSheet = new TabSheet();
		tabSheet.setWidth("100%");
		Component addStudentMainTab = mainLayoutFactory.createComponent(this);
		Component showStudentsTab = showStudentsLayoutFactory.createComponent();
	 //	tabSheet.addTab(addStudentMainTab,StudentsStringUtils.MAIN_MENU.getString());
	//	tabSheet.addTab(showStudentsTab,StudentsStringUtils.SHOW_ALL_STUDENTS.getString());
		
	//	addComponent(tabSheet);
		addComponent(addStudentMainTab);
		addComponent(showStudentsTab);
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
	public void studentSaved() {
		showStudentsLayoutFactory.refreshTable();
		
	}
	
}