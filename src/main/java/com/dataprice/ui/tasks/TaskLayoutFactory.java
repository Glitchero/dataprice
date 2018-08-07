package com.dataprice.ui.tasks;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.products.ShowAllProductsLayoutFactory;
import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
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
	private Label mainTittle;
    private Label subTittle;
	
	private void addLayout() {
		
		mainTittle = new Label("<b><font size=\"5\">Administrador de Bots </font></b>",ContentMode.HTML);	
		subTittle = new Label("<font size=\"2\">Agregue, ejecute y elimine bots. </font>",ContentMode.HTML);	
		
		tabSheet = new TabSheet();
		Component addTasks = mainLayoutFactory.createComponent(this);
		Component showTasks = showTasksLayoutFactory.createComponent(this,this);
	
		
	 	tabSheet.addTab(addTasks,"Agrega Bots");
		tabSheet.addTab(showTasks,"Visualizar Bots");
		
		addComponent(mainTittle);
		addComponent(subTittle);
	//	addComponent(addTasks);
	//	addComponent(showTasks);
		addComponent(tabSheet);
		
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
	//	vaadinHybridMenuUI.finishTasksExecution();	
	}
	
}
