package com.dataprice.ui.tasks;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Student;
import com.dataprice.model.entity.Task;
import com.dataprice.service.addproductservice.AddProductService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.crawltask.CrawlTaskServiceImpl;
import com.dataprice.service.modifytask.ModifyTaskServiceImpl;
import com.dataprice.service.removestudent.RemoveStudentService;
import com.dataprice.service.removetask.RemoveTaskService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.students.UIComponentBuilder;
import com.dataprice.utils.NotificationsMessages;
import com.vaadin.annotations.Push;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ShowAllTasksLayoutFactory implements UIComponentBuilder{

	private List<Task> tasks;
	
	private Grid<Task> tasksTable;
	
	private class ShowAllTasksLayout extends VerticalLayout implements Button.ClickListener{

		private Button removeTasksButton;
		private Button runTasksButton;
		
		public ShowAllTasksLayout init() {
			removeTasksButton = new Button("Remove");
			runTasksButton = new Button("Run");
			setMargin(true);
			tasksTable = new Grid<>(Task.class);
			tasksTable.setWidth("100%");
			tasksTable.setColumnOrder("taskName", "retail", "seed", "status");
			tasksTable.removeColumn("taskId");
			tasksTable.setItems(tasks);
			tasksTable.setSelectionMode(SelectionMode.MULTI);
			removeTasksButton.addClickListener(this);
			removeTasksButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			runTasksButton.addClickListener(this);
			runTasksButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			return this;
		}



		public ShowAllTasksLayout load() {
			tasks = showAllTasksService.getAllTasks();     			
			return this;
		}
		
		public ShowAllTasksLayout layout() {
			addComponent(tasksTable); //Add component to the verticalLayout, That's why we extend the class.
			addComponent(removeTasksButton);
			addComponent(runTasksButton);
			return this;
		}



		@Override
		public void buttonClick(ClickEvent event) {
			
            if (event.getSource()==this.removeTasksButton)	{
           	 deleteTasks();
            }else {
           	 runTasks();
            }			
		}



		private void runTasks() {
			System.out.println("Running tasks");
			new Thread(new TaskRunner()).start();	
		}



		private void deleteTasks() {
            MultiSelectionModel<Task> selectionModel = (MultiSelectionModel<Task>) tasksTable.getSelectionModel();
			
			for(Task task : selectionModel.getSelectedItems()) {
				//System.out.println("EL tas es: " + task);
				tasks.remove(task);
				removeTaskService.removeTask(task);
			}
			
			Notification.show("REMOVE","Tasks have been removed", Type.WARNING_MESSAGE);
			
			tasksTable.getDataProvider().refreshAll();
			
		}
		
		
	    private class TaskRunner implements Runnable {

	        @Override
	        public void run() {
             
	            tasks = showAllTasksService.getAllTasks(); 
         
	            for(Task task : tasks) {
	            	//Preparing task
	            	task.setStatus("procesando");
	            	modifyTaskService.modifyTask(task);
	            	refreshTable();
	            	//Download Products
	            	List<Product> products = CrawlTaskServiceImpl.getService(task.getRetail()).getProductsFromTask(task);
	            	for (Product p : products) {
	            		System.out.println(p);
	            		addProductService.saveProduct(p);
	            	}
                    //Task Done
				    task.setStatus("Listo");
	            	modifyTaskService.modifyTask(task);
	            	refreshTable();
				 }	

	        }
	        
	    }
	    
	    
		
	}
	
	@Autowired
	private AddProductService addProductService;
	
	
	@Autowired
	private ModifyTaskServiceImpl modifyTaskService;
	
	@Autowired
	private ShowAllTasksService showAllTasksService;
	
	@Autowired
	private RemoveTaskService removeTaskService;
	
	@Autowired
    private AddTaskService addtaskService;
	
	@Autowired
    private CrawlTaskServiceImpl CrawlTaskServiceImpl;
	
	public Component createComponent() {
		return new ShowAllTasksLayout().load().init().layout();
	}

	public void refreshTable() {
             tasks = showAllTasksService.getAllTasks();
             tasksTable.setItems(tasks);
	}

	
}
