package com.dataprice.ui.reports;

import java.util.Date;
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
import com.vaadin.annotations.Push;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@Push
@UIScope
@org.springframework.stereotype.Component
public class MatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	
	private class MatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public MatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public MatrixReportLayout init() {
			productsTable = new Grid<>(Product.class);
			productsTable.removeAllColumns();
		
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getName() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Nombre").setId("Myname");	
			
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getPrice() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Mi precio").setId("Myprice");
			
			productsTable.setWidth("100%");
			
			
			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
					 productsTable.addComponentColumn(p -> {
						 List<Product> products = null;
						 if (settings.getKeyType().equals("sku")) {
							 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
							}else {
							 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
							}
					    Label label = new Label();
				        if (products.size()!=0) {
				        	label.setContentMode(ContentMode.HTML);
				        	if (p.getPrice()<=products.get(0).getPrice()) {
					        	label.setValue("<font size = \"3\" color=\"green\">" + products.get(0).getPrice().toString());	
				        	}else {
					        	label.setValue("<font size = \"3\" color=\"red\">" + products.get(0).getPrice().toString());	
				        	}
				        }
					    return label;
					}).setCaption(seller).setId(seller);
				  
				}

				productsTable.setItems(products);
				
			
			return this;
		}

		public Component layout() {
			return productsTable;
		}

		public MatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			java.util.Date startDate = java.sql.Date.valueOf(reportSettings.getStartDate());
			java.util.Date endDate = java.sql.Date.valueOf(reportSettings.getEndDate());
			
			if (settings.getKeyType().equals("sku")) {
				products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), reportSettings.getCategories(),startDate,endDate,reportSettings.getCompetitors());
			}else {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), reportSettings.getCategories(),startDate,endDate,reportSettings.getCompetitors());
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
		return new MatrixReportLayout(reportSettings).load().init().layout();
	}

	
}
