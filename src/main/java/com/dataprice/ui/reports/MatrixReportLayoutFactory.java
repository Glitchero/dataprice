package com.dataprice.ui.reports;


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
import com.vaadin.ui.VerticalLayout;


@Push
@UIScope
@org.springframework.stereotype.Component
public class MatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	
	private Icon upArrow;
	private Icon downArrow;
	private Icon equalArrow;
	
	
	private class MatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public MatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public MatrixReportLayout init() {
			
			upArrow = new Icon(VaadinIcons.ARROW_UP);
			upArrow.setRedColor();
		//	upArrow.setCaption("Soy más caro");
			
			downArrow = new Icon(VaadinIcons.ARROW_DOWN);
		//	downArrow.setCaption("Soy más barato");
			
			equalArrow = new Icon(VaadinIcons.SCALE);
			equalArrow.setBlueColor();
		//	equalArrow.setCaption("Tengo mismo precio");
			
			
			productsTable = new Grid<>(Product.class);						  
			productsTable.removeAllColumns();

			 if (settings.getKeyType().equals("sku")) {
			     productsTable.addComponentColumn(p -> {
				 Label label = new Label("<b><font size=\"3\">" + p.getSku() + "</font></b>",ContentMode.HTML);	
			     return label;
			     }).setCaption("Sku").setId("Mysku");	
			 }else {
				 productsTable.addComponentColumn(p -> {
				 Label label = new Label("<b><font size=\"3\">" + p.getUpc() + "</font></b>",ContentMode.HTML);	
				 return label;
				 }).setCaption("Upc").setId("Myupc");	
			 }
			
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getName() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Nombre").setId("Myname");	
			
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getUpdateDay() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Actualizado").setId("Update");
			
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getPrice() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Mi precio").setId("Myprice");
			
			
			
			
			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
					 productsTable.addComponentColumn(p -> {
						 List<Product> products = null;
						 if (settings.getKeyType().equals("sku")) {
							 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
							}else {
							 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
							}
						HorizontalLayout h1 = new HorizontalLayout();
					    Label label = new Label();
					    Icon icon = null;
				        if (products.size()!=0) {
				        	label.setContentMode(ContentMode.HTML);
				        	if (p.getPrice()<products.get(0).getPrice()) {
					        	label.setValue("<font size = \"3\" color=\"green\">" + products.get(0).getPrice().toString());	
					        	icon = new Icon(VaadinIcons.ARROW_DOWN);
					        	h1.addComponent(label);
					        	h1.addComponent(icon);
				        	}else {
				        		if (p.getPrice()>products.get(0).getPrice()) {
					        	label.setValue("<font size = \"3\" color=\"red\">" + products.get(0).getPrice().toString());
					        	icon = new Icon(VaadinIcons.ARROW_UP);
					        	icon.setRedColor();
					        	h1.addComponent(label);
					        	h1.addComponent(icon);
				        		}else {
				        			label.setValue("<font size = \"3\" color=\"blue\">" + products.get(0).getPrice().toString());	
				        			icon = new Icon(VaadinIcons.SCALE);
				        			icon.setBlueColor();
				        			h1.addComponent(label);
				        			h1.addComponent(icon);
				        		}
				        	}
				        }
					    return h1;
					}).setCaption(seller).setId(seller);
				  
				}			
			
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getBrand() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Marca").setId("Mybrand");
			
			productsTable.setWidth("100%");
			productsTable.setItems(products);
				
			
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

		public MatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			java.util.Date lastUpdate = java.sql.Date.valueOf(reportSettings.getLastUpdate());
			
			if (settings.getKeyType().equals("sku")) {
				products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), reportSettings.getCategories(),lastUpdate,reportSettings.getCompetitors());
			}else {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), reportSettings.getCategories(),lastUpdate,reportSettings.getCompetitors());
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
