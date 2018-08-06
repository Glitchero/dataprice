package com.dataprice.ui.tasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Retail;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.TaskAdapter;
import com.dataprice.model.entity.TaskJson;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.showallretails.ShowAllRetailsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vaadin.annotations.Push;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import kaesdingeling.hybridmenu.builder.NotificationBuilder;
import kaesdingeling.hybridmenu.data.enums.ENotificationPriority;

@Push
@UIScope
@org.springframework.stereotype.Component
public class TaskExportImportFactory {
	
	private Label exportTittle;	
	private Label importTittle;
	private Label importDescription;
	private Label exportDescription;
	private TextArea importArea;
	private TextArea exportArea;
	private Button importButton;
	private Button exportButton;
	
	private List<Task> tasks;
	private List<TaskJson> importedTasks = null;
	
	private class TaskExportImport extends VerticalLayout implements ClickListener  {

		
		public TaskExportImport init() {
			exportTittle = new Label("<b><font size=\"5\">Exportar</font></b>",ContentMode.HTML);	
			importTittle = new Label("<b><font size=\"5\">Importar</font></b>",ContentMode.HTML);	
			importDescription = new Label("<i><font size=\"3\">Copia y pega tus bots en formato JSON para ingresalos al sistema.</font></i>",ContentMode.HTML);	
			exportDescription = new Label("<i><font size=\"3\">Descarga tus bots en formato JSON y guardalos en tu máquina local. </font></i>",ContentMode.HTML);	

			importArea = new TextArea();
			importArea.setWidth("100%");
			importArea.setHeight("400px");
			
			exportArea = new TextArea();
			exportArea.setWidth("100%");
			exportArea.setHeight("400px");
			
			importButton = new Button("Importar");
			importButton.setWidth("25%");
			importButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			importButton.addClickListener(this);
			
			exportButton = new Button("Exportar");
			exportButton.setWidth("25%");
			exportButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			exportButton.addClickListener(this);

			return this;
		}

		public Component layout() {
			VerticalLayout vImport = new VerticalLayout(importTittle,importDescription,importArea,importButton);
			vImport.setSizeFull();
			
			VerticalLayout vExport = new VerticalLayout(exportTittle,exportDescription,exportArea,exportButton);
			vExport.setSizeFull();
			
			VerticalLayout v1 = new VerticalLayout(vImport,vExport);
			v1.setMargin(false);
			
			return v1;
		}
		
		public TaskExportImport load() {
			tasks = showAllTasksService.getAllTasks();
			return this;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getSource()==importButton) {
				importBots();
			}else {
				exportBots();
			}
			
		}

		private void exportBots() {
			GsonBuilder gsonBuilder = new GsonBuilder();
		    Gson gson = gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter()).create();
			exportArea.setValue(gson.toJson(tasks));
		}

		private void importBots() {
			if (!importArea.isEmpty()) {
			
			try {
				importedTasks = new Gson().fromJson(importArea.getValue(), new TypeToken<List<TaskJson>>(){}.getType());
				
				NotificationBuilder.get(vaadinHybridMenuUI.getHybridMenu().getNotificationCenter())
				.withCaption("Notificación")
				.withDescription("Importación exitosa")
				.withPriority(ENotificationPriority.MEDIUM)
				.withIcon(VaadinIcons.INFO)
				.withCloseButton()
				.build();	
				
				loadTasks();
				
			} catch (Exception e2) {
				NotificationBuilder.get(vaadinHybridMenuUI.getHybridMenu().getNotificationCenter())
					.withCaption("Notificación")
					.withDescription("Importación rechazada")
					.withPriority(ENotificationPriority.HIGH)
					.withIcon(VaadinIcons.INFO)
					.withCloseByHide() 
					.build();
			}
			
			importArea.setValue("");
			
			}else {
				Notification.show("ERROR","No hay entrada JSON", Type.ERROR_MESSAGE);
			}
		}

		/**
		 * Load importedTasks
		 */
		private void loadTasks() {
			for (TaskJson taskJson : importedTasks) {
				Retail retail = showAllRetailsService.getRetailFromId(taskJson.getRetailId());
			    if (retail!=null) { //If retail exist!!
			        Task task = new Task();	
			    	task.setTaskName(taskJson.getTaskName());
			    	task.setRetail(retail);
			    	task.setSeed(taskJson.getSeed());	
			    	addtaskService.saveTask(task);
			    }
			    
			}		
		}
	
		
		
	}

	
	@Autowired
    private ShowAllTasksService showAllTasksService;
	
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	@Autowired
	private ShowAllRetailsService showAllRetailsService;
	
	@Autowired
    private AddTaskService addtaskService;
	
	public Component createComponent() {
		return new TaskExportImport().load().init().layout();
	}


}
