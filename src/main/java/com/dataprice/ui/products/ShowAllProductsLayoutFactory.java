package com.dataprice.ui.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class ShowAllProductsLayoutFactory {

	private List<Product> products;
	
	private Grid<Product> productsTable;
	
	private TextField search;
	
	private Button searchButton;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	private class ShowAllProductsLayout extends VerticalLayout implements Button.ClickListener {

		ProductEditListener productEditListener;
		
		public ShowAllProductsLayout(ProductEditListener productEditListener) {
		   this.productEditListener = productEditListener;
		}


		public ShowAllProductsLayout init() {
			
			setMargin(true);
			
			search = new TextField("Search");			
			search.setWidth("100%");
		   	
			searchButton = new Button("Search");
			searchButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			searchButton.addClickListener(this);		
			searchButton.setWidth("30%");
			
			productsTable = new Grid<>(Product.class);
			productsTable.setWidth("100%");
			productsTable.setHeight("100%");
			
			productsTable.setColumnOrder("productId","retail","name", "precio", "imageUrl","productUrl","pid","gender","category","subcategory","brand");
			productsTable.removeColumn("task");
			
			productsTable.setItems(products);
			
			productsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(100,Unit.PIXELS);
			    image.setHeight(100,Unit.PIXELS);

			    return image;
			}).setCaption("Structureee");
			
			productsTable.addColumn(p ->
		      "<a target=\"_blank\" href='" + p.getProductUrl() + "' target='_top'>product link</a>",
		      new HtmlRenderer());
			
			productsTable.removeColumn("imageUrl");
		    productsTable.removeColumn("productUrl");
		    
		   // Render a button that deletes the data row (item)
		    productsTable.addColumn(product -> "Edit",
		          new ButtonRenderer(clickEvent -> {
		        	  productEditListener.productEdited(clickEvent.getItem());
		        }));
		    
			
			productsTable.setBodyRowHeight(100);
			productsTable.setHeight("500px");
			return this;
		}
		
		
		public ShowAllProductsLayout load() {
			products = showAllProductsService.getAllProducts();   			
			return this;
		}
		
		public ShowAllProductsLayout layout() {
			HorizontalLayout hl = new HorizontalLayout(search,searchButton);
			hl.addComponent(search);
			hl.addComponent(searchButton); //Add component to the verticalLayout, That's why we extend the class.
			//hl.setComponentAlignment(search, Alignment.MIDDLE_CENTER);
			hl.setComponentAlignment(searchButton, Alignment.BOTTOM_LEFT);
			hl.setSpacing(true);
			hl.setWidth("70%");
			addComponent(hl);
			addComponent(productsTable);
			return this;
		}


		@Override
		public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	

	
	public Component createComponent(ProductEditListener productEditListener) {
		return new ShowAllProductsLayout(productEditListener).load().init().layout();
	}



	public void refresh() {
		products = showAllProductsService.getAllProducts();
		productsTable.setItems(products);
	}



	

}
