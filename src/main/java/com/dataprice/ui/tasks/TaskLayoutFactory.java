package com.dataprice.ui.tasks;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.products.ShowAllProductsLayoutFactory;
import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class TaskLayoutFactory extends VerticalLayout implements View, TaskSavedListener,TaskFinishedListener,TaskSetFinishedListener {

	@Autowired
	private AddTaskMainLayoutFactory mainLayoutFactory;
	
	@Autowired
	private ShowAllTasksLayoutFactory showTasksLayoutFactory;
	
	@Autowired
	private ShowAllProductsLayoutFactory showAllProductsLayoutFactory;
	
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	private TabSheet tabSheet;
	
	private void addLayout() {
		
		
		Component addStudentMainTab = mainLayoutFactory.createComponent(this);
		Component showStudentsTab = showTasksLayoutFactory.createComponent(this,this);
	 //	tabSheet.addTab(addStudentMainTab,StudentsStringUtils.MAIN_MENU.getString());
	//	tabSheet.addTab(showStudentsTab,StudentsStringUtils.SHOW_ALL_STUDENTS.getString());
		
	//	addComponent(tabSheet);
		addComponent(addStudentMainTab);
		addComponent(showStudentsTab);
		
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


	@Override
	public void taskSaved() {
		showTasksLayoutFactory.refreshTable();
		
	}


	@Override
	public void taskFinished() {
		showAllProductsLayoutFactory.refresh();	
	}


	@Override
	public void taskSetFinished() {
		vaadinHybridMenuUI.finishTasksExecution();	
	}
	
}
