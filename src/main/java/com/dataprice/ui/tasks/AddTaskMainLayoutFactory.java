package com.dataprice.ui.tasks;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.*;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallretails.ShowAllRetailsService;
import com.dataprice.utils.StringUtils;

@org.springframework.stereotype.Component
public class AddTaskMainLayoutFactory {

	    private class AddTaskMainLayout extends VerticalLayout implements Button.ClickListener{
		
	   	private Settings settings;	

	    private TextField taskName;
	    private ComboBox retail;
	    private TextField seed;
	    //private TextField status;
	    private Button saveButton;
	    private Button clearButton;
        private List<Retail> retailers;
	    
	    private Binder<Task> binder;
	    private Label currentCountry;
	    
	    private Task task;
	
	

	    
	    private TaskSavedListener taskSavedListener;
	    
	    public AddTaskMainLayout(TaskSavedListener taskSavedListener) {
			this.taskSavedListener = taskSavedListener;
		}

		public AddTaskMainLayout init() {
			
		
			
	    	binder = new Binder<>(Task.class);
	    	
	    	task = new Task();
	    	
	    	if (settings.getCountrySelected()==null) {
	    		currentCountry = new Label("<b><font size=\"3\">" + StringUtils.TASKS_SELECT_COUNTRY.getString() + "</font></b>",ContentMode.HTML);	
				currentCountry.addStyleName(ValoTheme.LABEL_FAILURE);
			}else {
				currentCountry = new Label("<b><font size=\"3\">" + StringUtils.TASKS_SELECTED_COUNTRY.getString() + settings.getCountrySelected() + "</font></b>",ContentMode.HTML);	
		    	currentCountry.addStyleName(ValoTheme.LABEL_SUCCESS);
		    }
	    	
	    	
	    	taskName = new TextField(StringUtils.TASK_NAME.getString());
	    //	taskName.setPlaceholder("Escribe el nombre que mejor represente a tu bot");
	    	taskName.setWidth("35%");
	    	
	    	retail = new ComboBox(StringUtils.TASK_MARKETPLACE.getString());
	    //	retail.setPlaceholder("Selecciona el retail o marketplace");
	    	retail.setWidth("35%");
	    	
	    	seed = new TextField(StringUtils.TASK_SEED.getString());
	    //	seed.setPlaceholder("Copia y pega la url donde se encuentran los productos que deseas descargar");
	    	seed.setWidth("70%");
	    	
	    	
		//	status = new TextField("Status");
			
			saveButton = new Button(StringUtils.SAVE.getString());
			saveButton.setWidth("100%");
			saveButton.setIcon(VaadinIcons.EDIT);
			
			clearButton = new Button(StringUtils.ClEAN.getString());
			clearButton.setWidth("100%");
			clearButton.setIcon(VaadinIcons.ERASER);
			
			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			clearButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			
			saveButton.addClickListener(this);
			clearButton.addClickListener(this);

			//gender.setEmptySelectionAllowed(false);
			retail.setItems(retailers);

			return this;
		    
	    }
	
		public AddTaskMainLayout bind() {
			binder.forField(taskName)
			  .asRequired("taskName is required")
			  .bind("taskName");
			
			binder.forField(retail)
			  .asRequired("retail is required")
			  .bind("retail");

			binder.forField(seed)
			  .asRequired("Seed is required")
			  .bind("seed");
			
		//	binder.forField(status)
		//	  .asRequired("Status must be set")
		//	  .bind("status");
			
			binder.readBean(task);
			
			return this;
		}	
		
	    public Component layout() {		
 	
	    	/**
	    	HorizontalLayout h2 = new HorizontalLayout(taskName,retail);
	    	h2.setWidth("100%");
	    	h2.setMargin(false);
	    	
	    	HorizontalLayout h3 = new HorizontalLayout(seed);
	    	h3.setWidth("100%");
	    	h3.setMargin(false);
	    	
	    	HorizontalLayout h1 = new HorizontalLayout(h2,h3);
	    	
	    	h1.setWidth("100%");
	    	h1.setMargin(false);
	    	
	    	HorizontalLayout h4 = new HorizontalLayout(saveButton,clearButton);
	    	h4.setWidth("25%");
	    	h4.setMargin(false);
	    	
	    	
            VerticalLayout v1 = new VerticalLayout(h1,h4);
            v1.setComponentAlignment(h4, Alignment.TOP_LEFT);
            v1.setWidth("100%");
            v1.setMargin(false);
            
	    	return v1;
            */
            
	    	
	    	
	    	HorizontalLayout hbuttons= new HorizontalLayout(saveButton,clearButton);
	    	hbuttons.setWidth("40%");
	    	
	    	FormLayout form = new FormLayout(taskName,retail,seed);
	    	form.setWidth("100%");
	    	
	    	VerticalLayout vl = new VerticalLayout(currentCountry,form,hbuttons);
	    	vl.setComponentAlignment(currentCountry, Alignment.TOP_LEFT);
	    	vl.setMargin(true);
	    	vl.setSizeFull();

	    
	    	
	    	return vl;
	    	
	    }

		@Override
		public void buttonClick(ClickEvent event) {
                 if (event.getSource()==this.saveButton)	{
                	 save();
                 }else {
                	 clearField();
                 }
		}

		private void save() {
			try {
				binder.writeBean(task);
			} catch (ValidationException e) {
				Notification.show(StringUtils.ERROR.getString(),StringUtils.NOT_SAVED.getString(),Type.ERROR_MESSAGE);
				return;
			}
			
			//Add the limits on the settings !!! DO LATER IS IMPORTANT
			//if(dashboardService.getNumOfTasks()>19) {
			//	Notification.show("ERROR","Límite de bots alcanzado",Type.ERROR_MESSAGE);
			//	return;
			//}
			
			
			//System.out.println(task);
			addtaskService.saveTask(task);
			taskSavedListener.taskSaved();
			clearField();
			Notification.show(StringUtils.SUCESS.getString(),StringUtils.SAVED.getString(),Type.WARNING_MESSAGE);
		}

		private void clearField() {

			taskName.setValue("");
			seed.setValue("");
		//	status.setValue("");
			retail.setValue(null);
		}

		public AddTaskMainLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			retailers = showAllRetailsService.getAllRetailersFromCountry(settings.getCountrySelected());
			return this;
		}
	  
	}
	    
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired
    private AddTaskService addtaskService;
	
	@Autowired
    private ShowAllRetailsService showAllRetailsService;
	
	@Autowired
	private DashboardService dashboardService;
	    
	public Component createComponent(TaskSavedListener taskSavedListener) {
		return new AddTaskMainLayout(taskSavedListener).load().init().bind().layout();
	}
	
}