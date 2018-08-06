package com.dataprice.ui.tasks;


import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.addproducthistservice.AddProductHistService;
import com.dataprice.service.addproductservice.AddProductService;
import com.dataprice.service.crawltask.CrawlTaskServiceImpl;
import com.dataprice.service.modifytask.ModifyTaskServiceImpl;
import com.dataprice.service.removetask.RemoveTaskService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallretails.ShowAllRetailsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.products.Icon;
import com.dataprice.ui.reports.exporter.ExcelExport;
import com.dataprice.ui.reports.gridutil.cell.GridCellFilter;
import com.dataprice.ui.tasks.TaskExecuteOrder.ITaskIterator;
import com.dataprice.ui.tasks.TaskExecuteOrder.RandomTaskIterator;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import com.vaadin.ui.themes.ValoTheme;

import kaesdingeling.hybridmenu.builder.NotificationBuilder;
import kaesdingeling.hybridmenu.data.enums.ENotificationPriority;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ShowAllTasksLayoutFactory{

	private List<Task> tasks;
	private GridCellFilter filter;
	private Grid<Task> tasksTable;	
	private Settings settings;
	private AtomicInteger isCanceledComplete;
	
	private class ShowAllTasksLayout extends VerticalLayout implements Button.ClickListener{

		private Button removeTasksButton;
		private Button runTasksButton;
		private Button stopTasksButton;
		private Button updateTasksButton;
		
		
		private ProgressBar cancelProgressBar;  //Tell us to wait for cancell!
		private ProgressBar removeProgressBar;  //Tell us to wait for cancell!

		private TaskFinishedListener taskFinishedListener;
		private TaskSetFinishedListener taskSetFinishedListener;
		public ShowAllTasksLayout(TaskFinishedListener taskFinishedListener, TaskSetFinishedListener taskSetFinishedListener) {
			this.taskFinishedListener = taskFinishedListener;
			this.taskSetFinishedListener = taskSetFinishedListener;
			
		}



		public ShowAllTasksLayout init() {

			cancelProgressBar = new ProgressBar();
			cancelProgressBar.setCaption("Cancelando...");
			cancelProgressBar.setIndeterminate(true);
			cancelProgressBar.setVisible(false);
			
			updateTasksButton = new Button("Actualizar");
			updateTasksButton.setWidth("100%");
			updateTasksButton.setIcon(VaadinIcons.REFRESH);
			updateTasksButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			updateTasksButton.addClickListener(this);
			
			
			removeProgressBar = new ProgressBar();
			removeProgressBar.setCaption("Removiendo...");
			removeProgressBar.setIndeterminate(true);
			removeProgressBar.setVisible(false);
	        
	        
			removeTasksButton = new Button("Eliminar");
			removeTasksButton.addClickListener(this);
			removeTasksButton.setIcon(VaadinIcons.TRASH);
			removeTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);
			removeTasksButton.setWidth("100%");
							
			runTasksButton = new Button("Correr");
			runTasksButton.setWidth("100%");
			runTasksButton.addClickListener(this);
			runTasksButton.setIcon(VaadinIcons.PLAY_CIRCLE);
			runTasksButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	
			stopTasksButton =  new Button("Cancelar");
			stopTasksButton.setWidth("100%");
			stopTasksButton.setIcon(VaadinIcons.STOP);
			stopTasksButton.setStyleName(ValoTheme.BUTTON_DANGER);
			stopTasksButton.addClickListener(this);
			
			tasksTable = new Grid<>(Task.class);
			tasksTable.removeAllColumns();
			tasksTable.setWidth("100%");
			
			tasksTable.addColumn(p -> p.getTaskName()).setCaption("Nombre").setId("Mytaskname");
			tasksTable.addColumn(p -> p.getRetail().getRetailName()).setCaption("Retail").setId("Myretailname");

			tasksTable.addColumn(t ->
		      t.getProgress(),
		      new ProgressBarRenderer()).setCaption("Progreso");
			
			
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
		       t.getDownloadedProducts()).setCaption("Núm.").setWidth(90);
			
			tasksTable.addColumn(t ->
		      t.getRunDateTime(),
		      new DateRenderer()).setCaption("Fecha y Tiempo de Descarga");

			tasksTable.addColumn(t ->
		      "<a target=\"_blank\" href='" + t.getSeed() + "' target='_top'>Semilla</a>",
		      new HtmlRenderer()).setCaption("Semillas");
			
			tasksTable.setItems(tasks);
			tasksTable.setSelectionMode(SelectionMode.MULTI);
			tasksTable.setSizeFull();
			
             //Set some filters taskName and retail
			filter = new GridCellFilter(tasksTable);
			filter.setTextFilter("Mytaskname", true, false);
			filter.setTextFilter("Myretailname", true, false);

			return this;
		}



		public ShowAllTasksLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			tasks = showAllTasksService.getAllTasks();    
			
			return this;
		}
		
		
		public void updateBotsInfo() {
			
			refreshTable();
			
			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
				System.out.println("Los tasks no se están ejecutando");
				Notification.show("ESTATUS","Los Bots no se están ejecutando", Type.WARNING_MESSAGE);
			}else {
				System.out.println("Los tasks se están ejecutando");
				Notification.show("ESTATUS","Los Bots se están ejecutando", Type.WARNING_MESSAGE);
			}
			
		}
		
		public Component layout() {
	

			//Remove all buttons when progress bar is activated.
			HorizontalLayout hbutton = new HorizontalLayout(runTasksButton,stopTasksButton,cancelProgressBar,removeTasksButton,removeProgressBar,updateTasksButton);
			hbutton.setMargin(false);
		//	hbutton.setMargin(new MarginInfo(true, false, false, false));
			hbutton.setWidth("50%");

			VerticalLayout v1 = new VerticalLayout(hbutton,tasksTable);
			v1.setComponentAlignment(hbutton, Alignment.TOP_CENTER);
            v1.setWidth("100%");
          //  v1.setMargin(false);
            v1.setMargin(new MarginInfo(true, false, false, false));
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
            		if (event.getSource()==this.stopTasksButton)	{
            			stopTasks();
            		}else {
            			updateBotsInfo();
            		}	           		
            	}
            }			
		}



		private void stopTasks() {
		    //stopTasksInNewThread();
			if (vaadinHybridMenuUI.isTaskSetRunning()) {
			 //   stopTasksButton.setVisible(false);
			 //   cancelProgressBar.setVisible(true);
			 //   vaadinHybridMenuUI.stopTasksExecution();
				  stopTasksInNewThread();
			}else {
				Notification.show("ERROR","No se puede cancelar ya que no hay bots en ejecución", Type.ERROR_MESSAGE);
			}
	     }
		
			

		private void stopTasksInNewThread() {
			new Thread(() -> {	
				    isCanceledComplete = new AtomicInteger(0);
				
					vaadinHybridMenuUI.access(() -> {
						 stopTasksButton.setVisible(false);
						 cancelProgressBar.setVisible(true);
   		             });
					
					
					 vaadinHybridMenuUI.stopTasksExecution();		 
 				/**
					 try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				*/	 
					 while (isCanceledComplete.get()==0) {
						 //Do nothing
					 }
					 
					 vaadinHybridMenuUI.access(() -> {
						 stopTasksButton.setVisible(true);
						 cancelProgressBar.setVisible(false);
   		             });
					 
				
			}).start();	
		}



		private void deleteTasks() {
			deleteTasksInNewThread();			
		}
			
		
		
        private void deleteTasksInNewThread() {
        	new Thread(() -> {	
        	if (!vaadinHybridMenuUI.isTaskSetRunning()) {
   			     if (tasksTable.getSelectedItems().size()!=0) {
   			    	vaadinHybridMenuUI.access(() -> {
   			    		removeProgressBar.setVisible(true);
   			    		removeTasksButton.setVisible(false);
   		             });
   			    	 for (Task task : tasksTable.getSelectedItems()) {
   			    		 removeTaskService.removeTask(task);
   			    	 }
   			    	 refreshTable();
   			    	 tasksTable.deselectAll();
   			    	vaadinHybridMenuUI.access(() -> {
   			    		removeProgressBar.setVisible(false);
   			    		removeTasksButton.setVisible(true);
   			    		Notification.show("ELIMINAR","Los bots fueron eliminados con éxito", Type.WARNING_MESSAGE);
   		            });
   			    	 
   			     }else {
   			    	vaadinHybridMenuUI.access(() -> {
   			    		Notification.show("ERROR","Seleccione al menos un bot para borrar", Type.ERROR_MESSAGE);
   		             });	 
   			     }
   				     
   			}else {
			    	vaadinHybridMenuUI.access(() -> {
		   				Notification.show("ERROR","No se permite eliminar bots durante la ejecución", Type.ERROR_MESSAGE);
   		             });	
   			}   
    	  }).start();	
			
		}



		private void runTasks() {
			
         if (showAllTasksService.getNumOfTasks()!=0) {

			if (!vaadinHybridMenuUI.isTaskSetRunning()) {
      		    
				if (tasksTable.getSelectedItems().size()!=0) { //at least one selected
					System.out.println("Tamaño de la seleccion: " + tasksTable.getSelectedItems().size() );
					System.out.println("at least one selected");
					List<Task> tasksSelected = new LinkedList<Task>();
					tasksSelected.addAll(tasksTable.getSelectedItems());
					TasksExecutor tasksExecutor = new TasksExecutor(tasksSelected);
					vaadinHybridMenuUI.startTasksExecution(tasksExecutor);
					// Send notification
 
					 NotificationBuilder.get(vaadinHybridMenuUI.getHybridMenu().getNotificationCenter())
						.withCaption("Notificacíon")
						.withDescription(tasksTable.getSelectedItems().size() + " bots se van a ejecutar")
						.withPriority(ENotificationPriority.MEDIUM)
						.withIcon(VaadinIcons.INFO)
						.withCloseButton()
						.build();
					 
                } else {
                		System.out.println("Normal execution");
                		TasksExecutor tasksExecutor = new TasksExecutor();
    					vaadinHybridMenuUI.startTasksExecution(tasksExecutor);  
    					
    					NotificationBuilder.get(vaadinHybridMenuUI.getHybridMenu().getNotificationCenter())
 						.withCaption("Notificacíon")
 						.withDescription("Todos los bots se van a ejecutar")
 						.withPriority(ENotificationPriority.MEDIUM)
 						.withIcon(VaadinIcons.INFO)
 						.withCloseButton()
 						.build();
                }	
				
			}else {
				Notification.show("ERROR","Los bots se están ejecutando", Type.ERROR_MESSAGE);
			}
         }else {
				Notification.show("ERROR","No hay bots ingresados", Type.ERROR_MESSAGE);
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
	                refreshTable();  //Notify tasktable
	                Task task = null;

	        		//executor = Executors.newFixedThreadPool(Configuration.NUMBEROFCORES);
	                executor = Executors.newFixedThreadPool(settings.getCores());	
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
	        		
	        		/** 
	                //taskSetFinishedListener.taskSetFinished();
	        		vaadinHybridMenuUI.access(() -> {
              		    NotificationBuilder.get(vaadinHybridMenuUI.getHybridMenu().getNotificationCenter())
              		    .withCaption("Test")
      				    .withDescription("Tasks finish succesfuly")
      				    .withPriority(ENotificationPriority.MEDIUM)
      			    	.withIcon(VaadinIcons.INFO)
      				    .withCloseButton()
      				    .build();
  		             });
	        		*/
	        		
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
	            	
	            	vaadinHybridMenuUI.access(() -> {
          		           NotificationBuilder.get(vaadinHybridMenuUI.getHybridMenu().getNotificationCenter())
              	    	  .withCaption("Notificación")
    				      .withDescription("Los bots fueron interrumpidos")
    				      .withPriority(ENotificationPriority.HIGH)
    				      .withIcon(VaadinIcons.INFO)
    				      .withCloseButton()
    				      .build();
						 
  		             });
	            	
	            	refreshTable();
	                System.out.println("....run()::TasksExecutor::CANCELED::INTERRUPTED::THREADS::READY::TO::DIE");
	                isCanceledComplete.incrementAndGet();
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
	    	//refreshTable();
		}
	    
	    private void setTaskStatus(Task task,String status) {
	    	task.setStatus(status);
        	modifyTaskService.modifyTask(task);
        	//refreshTable();
	    }
	    
	    private void setTaskProgress(Task task,Double progress) {
	    	task.setProgress(progress);
        	modifyTaskService.modifyTask(task);
        	//refreshTable();
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
        	//refreshTable();
	    }

		
	}
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
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
