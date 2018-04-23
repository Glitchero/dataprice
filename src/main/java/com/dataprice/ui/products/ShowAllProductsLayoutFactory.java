package com.dataprice.ui.products;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.search.Explanation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Button.ClickEvent;
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
public class ShowAllProductsLayoutFactory {
	
	private List<Product> products;
	
	private Settings settings;
	
	private Grid<Product> productsTable;
			
	private ProgressBar progressBar;
	
	private Product currentSelection;
	
	private Label currentSeller;
	private TextFieldWithTwoButtons textFieldWithTwoButtons;
	
	private class ShowAllProductsLayout extends VerticalLayout implements SelectionListener<Product> {

		ProductEditListener productEditListener;
		
		public ShowAllProductsLayout(ProductEditListener productEditListener) {
		   this.productEditListener = productEditListener;
		}


		public ShowAllProductsLayout init() {
			
			textFieldWithTwoButtons = new TextFieldWithTwoButtons(VaadinIcons.SEARCH, VaadinIcons.REFRESH, onClick -> searchProduct(), onClick -> refresh());
			textFieldWithTwoButtons.setWidth("70%");
			textFieldWithTwoButtons.getTextfield().setPlaceholder("Busca por nombre, sku, upc, etc.");
			
			if (settings.getMainSeller()==null || products.size()==0) {
				currentSeller = new Label("<b><font size=\"3\">" + "Para visualizar los productos debe elegir un vendedor en settings." + "</font></b>",ContentMode.HTML);	
				currentSeller.addStyleName(ValoTheme.LABEL_FAILURE);
			}else {
		    	currentSeller = new Label("<b><font size=\"3\">" + "Los productos petenecen al vendedor: " + settings.getMainSeller() + "</font></b>",ContentMode.HTML);	
		    	currentSeller.addStyleName(ValoTheme.LABEL_SUCCESS);
		    }
				
			currentSeller.setWidth("100%");
									
			productsTable = new Grid<>(Product.class);

			productsTable.removeAllColumns();
			
			productsTable.addColumn(p -> p.getUpdateDay()).setCaption("Actualizado");
			productsTable.addColumn(p -> p.getName()).setCaption("Nombre");
			productsTable.addColumn(p -> p.getPrice()).setCaption("Precio");
			if (settings.getKeyType().equals("sku")) {
				productsTable.addColumn(p -> p.getSku()).setCaption("SKU");
			}else {
				productsTable.addColumn(p -> p.getUpc()).setCaption("UPC");
			} 
			
			productsTable.addComponentColumn(p -> {
				Icon icon = null;
				if (p.isChecked()) {
				icon = new Icon(VaadinIcons.CHECK_SQUARE);
				}else {
				icon = new Icon(VaadinIcons.THIN_SQUARE);
				}
				return icon;
			}).setCaption("").setWidth(50);
			
			productsTable.setItems(products);
		    productsTable.addSelectionListener(this);		    	
			productsTable.setSizeFull();
		
			return this;
		}
		
		
		public ShowAllProductsLayout load() {
			//loadDataInNewThread();
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
					
			products = showAllProductsService.getAllProductsFromSeller(settings.getMainSeller());
			//System.out.println("La fecha del primer producto es: " + products.get(0).getUpdateDay());
			return this;
		}
		
		 private void loadDataInNewThread() {
		        new Thread(() -> {
		        	products = showAllProductsService.getAllProducts();
		            // this is needed because we are modifying the UI from a different thread:
		        	vaadinHybridMenuUI.access(() -> {
		        		progressBar.setVisible(false);
		        		productsTable.setItems(products);
		                productsTable.setVisible(true);
		            });
		        }).start();
		    }

		
		public Component layout() {
			
		
			VerticalLayout productsTableLayout = new VerticalLayout(currentSeller,textFieldWithTwoButtons,productsTable);
			productsTableLayout.setMargin(false);
			productsTableLayout.setSizeFull();
			
			return productsTableLayout;
		}


		private void searchProduct() {		
		  List<Product> retrieveList = searchProductService.search(textFieldWithTwoButtons.getTextfield().getValue());	
        
		  if(retrieveList!=null) { 
		      if(retrieveList.size()!=0) { 
        	  
        	    List<Product> retrieveFilteredList = new LinkedList<Product>();
        	  
        		  for (Product p : retrieveList) {
        			  if (p.getSeller().equals(settings.getMainSeller())) {
        			    retrieveFilteredList.add(p);
        			  }
           		  }
        	  
		        productsTable.setItems(retrieveFilteredList);
             }
		}
	   }

		@Override
		public void selectionChange(SelectionEvent<Product> event) {
			
			 SingleSelectionModel<Product> singleSelect = (SingleSelectionModel<Product>) productsTable.getSelectionModel();
			 if (singleSelect.getSelectedItem().isPresent()) {
				 if (currentSelection!=null) {
					 if (!currentSelection.equals(singleSelect.getSelectedItem().get())) {
				          currentSelection = singleSelect.getSelectedItem().get();
				          productEditListener.productEdited(currentSelection);
					 }
				 }else{
					 currentSelection = singleSelect.getSelectedItem().get();
				     productEditListener.productEdited(currentSelection);
				 }
			 }
		}
	
		
		
	}
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
		
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	@Autowired
	private SearchProductService searchProductService;
	
	public Component createComponent(ProductEditListener productEditListener) {
		return new ShowAllProductsLayout(productEditListener).load().init().layout();
	}



	public void refresh() {
		products = showAllProductsService.getAllProductsFromSeller(settings.getMainSeller());
		productsTable.setItems(products);
		if (currentSelection!=null)
		    productsTable.select(currentSelection);
	}



	

}

