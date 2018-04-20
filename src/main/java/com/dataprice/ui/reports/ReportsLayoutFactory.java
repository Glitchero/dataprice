package com.dataprice.ui.reports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.searchproduct.SearchProductService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.vaadin.annotations.Push;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ReportsLayoutFactory {
	
	private List<String> competitors;
	
	private List<Product> products;
	
	private Grid<Product> productsTable;
	
	private Settings settings;
	
	private Button generateMatrixReport;
	
	private TwinColSelect<String> select;
	
	private class ReportsLayout extends VerticalLayout implements Button.ClickListener,ValueChangeListener, SelectionListener<Product> {


		public ReportsLayout init() {
			
			generateMatrixReport = new Button("Generar Matriz de Precios");
			generateMatrixReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			generateMatrixReport.addClickListener(this);
			generateMatrixReport.setWidth("25%");
			
			
			select = new TwinColSelect<>("Selecciona la competencia:");
		
			select.setItems(competitors);
			
			select.addSelectionListener(event ->
		    addComponent(new Label("Selected: " + event.getNewSelection())));
		
			productsTable = new Grid<>(Product.class);
			productsTable.removeAllColumns();
			
		//	productsTable.setColumnOrder("name",settings.getKeyType());
		
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getName() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Nombre").setId("Myname");	
			

			if (settings.getKeyType().equals("sku")) {
				productsTable.addComponentColumn(p -> {
					Label label = new Label("<b><font size=\"3\">" + p.getSku() + "</font></b>",ContentMode.HTML);	
				    return label;
				}).setCaption("SKU").setId("Mysku");	
			}else {
				productsTable.addComponentColumn(p -> {
					Label label = new Label("<b><font size=\"3\">" + p.getUpc() + "</font></b>",ContentMode.HTML);	
				    return label;
				}).setCaption("UPC").setId("Myupc");	
			}
			
			productsTable.addComponentColumn(p -> {
				Label label = new Label("<b><font size=\"3\">" + p.getPrice() + "</font></b>",ContentMode.HTML);	
			    return label;
			}).setCaption("Mi precio").setId("Myprice");
			
			productsTable.setWidth("100%");
		
			/**
			productsTable.removeColumn("price");
			productsTable.removeColumn("productId");
			productsTable.removeColumn("imageUrl");
			productsTable.removeColumn("productUrl");
			productsTable.removeColumn("brand");
			productsTable.removeColumn("seller");
			productsTable.removeColumn("description");
			productsTable.removeColumn("task");
			productsTable.removeColumn("productKey");
			if (settings.getKeyType().equals("sku")) {
				productsTable.removeColumn("upc");
			}else {
				productsTable.removeColumn("sku");
			}
			*/
			

			
			/**
			 * THIS IS WORKING , FIGURETE OUT HOW TO DO IT.
				sellersWithNoMatch.add("PANDA CANE"); //Get list with sql query!.
				
				for (String sellerWithNoMatch : sellersWithNoMatch) {
					productsTable.removeColumn(sellerWithNoMatch);
				}
				
			*/
			
			return this;
		}
		


		public ReportsLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			if (settings.getKeyType().equals("sku")) {
				products = showAllProductsService.getProductsFromSellerNameWithMatchesSku(settings.getMainSeller());
			}else {
				products = showAllProductsService.getProductsFromSellerNameWithMatchesUpc(settings.getMainSeller());
			}
			
			competitors = reportsService.getCompetitorsList(settings.getMainSeller());
			return this;
		}
	

		
		public Component layout() {
			
			FormLayout form = new FormLayout(select,generateMatrixReport);
			form.setMargin(false);
			form.setSizeFull();
			
			VerticalLayout vl = new VerticalLayout(form,productsTable);
			vl.setSizeFull();
			vl.setMargin(false);
			return vl;
		}


		@Override
		public void buttonClick(ClickEvent event) {
						generateMatrixReport();
		}


		private void generateMatrixReport() {
	
			for (String seller : select.getValue()) {  //Competition
				  if (!seller.equals(settings.getMainSeller()))	{ 
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
				}

				productsTable.setItems(products);
			
		}



		@Override
		public void selectionChange(SelectionEvent<Product> event) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void valueChange(ValueChangeEvent event) {
			
		
			
		}

		
		
	}
	
	
	@Autowired 
	private ReportsService reportsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	public Component createComponent() {
		return new ReportsLayout().load().init().layout();
	}

	public void refresh() {
		products = showAllProductsService.getAllProducts();
		productsTable.setItems(products);
	
	}



	

}
