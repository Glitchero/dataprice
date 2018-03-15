package com.dataprice.ui.tasks;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;
import com.dataprice.service.addproductservice.AddProductService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.crawltask.CrawlTaskServiceImpl;
import com.dataprice.service.modifytask.ModifyTaskServiceImpl;
import com.dataprice.service.removeproduct.RemoveProductService;
import com.dataprice.service.removetask.RemoveTaskService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.utils.NotificationsMessages;
import com.vaadin.annotations.Push;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ShowAllTasksLayoutFactory{

	private List<Task> tasks;
	
	private Grid<Task> tasksTable;
	
	private class ShowAllTasksLayout extends VerticalLayout implements Button.ClickListener{

		private Button removeTasksButton;
		private Button runTasksButton;
		private Button stopTasksButton;
		//private Thread thread;
		
		private TaskFinishedListener taskFinishedListener;
		public ShowAllTasksLayout(TaskFinishedListener taskFinishedListener) {
			this.taskFinishedListener = taskFinishedListener;
		}



		public ShowAllTasksLayout init() {
			removeTasksButton = new Button("Remove");
			
			runTasksButton = new Button("Run");
			runTasksButton.setWidth("100%");
			runTasksButton.addClickListener(this);
			runTasksButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			stopTasksButton =  new Button("Stop");
			stopTasksButton.setWidth("100%");
			
			stopTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			setMargin(true);
			tasksTable = new Grid<>(Task.class);
			tasksTable.setWidth("100%");
			tasksTable.setColumnOrder("taskName", "retail", "seed", "status");
			tasksTable.removeColumn("taskId");
			tasksTable.addColumn(t ->
		      "<a target=\"_blank\" href='" + t.getSeed() + "' target='_top'>product link</a>",
		      new HtmlRenderer());
			
			tasksTable.removeColumn("seed");
			
			tasksTable.setItems(tasks);
			tasksTable.setSelectionMode(SelectionMode.MULTI);
			removeTasksButton.addClickListener(this);
			removeTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			return this;
		}



		public ShowAllTasksLayout load() {
			tasks = showAllTasksService.getAllTasks();     			
			return this;
		}
		
		public ShowAllTasksLayout layout() {
			HorizontalLayout h1 = new HorizontalLayout(runTasksButton,stopTasksButton);
			h1.setWidth("100%");
			
			HorizontalLayout h2 = new HorizontalLayout(removeTasksButton);
			h2.setWidth("100%");
			
			HorizontalLayout h3 = new HorizontalLayout(h2,h1);
			h3.setWidth("100%");
			
			
			//h1.setComponentAlignment(runTasksButton, Alignment.BOTTOM_RIGHT);
			//h1.setComponentAlignment(stopTasksButton, Alignment.BOTTOM_RIGHT);
	
			addComponent(tasksTable); //Add component to the verticalLayout, That's why we extend the class.
			addComponent(h3);
			
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
			
			/**
			if (!thread.isAlive()) {
				System.out.println("Running tasks");
				thread = new Thread(new TaskRunner());
				thread.start();
			}else {
				System.out.println("Not Running tasks");
			}
			*/
			new Thread(new TaskRunner()).start();	
		}



		private void deleteTasks() {
            MultiSelectionModel<Task> selectionModel = (MultiSelectionModel<Task>) tasksTable.getSelectionModel();
			
			for(Task task : selectionModel.getSelectedItems()) {
				//System.out.println("EL tas es: " + task);
				removeProductService.removeAllProductsFromRetailName(task.getRetail());
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
	            //	CrawlTaskServiceImpl.getService(task.getRetail()).executeTask(task);
                    //Task Done
				    task.setStatus("Listo");
	            	modifyTaskService.modifyTask(task);
	            	taskFinishedListener.taskFinished();
	            	refreshTable();
				 }	

	        }
	        
	    }
	    
	    
		
	}
	
	@Autowired
	private RemoveProductService removeProductService;
	
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
	
	public Component createComponent(TaskFinishedListener taskFinishedListener) {
		return new ShowAllTasksLayout(taskFinishedListener).load().init().layout();
	}

	public void refreshTable() {
             tasks = showAllTasksService.getAllTasks();
             tasksTable.setItems(tasks);
	}

	
}
