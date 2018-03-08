package com.dataprice.ui.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.students.UIComponentBuilder;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;

@org.springframework.stereotype.Component
public class ShowAllProductsLayoutFactory {

	private List<Product> products;
	
	private Grid<Product> productsTable;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	private class ShowAllProductsLayout extends VerticalLayout {

		ProductEditListener productEditListener;
		
		public ShowAllProductsLayout(ProductEditListener productEditListener) {
		   this.productEditListener = productEditListener;
		}


		public ShowAllProductsLayout init() {
			
			setMargin(true);
			productsTable = new Grid<>(Product.class);
			productsTable.setWidth("100%");
			
			productsTable.setColumnOrder("productId","retail","name", "precio", "imageUrl","productUrl");
			
			productsTable.setItems(products);
			
			productsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(80,Unit.PIXELS);
			    image.setHeight(80,Unit.PIXELS);

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
			return this;
		}
		
		
		public ShowAllProductsLayout load() {
			products = showAllProductsService.getAllProducts();   			
			return this;
		}
		
		public ShowAllProductsLayout layout() {
			addComponent(productsTable); //Add component to the verticalLayout, That's why we extend the class.
			return this;
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
