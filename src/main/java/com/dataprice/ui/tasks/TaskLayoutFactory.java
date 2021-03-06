package com.dataprice.ui.tasks;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.dashboard.HeaderLayoutFactory;
import com.dataprice.ui.products.ShowAllProductsLayoutFactory;
import com.dataprice.utils.StringUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
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
	private HeaderLayoutFactory headerLayoutFactory;
	
	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	private TabSheet tabSheet;
	private Label mainTittle;
    private Label subTittle;

	
	private void addLayout() {
		
		
    	
		mainTittle = new Label("<b><font size=\"5\">"+ StringUtils.TASKS_TITLE.getString()+"</font></b>",ContentMode.HTML);	
		subTittle = new Label("<font size=\"2\">"+ StringUtils.TASKS_SUBTITLE.getString() + "</font>",ContentMode.HTML);	
		
		
		
		tabSheet = new TabSheet();
		this.setMargin(false);
		Component addTasks = mainLayoutFactory.createComponent(this);
		Component showTasks = showTasksLayoutFactory.createComponent(this,this);
	
		
	 	tabSheet.addTab(addTasks,StringUtils.TASKS_ADD_TASKS.getString());
		tabSheet.addTab(showTasks,StringUtils.TASKS_SHOW_TASKS.getString());
        
        
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
