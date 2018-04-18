package com.dataprice.ui.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
	
	//private Integer numberOfCheckedProducts;
	private Integer numberOfProducts;
	private Integer numberOfTasks;
	private Integer numberOfWorkingTasks;
	private Integer numberOfCanceledTasks;
	private Integer numberOfFinalizedTasks;
	private Integer numberOfPendingTasks;
		public HeaderLayout init() {
			
			header1 = new Label(
				    "Total de Productos: \n" +
				    "<center><b><font size=\"7\">" + numberOfProducts + "</font></b></center>",
				    ContentMode.HTML);
			
			header2 = new Label(
				    "Total de Bots: \n" +
				    "<center><b><font size=\"7\">" + numberOfTasks + "</font></b></center>",
				    ContentMode.HTML);
			
			header3 = new Label(
				    "Bots trabajando: \n" +
				    "<center><b><font size=\"7\">" + numberOfWorkingTasks +"/3" + "</font></b></center>",
				    ContentMode.HTML);
			
			header4 = new Label(
				    "Bots cancelados: \n" +
				    "<center><b><font size=\"7\">" + numberOfCanceledTasks + "</font></b></center>",
				    ContentMode.HTML);
			
			header5 = new Label(
				    "Bots Pendientes: \n" +
				    "<center><b><font size=\"7\">" + numberOfPendingTasks + "</font></b></center>",
				    ContentMode.HTML);
			
			header6 = new Label(
				    "Bots Finalizados: \n" +
				    "<center><b><font size=\"7\">" + numberOfFinalizedTasks + "</font></b></center>",
				    ContentMode.HTML);
			
			header7 = new Label(
				    "Total de Productos: \n" +
				    "<center><b><font size=\"7\">" + numberOfProducts + "</font></b></center>",
				    ContentMode.HTML);
			
			return this;
		}
		

	    public HeaderLayout load() {
	    	numberOfProducts = dashboardService.getNumOfProducts(); 
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
	    	HorizontalLayout hl = new HorizontalLayout(header1,header2,header3,header4,header5,header6,header7);
	        hl.setHeight("150px");
	        hl.setWidth("100%");
	        hl.setMargin(false);
		    return hl;
	    }
	    

	}
	
	@Autowired
	private DashboardService dashboardService;
	
	@Override
	public Component createComponent() {
		return new HeaderLayout().load().init().layout();
	}
}
