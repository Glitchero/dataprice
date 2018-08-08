package com.dataprice.ui.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class HeaderLayoutFactory implements UIComponentBuilder{

	
	private class HeaderLayout extends VerticalLayout {
	
	private Label header1;
	private Label header2;
	private Label header3;
	private Label header4;
	private Label header5;
	private Label header6;
	private Label header7;
	private ProgressBar productsBar;
	private ProgressBar botBar;
	
	private Label mainTittle;
	private Label subTittle;
	
	//private Integer numberOfCheckedProducts;
	private Integer numberOfProducts;
	private Integer numberOfTasks;
	private Integer numberOfWorkingTasks;
	private Integer numberOfCanceledTasks;
	private Integer numberOfFinalizedTasks;
	private Integer numberOfPendingTasks;
	
	private Settings settings;
		public HeaderLayout init() {
			
			
			
			mainTittle = new Label("<b><font size=\"5\">Administrador de Bots </font></b>",ContentMode.HTML);	
			subTittle = new Label("<font size=\"2\">Agregue, ejecute y elimine bots. </font>",ContentMode.HTML);
			
			float pbar =((float) numberOfProducts / (float) 100);
			productsBar = new ProgressBar(pbar); 
			productsBar.setWidth("90%");
			
			
			float bbar =((float) numberOfTasks / (float) 10);
			botBar = new ProgressBar(bbar); 
			botBar.setWidth("90%");
			
			
			header1 = new Label(
				    "Total de Productos: \n" +
				    "<center><b><font size=\"7\">" + numberOfProducts +"/100" + "</font></b></center>",
				    ContentMode.HTML);
			
			
			
			header2 = new Label(
				    "Total de Bots: \n" +
				    "<center><b><font size=\"7\">" + numberOfTasks +"/10" + "</font></b></center>",
				    ContentMode.HTML);
			
			header3 = new Label(
				    "Plan contratado: \n" +
				    "<center><b><font size=\"5\">" + "Versión Beta" + "</font></b></center>",
				    ContentMode.HTML);
			
			header4 = new Label(
				    "Bots cancelados: \n" +
				    "<center><b><font size=\"7\">" + numberOfCanceledTasks + "</font></b></center>",
				    ContentMode.HTML);
			
			header5 = new Label();
			
			header6 = new Label();
			
			header7 = new Label("");
			
			return this;
		}
		

	    public HeaderLayout load() {
	    	
	    	User user = userServiceImpl.getUserByUsername("admin");
			settings = user.getSettings();
			
			
	    	numberOfProducts = dashboardService.getNumOfProducts(settings.getMainSeller()); 
	    	numberOfTasks = dashboardService.getNumOfTasks();
	    
	    	Integer downloadingTasks = dashboardService.getNumOfTasksByStatus("Descargando");
	    	Integer scanningTasks = dashboardService.getNumOfTasksByStatus("Escaneando");
	    	
	    	numberOfWorkingTasks = downloadingTasks + scanningTasks;
	    	
	    	numberOfCanceledTasks = dashboardService.getNumOfTasksByStatus("Cancelado");
	    	
	    	numberOfPendingTasks = dashboardService.getNumOfTasksByStatus("Pendiente");
	    	
	    	numberOfFinalizedTasks = dashboardService.getNumOfTasksByStatus("Finalizado");
		    return this;
	    }


	    public Component layout() {
	    	
	    	VerticalLayout hl = new VerticalLayout(header6,header1,productsBar,header7,header2,botBar);
	     //   hl.setHeight("150px");
	        hl.setWidth("100%");
	        hl.setMargin(false);
		    return hl;
	    }
	    

	}
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Override
	public Component createComponent() {
		return new HeaderLayout().load().init().layout();
	}
}
