package com.dataprice.ui.tasks;


import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
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
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ProductHistory;
import com.dataprice.model.entity.ProductHistoryKey;
import com.dataprice.model.entity.Retail;
import com.dataprice.model.entity.Task;
import com.dataprice.service.addproducthistservice.AddProductHistService;
import com.dataprice.service.addproductservice.AddProductService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.crawltask.CrawlTaskServiceImpl;
import com.dataprice.service.modifytask.ModifyTaskServiceImpl;
import com.dataprice.service.removeproduct.RemoveProductService;
import com.dataprice.service.removetask.RemoveTaskService;
import com.dataprice.service.showallretails.ShowAllRetailsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.products.Icon;
import com.dataprice.ui.tasks.TaskExecuteOrder.ITaskIterator;
import com.dataprice.ui.tasks.TaskExecuteOrder.OriginalIterator;
import com.dataprice.ui.tasks.TaskExecuteOrder.RandomTaskIterator;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.utils.NotificationsMessages;
import com.vaadin.annotations.Push;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ShowAllTasksLayoutFactory{

	private List<Task> tasks;
	
	private Grid<Task> tasksTable;
	
	private ComboBox retailFilter;
	
	private ComboBox taskNameFilter;
	
	private List<Retail> retailers;
	
	
	private class ShowAllTasksLayout extends VerticalLayout implements Button.ClickListener,ValueChangeListener{

		private Button removeTasksButton;
		private Button reportButton;
		private Button runTasksButton;
		private Button stopTasksButton;
		//private Thread thread;
		
		
		private ProgressBar progressBar;  //Tell us to wait for cancell!
		
		private TaskFinishedListener taskFinishedListener;
		private TaskSetFinishedListener taskSetFinishedListener;
		public ShowAllTasksLayout(TaskFinishedListener taskFinishedListener, TaskSetFinishedListener taskSetFinishedListener) {
			this.taskFinishedListener = taskFinishedListener;
			this.taskSetFinishedListener = taskSetFinishedListener;
			
		}



		public ShowAllTasksLayout init() {
			
			
			
			progressBar = new ProgressBar();
			progressBar.setCaption("Cancelling...");
	        progressBar.setIndeterminate(true);
	        progressBar.setVisible(false);
	        
	        
			removeTasksButton = new Button("Eliminar");
			removeTasksButton.addClickListener(this);
			removeTasksButton.setIcon(VaadinIcons.TRASH);
			removeTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);
			removeTasksButton.setWidth("100%");
			
			reportButton = new Button("Reportar");
			reportButton.setIcon(VaadinIcons.TOOLS);
			reportButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			reportButton.setWidth("100%");
			
			runTasksButton = new Button("Correr");
			runTasksButton.setWidth("100%");
			runTasksButton.addClickListener(this);
			runTasksButton.setIcon(VaadinIcons.PLAY_CIRCLE);
			runTasksButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			
			retailFilter = new ComboBox();
			retailFilter.setStyleName(ValoTheme.COMBOBOX_SMALL);
			retailFilter.setItems(retailers);
			retailFilter.addValueChangeListener(this);
			
			
			taskNameFilter = new ComboBox();
			taskNameFilter.setStyleName(ValoTheme.COMBOBOX_SMALL);
		//	taskNameFilter.setItems(taskNames); //Create query!!.
		//	taskNameFilter.addValueChangeListener(this);
			
			
			stopTasksButton =  new Button("Cancelar");
			stopTasksButton.setWidth("100%");
			stopTasksButton.setIcon(VaadinIcons.STOP);
			stopTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);
			stopTasksButton.addClickListener(this);
			
			tasksTable = new Grid<>(Task.class);
			tasksTable.setWidth("100%");
			
			tasksTable.setColumnOrder("taskName", "retail", "seed", "progress", "runDateTime");

			tasksTable.addColumn(t ->
		      t.getProgress(),
		      new ProgressBarRenderer()).setCaption("Progreso");
			
		//	tasksTable.addColumn(t ->
		//      t.getStatus()).setCaption("Estatus");
			
			
			tasksTable.addComponentColumn(t -> {
				Icon icon = null;
	
				 switch (t.getStatus()) {
		         case "Pendiente":
		        	 icon = new Icon(VaadinIcons.CLOCK);
		             break;
		         case "Escaneando":
		        	 icon = new Icon(VaadinIcons.FILE_SEARCH);
		             break;
		         case "Descargando":
		        	 icon = new Icon(VaadinIcons.DOWNLOAD);
		             break;
		         case "Cancelado":
		        	 icon = new Icon(VaadinIcons.CLOSE_CIRCLE);
		        	 icon.setRedColor();
		             break;
		         case "Finalizado":
		        	 icon = new Icon(VaadinIcons.CHECK_CIRCLE);
		             break;
		         }
				HorizontalLayout h = new HorizontalLayout(icon);
				h.setComponentAlignment(icon, Alignment.MIDDLE_CENTER);
				h.setMargin(false);
				h.setWidth("100%");
				return h;
			}).setCaption("Estatus").setWidth(90);
			
			
			tasksTable.addColumn(t ->
		       t.getDownloadedProducts()).setCaption("Num. de Productos").setWidth(162);
			
			tasksTable.addColumn(t ->
		      t.getRunDateTime(),
		      new DateRenderer()).setCaption("Tiempo");

			tasksTable.addColumn(t ->
		      "<a target=\"_blank\" href='" + t.getSeed() + "' target='_top'>Semilla</a>",
		      new HtmlRenderer()).setCaption("Semillas");
					

			tasksTable.removeColumn("taskId");
			tasksTable.removeColumn("seed");
			tasksTable.removeColumn("progress");
			tasksTable.removeColumn("runDateTime");
			tasksTable.removeColumn("downloadedProducts");
			tasksTable.removeColumn("status");
			
			tasksTable.setItems(tasks);
			tasksTable.setSelectionMode(SelectionMode.MULTI);
			tasksTable.setSizeFull();
			

			
			return this;
		}



		public ShowAllTasksLayout load() {
			tasks = showAllTasksService.getAllTasks();    
			retailers = showAllRetailsService.getAllRetailers();
			return this;
		}
		
		
		
		public Component layout() {
	
			HeaderRow HeaderRow = tasksTable.prependHeaderRow();
			HeaderRow.getCell("retail").setComponent(retailFilter);
			HeaderRow.getCell("taskName").setComponent(taskNameFilter);
			//Remove all buttons when progress bar is activated.
			HorizontalLayout hbutton = new HorizontalLayout(runTasksButton,stopTasksButton,removeTasksButton,reportButton,progressBar);
			hbutton.setMargin(false);
			hbutton.setWidth("50%");

			VerticalLayout v1 = new VerticalLayout(hbutton,tasksTable);
			v1.setComponentAlignment(hbutton, Alignment.TOP_RIGHT);
            v1.setWidth("100%");
            v1.setMargin(false);

			return v1;
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



		private void stopTasks() {
			if (vaadinHybridMenuUI.isTaskSetRunning()) {
				 stopTasksButton.setVisible(false);
				 progressBar.setVisible(true); // give the user some visual hint about cancelling taking place
				 vaadinHybridMenuUI.stopTasksExecution();
			}else {
				Notification.show("CUIDADO","Los tasks no se están ejecutando", Type.ERROR_MESSAGE);
			}  			        
	     }
		
			
		/**
		 * Should be nice to have this for the remove because it takes time
		 * https://github.com/alejandro-du/large-dataset-example/blob/master/src/main/java/com/example/MyUI.java
		 */
		private void deleteTasks() {
			/**
			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
				MultiSelectionModel<Task> selectionModel = (MultiSelectionModel<Task>) tasksTable.getSelectionModel();
				
				for(Task task : selectionModel.getSelectedItems()) {
					tasks.remove(task);
					removeTaskService.removeTask(task);
				}
				
				Notification.show("REMOVE","Tasks have been removed", Type.WARNING_MESSAGE);
				
				tasksTable.getDataProvider().refreshAll();
				tasksTable.deselectAll();
			}else {
				Notification.show("CUIDADO","No se permite eliminar durante la ejecución", Type.ERROR_MESSAGE);
			}   
			*/
			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
			     if (tasksTable.getSelectedItems().size()!=0) {
			    	 for (Task task : tasksTable.getSelectedItems()) {
			    		 removeTaskService.removeTask(task);
			    	 }
			    	 refreshTable();
			    	 tasksTable.deselectAll();
			    	 Notification.show("REMOVE","Tasks have been removed", Type.WARNING_MESSAGE);
			     }else {
			    	 Notification.show("CUIDADO","Seleccione al menos un task para borrar", Type.ERROR_MESSAGE);
			     }
				     
			}else {
				Notification.show("CUIDADO","No se permite eliminar durante la ejecución", Type.ERROR_MESSAGE);
			}   
			
			
		}
			
		
		
        private void runTasks() {

			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
                //vaadinHybridMenuUI.startTasksExecution(new TasksExecutor());
				
				if (tasksTable.getSelectedItems().size()!=0) { //at least one selected
					System.out.println("Tamaño de la seleccion: " + tasksTable.getSelectedItems().size() );
					System.out.println("at least one selected");
					List<Task> tasksSelected = new LinkedList<Task>();
					tasksSelected.addAll(tasksTable.getSelectedItems());
					TasksExecutor tasksExecutor = new TasksExecutor(tasksSelected);
					vaadinHybridMenuUI.startTasksExecution(tasksExecutor);
					
                } else {
                		System.out.println("Normal execution");
                		TasksExecutor tasksExecutor = new TasksExecutor();
    					vaadinHybridMenuUI.startTasksExecution(tasksExecutor);  	  	
                }	
				
			}else {
				Notification.show("CUIDADO","Los tasks se están ejecutando", Type.ERROR_MESSAGE);
			}	     
		}

        
        
	    private class TasksExecutor implements Runnable {

            private List<Task> tasksSelected = null;
	    	
            public TasksExecutor() {
			       //Constructor	
			}
            
			public TasksExecutor(List<Task> tasksSelected) {
				this.tasksSelected = tasksSelected;
			}
	    	
	        @Override
	        public void run() {
	        	List<Task> tasks = null;    
	        	ExecutorService executor = null;
	        	try {
	        		long startTime   = System.currentTimeMillis();
	                System.out.println("....run()::Extraction::starting");               
	    
	                if (tasksSelected!=null) {
	                	tasks = tasksSelected;	
	                } else {
	                	tasks = showAllTasksService.getAllTasks();
	                }		

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
	            	while (!executor.isTerminated())
	        		  {
	            		//Wait for tasks to finished, again!!
	        		  }
	                 vaadinHybridMenuUI.access(new Runnable() {     
                  	     @Override     
                  	     public void run() {         
                  		    progressBar.setVisible(false);
                  		    stopTasksButton.setVisible(true);
                  		    vaadinHybridMenuUI.cancelNotification();
                  	    }
                  	  });
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
	            	setTaskStatus(task,"Pendiente");
	            	setTaskDownloadedProducts(task,0);
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
             	
                 List<CrawlInfo> productsInfo = crawling(task);
                 	                    
                 if (productsInfo.size()!=0) {
                 
                 	int downloadedProducts = scraping(task,productsInfo);
                 	
                 	if (downloadedProducts!=0) {
                 		
                   	    long endTime   = System.currentTimeMillis();
                   	    long totalTime = endTime - startTime;
                  		setTodayDateAndExecutionTime(task,totalTime);
                  		setTaskStatus(task,"Finalizado");  
                  		setTaskDownloadedProducts(task,downloadedProducts); 
               	    }else {
               	    	setTaskStatus(task,"Cancelado" );
               	    	setTaskProgress(task,0.0);
               	    	setTaskDownloadedProducts(task,0);                   
               	   }
                
                 }  else {       	   
                 	setTaskStatus(task,"Cancelado");
                 	setTaskProgress(task,0.0);
                 	setTaskDownloadedProducts(task,0);                 	
	            	}
                 
                } catch (InterruptedException x) {
	            	
	                System.out.println("....run()::Extraction::CANCELED::INTERRUPTED");
	                return;
	            }
				
			}

			

			public List<CrawlInfo> crawling(Task task) throws InterruptedException {
			    
			    setTaskStatus(task,"Escaneando");
			    //System.out.println("Task nombreeee " + task.toString());
        	    List<CrawlInfo> productsInfo = CrawlTaskServiceImpl.getService(task.getRetail().getCrawlerName()).getUrlsFromTask(task);
        	    //System.out.println("Tamaño del array " + productsUrl.size());
        	    if (productsInfo==null)
        	    	Thread.currentThread().interrupt();
        	    
        	    if (Thread.currentThread().isInterrupted()) {
                    System.out.println("....run()::Extraction::Crawling::isInterrupted():" + Thread.currentThread().isInterrupted());
                    return new LinkedList<CrawlInfo>();
                } 
              
				return productsInfo;    
        }
		
		
		public int scraping(Task task, List<CrawlInfo> productsInfo) throws InterruptedException {
			 
			int downloadedProducts = 0;
    		int errorProducts = 0;

    		setTaskStatus(task,"Descargando");
    		
    		for (int i = 0; i<productsInfo.size(); i++) {
    			
    		   CrawlInfo crawlInfo = productsInfo.get(i);
    		 
    		   Product p = CrawlTaskServiceImpl.getService(task.getRetail().getCrawlerName()).parseProductFromURL(crawlInfo, task);
    		
    		   if (p==null)
    			   Thread.currentThread().interrupt();  
    		   
    		   
    		   if (Thread.currentThread().isInterrupted()) {
                System.out.println("....run()::Extraction::Scraper::isInterrupted():" + Thread.currentThread().isInterrupted());
                return 0;
               } 
    		   
    		   if (!p.getProductKey().equals("")) {
    			   downloadedProducts++;
    			   addProductService.saveProduct(p);
    			   //addProductHistService.saveProductHist(new ProductHistory(new ProductHistoryKey(p.getProductKey()),p.getPrice()));
    			   if ((i+1) % 5 == 0 || i+1 == productsInfo.size())  //Update evert 5 downloads or in the last iteration, in the future we can also include the saveProduct.
    			         setTaskProgress(task, (double) (i + 1)/ (double) productsInfo.size());
    		   }else {
    			   errorProducts++;
    			   setTaskProgress(task, (double) (i + 1)/ (double) productsInfo.size());
    		   }
    		   
    		   
    	     }
             
    	    System.out.println("Total de Errores: " + errorProducts );
        
			return downloadedProducts;

        }	
	    	
	}
	    
	    private void setTaskDownloadedProducts(Task task, int downloadedProducts) {
	    	task.setDownloadedProducts(downloadedProducts);
	    	modifyTaskService.modifyTask(task);
	    	refreshTable();
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



		@Override
		public void valueChange(ValueChangeEvent event) {
			
		    if (retailFilter.getValue()!=null) {
		       Retail selectedRetail = (Retail) retailFilter.getValue();
			   tasks = showAllTasksService.getAllTasksFromRetailId(selectedRetail.getRetailId());
		        
                vaadinHybridMenuUI.access(new Runnable() {     
            	   @Override     
            	   public void run() {         
            		   tasksTable.setItems(tasks); 
            	   }
            	});
		    }
		}
		
	}
	
	@Autowired
    private ShowAllRetailsService showAllRetailsService;
	
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	@Autowired
	private AddProductService addProductService;
	
	@Autowired
	private AddProductHistService addProductHistService;
	
	@Autowired
	private ModifyTaskServiceImpl modifyTaskService;
	
	@Autowired
	private ShowAllTasksService showAllTasksService;
	
	@Autowired
	private RemoveTaskService removeTaskService;
	
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
