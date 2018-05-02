package com.dataprice.ui.reports;


import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.products.Icon;
import com.dataprice.ui.reports.gridutil.cell.GridCellFilter;
import com.vaadin.annotations.Push;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;


@Push
@UIScope
@org.springframework.stereotype.Component
public class PerMatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	
	private Icon upArrow;
	private Icon downArrow;
	private Icon equalArrow;
	
	private DecimalFormat df = new DecimalFormat("#0.0");
	
	
	private class PerMatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public PerMatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public PerMatrixReportLayout init() {
			
			upArrow = new Icon(VaadinIcons.STOP);
			upArrow.setRedColor();
		//	upArrow.setCaption("Soy más caro");
			
			downArrow = new Icon(VaadinIcons.STOP);
		//	downArrow.setCaption("Soy más barato");
			
			equalArrow = new Icon(VaadinIcons.STOP);
			equalArrow.setBlueColor();
		//	equalArrow.setCaption("Tengo mismo precio");
			
			
			productsTable = new Grid<>(Product.class);						  
			productsTable.removeAllColumns();

			if (settings.getKeyType().equals("sku")) {
				productsTable.addColumn(p -> p.getSku()).setCaption("Sku").setId("Mysku");
			}else {
				productsTable.addColumn(p -> p.getUpc()).setCaption("Upc").setId("Myupc");
			}
			
			productsTable.addColumn(p -> p.getName()).setCaption("Nombre").setId("Myname");
			
	        productsTable.addColumn(p -> p.getBrand()).setCaption("Brand").setId("Mybrand");
			
			productsTable.addColumn(p -> p.getCategory()).setCaption("Category").setId("Mycategory");
				
			productsTable.addColumn(p -> p.getPrice()).setCaption("Mi precio").setId("Myprice");
			
			
			
			/**
			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
					 productsTable.addComponentColumn(p -> {
						 List<Product> products = null;
						 if (settings.getKeyType().equals("sku")) {
							 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
							}else {
							 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
							}
						HorizontalLayout h1 = new HorizontalLayout();
						h1.setSizeFull();
					
					    Label label = new Label();
					    ProgressBar progressBar = new ProgressBar();
	
					    Double percentage = 0.0;
				        if (products.size()!=0) {
				        	label.setContentMode(ContentMode.HTML);
				        	if (p.getPrice()<products.get(0).getPrice()) {
				        		percentage = ((products.get(0).getPrice()-p.getPrice())/p.getPrice())*100.0; 
					        	label.setValue("<font size = \"2\" color=\"black\">" + "-" + df.format(percentage) + "%");	
					        	progressBar.setValue((float)((percentage/100.0) + (percentage/100.0)*2));
					        	progressBar.setStyleName("myprogress");
					        	h1.addComponent(progressBar);
					        	h1.addComponent(label);
					        	h1.setComponentAlignment(progressBar, Alignment.BOTTOM_LEFT);
					        	
					        	h1.setComponentAlignment(label, Alignment.TOP_RIGHT);
					        	h1.setExpandRatio(progressBar, 1);
				        	}else {
				        		if (p.getPrice()>products.get(0).getPrice()) {
				        		   percentage = ((p.getPrice()-products.get(0).getPrice())/products.get(0).getPrice())*100.0; 
					        	   label.setValue("<font size = \"2\" color=\"black\">" + "+" +df.format(percentage) + "%");
					        	   progressBar.setValue((float)((percentage/100.0) + (percentage/100.0)*2));
					        	   progressBar.setStyleName("myprogressbar");
					        	   h1.addComponent(progressBar);
					        	   h1.addComponent(label);
					        	   h1.setComponentAlignment(progressBar, Alignment.BOTTOM_LEFT);
						           h1.setComponentAlignment(label, Alignment.TOP_RIGHT);
						       //    h1.setExpandRatio(progressBar, 1);
				        		}else {
						        	   label.setValue("<font size = \"2\" color=\"black\">" + "+0,0%");
						        	   progressBar.setValue((float)(0.01));			
						        	   h1.addComponent(progressBar);
						        	   h1.addComponent(label);
						        	   h1.setComponentAlignment(progressBar, Alignment.BOTTOM_LEFT);
							           h1.setComponentAlignment(label, Alignment.TOP_RIGHT);
							           h1.setExpandRatio(progressBar, 1);
				        			
				        		}
				        	}
				        }
					    return h1;
					}).setCaption(seller).setId(seller);
				  
				}			
			*/

			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
				 productsTable.addComponentColumn(p -> {
					 List<Product> products = null;
					 if (settings.getKeyType().equals("sku")) {
						 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
						}else {
						 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
						}
					 BarPositionIndicator barPositionIndicator = null;
			        if (products.size()!=0) {
			        	barPositionIndicator = new BarPositionIndicator(p.getPrice(),products.get(0).getPrice());
			        }
				    return barPositionIndicator;
				}).setCaption(seller).setId(seller);
			  
			}
			
			productsTable.setWidth("100%");
			productsTable.setHeight("500px");
			productsTable.setItems(products);
				
          /////////////////////////Filter Utils//////////////////////////////
			
         final GridCellFilter filter = new GridCellFilter(productsTable);

         filter.setTextFilter("Myname", true, false);

         if (settings.getKeyType().equals("sku")) {
	         filter.setTextFilter("Mysku", true, true);
         }else {
	         filter.setTextFilter("Myupc", true, true);
         }

	    filter.setTextFilter("Mybrand", true, true);

	    filter.setTextFilter("Mycategory", true, true);
	
	    filter.setNumberFilter("Myprice", Double.class,"invalid input", "inferior", "superior");
	
	    for (String seller : reportSettings.getCompetitors()) {
		     filter.setIndicatorFilter(seller);
	    }
	

///////////////////////////////////////////////////////////////////
	
			return this;
		}

		public Component layout() {
			
			HorizontalLayout f1 = new HorizontalLayout(upArrow, new Label("Soy más caro"));
			f1.setMargin(false);
			
			HorizontalLayout f2 = new HorizontalLayout(equalArrow, new Label("Tengo mismo precio"));
			f1.setMargin(false);
			
			HorizontalLayout f3 = new HorizontalLayout(downArrow, new Label("Soy más barato"));
			f1.setMargin(false);
			
			HorizontalLayout h1 = new HorizontalLayout(f1,f2,f3);
			h1.setWidth("50%");
			h1.setMargin(false);
			
			VerticalLayout v1 = new VerticalLayout(h1,productsTable);
			v1.setComponentAlignment(h1, Alignment.MIDDLE_RIGHT);
			v1.setSizeFull();
			v1.setMargin(false);
			
	        return v1;
		}

		public PerMatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			java.util.Date lastUpdate = java.sql.Date.valueOf(reportSettings.getLastUpdate());
			
			if (settings.getKeyType().equals("sku")) {
				products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), lastUpdate,reportSettings.getCompetitors());
			}else {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), lastUpdate,reportSettings.getCompetitors());
			}
			System.out.println("TOtal de productos: " + products.size());
			return this;
		}
		
	}

	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired 
	private ReportsService reportsService;
	
	public Component createComponent(ReportSettings reportSettings) {
		return new PerMatrixReportLayout(reportSettings).load().init().layout();
	}

	
}
