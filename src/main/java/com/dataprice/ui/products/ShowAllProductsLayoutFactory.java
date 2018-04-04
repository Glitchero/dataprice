package com.dataprice.ui.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;
import com.dataprice.service.productstatistics.ProductStatisticsService;
import com.dataprice.service.searchproduct.SearchProductService;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.vaadin.annotations.Push;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
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
	
	private Grid<Product> productsTable;
	
	private TextField search;
	
	private Button searchButton;
	
	private Button refreshButton;
	
	private ComboBox retailFilter;
	
	private Label statusCounter;
	
	private Integer numberOfProducts;
	
	private Integer numberOfProductsWithoutProfile;
	
	private ProgressBar progressBar;
	
	private class ShowAllProductsLayout extends VerticalLayout implements Button.ClickListener,ValueChangeListener, SelectionListener<Product> {

		ProductEditListener productEditListener;
		
		public ShowAllProductsLayout(ProductEditListener productEditListener) {
		   this.productEditListener = productEditListener;
		}


		public ShowAllProductsLayout init() {
			
			//setMargin(true);
			
			//progressBar = new ProgressBar();
			//progressBar.setCaption("Loading Products...");
		    //progressBar.setIndeterminate(true);
		    //progressBar.setVisible(true);
		        
			
			search = new TextField();			
			search.setWidth("100%");
		   	
			searchButton = new Button(FontAwesome.USER);
			searchButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		//	searchButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			searchButton.addClickListener(this);		
		//	searchButton.setWidth("30%");
			
			refreshButton = new Button(FontAwesome.USER);
			refreshButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		//	refreshButton.setStyleName(ValoTheme.BUTTON_DANGER);
			refreshButton.addClickListener(this);		
		//	refreshButton.setWidth("30%");
			
			statusCounter = new Label(numberOfProductsWithoutProfile + "/" + numberOfProducts);
			statusCounter.setWidth("30%");
			
			retailFilter = new ComboBox("Retails");
			retailFilter.setVisible(true);
			retailFilter.setItems("Walmart","Chedraui","SuplementosFitness");
			retailFilter.addValueChangeListener(this);
			
			
			productsTable = new Grid<>(Product.class);
			
			productsTable.setColumnOrder("productId","retail","name", "price", "imageUrl","productUrl","pid","gender","category","subcategory","brand");
			
			productsTable.setVisible(true);
			productsTable.setItems(products);
			
			/**
			if (products.size()!=0) {
				productsTable.select(products.get(0));				
				productEditListener.productEdited(products.get(0)); 
			}
			  */ 
			
			
			/**
			productsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(100,Unit.PIXELS);
			    image.setHeight(100,Unit.PIXELS);

			    return image;
			}).setCaption("Imagen");
			*/
		//	productsTable.addColumn(p ->
		//      "<a target=\"_blank\" href='" + p.getProductUrl() + "' target='_top'>product link</a>",
		//      new HtmlRenderer());
			
			productsTable.removeColumn("imageUrl");
		    productsTable.removeColumn("productUrl");
		    productsTable.removeColumn("productKey");
		    productsTable.removeColumn("task");
		    productsTable.removeColumn("description");
		    
		    productsTable.addSelectionListener(this);
		   // Render a button that edit the data row (item)
		  //  productsTable.addColumn(product -> "Edit",
		  //        new ButtonRenderer(clickEvent -> {
		  //      	  productEditListener.productEdited(clickEvent.getItem());
		  //      	  productsTable.select((Product) clickEvent.getItem());
		  //      }));
		    
			
			//productsTable.setBodyRowHeight(100);
			productsTable.setWidth("100%");
			//productsTable.setHeight("580px");
			return this;
		}
		
		
		public ShowAllProductsLayout load() {
			//loadDataInNewThread();
			products = showAllProductsService.getAllProducts(); 
			numberOfProducts = productStatisticsService.getNumOfProducts(); 
			numberOfProductsWithoutProfile = productStatisticsService.getNumOfProductsWithoutPid();
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
			
		
			//Search section
			HorizontalLayout hl = new HorizontalLayout(search,searchButton,refreshButton);
			hl.setExpandRatio(search, 1);
		//	hl.setComponentAlignment(searchButton, Alignment.BOTTOM_LEFT);
		//	hl.setComponentAlignment(refreshButton, Alignment.BOTTOM_LEFT);
		//	hl.setComponentAlignment(statusCounter, Alignment.MIDDLE_RIGHT);
		//	hl.setSpacing(true);
			hl.setWidth("100%");
			
			HeaderRow row = productsTable.prependHeaderRow();
			row.getCell("retail").setComponent(retailFilter);
			row.getCell("name").setComponent(hl);
		//	VerticalLayout vl = new VerticalLayout(hl,productsTable);
			//vl.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		//	vl.setWidth("100%");
		//	vl.setHeight("580px");
		//	vl.setMargin(false);
		//	return vl;
			
			return productsTable;
		}


		@Override
		public void buttonClick(ClickEvent event) {
			
			 if (event.getSource()==searchButton)	{
				 searchProduct();
             }else {
            	 refresh();
             }
			
		}


		private void searchProduct() {		
		   List<Product> retrieveList =	searchProductService.search(search.getValue());	
           if(retrieveList!=null) 
		      productsTable.setItems(retrieveList);
	    }


		@Override
		public void valueChange(ValueChangeEvent event) {
			if (retailFilter.getValue()!=null) {
		    	String retailValue = retailFilter.getValue().toString();
			    products = showAllProductsService.getAllProductsFromRetail(retailValue);		   
                vaadinHybridMenuUI.access(new Runnable() {     
            	   @Override     
            	   public void run() {         
            		   productsTable.setItems(products); 
            	   }
            	});
		    }
			
		}


		@Override
		public void selectionChange(SelectionEvent<Product> event) {
			 SingleSelectionModel<Product> singleSelect = (SingleSelectionModel<Product>) productsTable.getSelectionModel();
			 if (singleSelect.getSelectedItem().isPresent())
			     productEditListener.productEdited(singleSelect.getSelectedItem().get()); 
		}
	
	}
	
	@Autowired
	private ProductStatisticsService productStatisticsService;
	
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
		products = showAllProductsService.getAllProducts();
		productsTable.setItems(products);
	}



	

}

