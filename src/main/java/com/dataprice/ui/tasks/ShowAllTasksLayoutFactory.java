package com.dataprice.ui.tasks;


import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.crawlers.utils.Configuration;
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
import com.dataprice.ui.tasks.TaskExecuteOrder.ITaskIterator;
import com.dataprice.ui.tasks.TaskExecuteOrder.OriginalIterator;
import com.dataprice.ui.tasks.TaskExecuteOrder.RandomTaskIterator;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
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
		private TaskSetFinishedListener taskSetFinishedListener;
		public ShowAllTasksLayout(TaskFinishedListener taskFinishedListener, TaskSetFinishedListener taskSetFinishedListener) {
			this.taskFinishedListener = taskFinishedListener;
			this.taskSetFinishedListener = taskSetFinishedListener;
			
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
			
			stopTasksButton.addClickListener(this);
			
			setMargin(true);
			tasksTable = new Grid<>(Task.class);
			tasksTable.setWidth("100%");
			
			tasksTable.setColumnOrder("taskName", "retail", "seed", "progress", "runDateTime");

			tasksTable.addColumn(t ->
		      t.getProgress(),
		      new ProgressBarRenderer()).setCaption("Progreso");
			
			tasksTable.addColumn(t ->
		      t.getRunDateTime(),
		      new DateRenderer()).setCaption("Tiempo");

			tasksTable.addColumn(t ->
		      "<a target=\"_blank\" href='" + t.getSeed() + "' target='_top'>seed link</a>",
		      new HtmlRenderer()).setCaption("Seed Links");
			
			tasksTable.removeColumn("taskId");
			tasksTable.removeColumn("seed");
			tasksTable.removeColumn("progress");
			tasksTable.removeColumn("runDateTime");
			
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
            	if (event.getSource()==this.runTasksButton)	{
            		runTasks();
            	}else {
            		stopTasks();
            	}
            }			
		}



		private void runTasks() {
			
			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
				vaadinHybridMenuUI.startTasksExecution(new TasksExecutor());
			}else {
				Notification.show("CUIDADO","Los tasks se están ejecutando", Type.ERROR_MESSAGE);
			}
			     
		
		}

		private void stopTasks() {
			   
			
			if (vaadinHybridMenuUI.isTaskSetRunning()) {
				 vaadinHybridMenuUI.stopTasksExecution();
			}else {
				Notification.show("CUIDADO","Los tasks no se están ejecutando", Type.ERROR_MESSAGE);
			}
			
			
			  
			        
	}

		private void deleteTasks() {
			
			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
				MultiSelectionModel<Task> selectionModel = (MultiSelectionModel<Task>) tasksTable.getSelectionModel();
				
				for(Task task : selectionModel.getSelectedItems()) {
					//System.out.println("EL tas es: " + task);
					//removeProductService.removeAllProductsFromRetailName(task.getRetail());
					tasks.remove(task);
					removeTaskService.removeTask(task);
				}
				
				Notification.show("REMOVE","Tasks have been removed", Type.WARNING_MESSAGE);
				
				tasksTable.getDataProvider().refreshAll();
			}else {
				Notification.show("CUIDADO","No se permite eliminar durante la ejecución", Type.ERROR_MESSAGE);
			}
			
            
			
		}
		
		
	    private class TasksExecutor implements Runnable {

	        @Override
	        public void run() {
	        	List<Task> tasks = null;    
	        	ExecutorService executor = null;
	        	try {
	        		long startTime   = System.currentTimeMillis();
	                System.out.println("....run()::Extraction::starting");
	
	                tasks = showAllTasksService.getAllTasks();  
	                initialization(tasks);
	                
	                Task task = null;

	        		executor = Executors.newFixedThreadPool(Configuration.NUMBEROFCORES);		
	        		TaskExecute taskexecute = null;
	        		//ITaskIterator taskIterator = new OriginalIterator(tasks);
	        		ITaskIterator taskIterator = new RandomTaskIterator(tasks);
	        		
	                while ((task = taskIterator.getNextTask()) != null){
	                	   
	                	System.out.println("Starting task: " + task.toString());
	         		    taskexecute = new TaskExecute(task);
	         			executor.execute(taskexecute);
	         			/**
	         			if (Thread.currentThread().isInterrupted()) {
	         			     System.out.println("....run()::Extraction::TasksExecutor::isInterrupted():" + Thread.currentThread().isInterrupted());
	         			     Thread.sleep(500);
	         			}
	         			*/
	         			   	                    
	                }
	                   
	               executor.shutdown();
	           
	        		while (!executor.isTerminated())
	        		  {
	        			
	        			if (Thread.currentThread().isInterrupted()) {
	         			     System.out.println("....run()::Extraction::TasksExecutor::isInterrupted():" + Thread.currentThread().isInterrupted());
	         			     Thread.sleep(500);
	         			}
	        			//wait for all experiments
	        		  }
	        		
	                taskSetFinishedListener.taskSetFinished();
	                long endTime   = System.currentTimeMillis();
               	    long totalTime = endTime - startTime;
               	    System.out.println("Total time execution: " + totalTime);
	                System.out.println("....run()::Extraction::ended");
	                
	            } catch (InterruptedException x) {	  
	            	executor.shutdownNow();
	                System.out.println("....run()::TasksExecutor::CANCELED::INTERRUPTED::THREADS::READY::TO::DIE");
	                
	                return;
	                
	            } catch (Exception ex) {
	            	ex.printStackTrace();
	            	executor.shutdownNow();
	                System.out.println("....run()::Extraction::ERROR::FATAL");
	                return;
	                
	            }   
	            System.out.println("....run()::Extraction::leaving normally");  
	        }
	        
			public void initialization(List<Task> tasks) {
	                for(Task task : tasks) {
	            	setTaskProgress(task,0.0);
	            	setTodayDateAndExecutionTime(task,(long) 0);	
	            	setTaskStatus(task,"Pendiente a descarga");
	            } 
            }	

	    }    
	    
	    
	    /**
	     * Execution of a single task
	     * @author rene
	     *
	     */
	    
	    private class TaskExecute implements Runnable  {

	    	private Task task;
	    	
			public TaskExecute(Task task) {
				this.task = task;
			}

			@Override
			public void run() {
				try {
				 long startTime = System.currentTimeMillis();
             	
                 List<String> productsUrl = crawling(task);
                 	                    
                 if (productsUrl.size()!=0) {
                 
                 	int downloadedProducts = scraping(task,productsUrl);
                 	
                 	if (downloadedProducts!=0) {
                 		
                 		
                   	    long endTime   = System.currentTimeMillis();
                   	    long totalTime = endTime - startTime;
                  		setTodayDateAndExecutionTime(task,totalTime);
                  		setTaskStatus(task,"Productos descargados: " + downloadedProducts);  
       	                
               	    }else {
               	    	setTaskStatus(task,"Error fatál en descarga" );
               	    	setTaskProgress(task,0.0);
               	   }
                
                 }  else {       	   
                 	setTaskStatus(task,"Error fatál en escaneo");
                 	setTaskProgress(task,0.0);
	            	}
                 
                } catch (InterruptedException x) {
	            	
	                System.out.println("....run()::Extraction::CANCELED::INTERRUPTED");
	                return;
	            }
				
			}

			public List<String> crawling(Task task) throws InterruptedException {
			    
			    setTaskStatus(task,"Escaneando productos");
			    //System.out.println("Task nombreeee " + task.toString());
        	    List<String> productsUrl = CrawlTaskServiceImpl.getService(task.getRetail()).getUrlsFromTask(task);
        	    //System.out.println("Tamaño del array " + productsUrl.size());
        	    if (productsUrl==null)
        	    	Thread.currentThread().interrupt();
        	    
        	    if (Thread.currentThread().isInterrupted()) {
                    System.out.println("....run()::Extraction::Crawling::isInterrupted():" + Thread.currentThread().isInterrupted());
                    return new LinkedList<String>();
                } 
              
				return productsUrl;    
        }
		
		
		public int scraping(Task task, List<String> productsUrl) throws InterruptedException {
			 
			int downloadedProducts = 0;
    		int errorProducts = 0;

    		setTaskStatus(task,"Descargando productos");
    		
    		for (int i = 0; i<productsUrl.size(); i++) {
    			
    		   String url = productsUrl.get(i);
    		 
    		   Product p = CrawlTaskServiceImpl.getService(task.getRetail()).parseProductFromURL(url, task);
    		
    		   if (p==null)
    			   Thread.currentThread().interrupt();  
    		   
    		   
    		   if (Thread.currentThread().isInterrupted()) {
                System.out.println("....run()::Extraction::Scraper::isInterrupted():" + Thread.currentThread().isInterrupted());
                return 0;
               } 
    		   
    		   if (!p.getProductId().equals("")) {
    			   downloadedProducts++;
    			   addProductService.saveProduct(p);
    			   setTaskProgress(task, (double) (i + 1)/ (double) productsUrl.size());
    		   }else {
    			   errorProducts++;
    			   setTaskProgress(task, (double) (i + 1)/ (double) productsUrl.size());
    		   }
    		   
    		   
    	     }
             
    	    System.out.println("Total de Errores: " + errorProducts );
        
			return downloadedProducts;

        }	
	    	
	}
	    
	    
	    
	    
	    private void setTaskStatus(Task task,String status) {
	    	task.setStatus(status);
        	modifyTaskService.modifyTask(task);
        	refreshTable();
	    }
	    
	    private void setTaskProgress(Task task,Double progress) {
	    	task.setProgress(progress);
        	modifyTaskService.modifyTask(task);
        	refreshTable();
	    }
	    
	    private void setTodayDateAndExecutionTime(Task task, Long totalTime) {
	    	Calendar today = Calendar.getInstance();
     		  
    	    int seconds = (int) (totalTime / 1000) % 60 ;
    		int minutes = (int) ((totalTime / (1000*60)) % 60);
    		int hours   = (int) ((totalTime / (1000*60*60)) % 24);
    		
    		today.set(Calendar.HOUR_OF_DAY, hours);		           		
    		today.set(Calendar.MINUTE, minutes);     		
    		today.set(Calendar.SECOND, seconds);
    		
    		Date date = today.getTime();
	    	
	    	task.setRunDateTime(date);
        	modifyTaskService.modifyTask(task);
        	refreshTable();
	    }
		
	}
	
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
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
	
	public Component createComponent(TaskFinishedListener taskFinishedListener,TaskSetFinishedListener taskSetFinishedListener) {
		return new ShowAllTasksLayout(taskFinishedListener,taskSetFinishedListener).load().init().layout();
	}

	public void refreshTable() {
             tasks = showAllTasksService.getAllTasks();
        
             vaadinHybridMenuUI.access(new Runnable() {     
            	   @Override     
            	   public void run() {         
            		   tasksTable.setItems(tasks); 
            	   }
            	});
             
            
	}

	
}
