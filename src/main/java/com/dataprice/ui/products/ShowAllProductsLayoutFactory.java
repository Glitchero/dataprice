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
import com.vaadin.ui.renderers.ImageRenderer;

@org.springframework.stereotype.Component
public class ShowAllProductsLayoutFactory implements UIComponentBuilder {

	private List<Product> products;
	
	private Grid<Product> productsTable;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	private class ShowAllProductsLayout extends VerticalLayout {

		public ShowAllProductsLayout init() {
			
			setMargin(true);
			productsTable = new Grid<>(Product.class);
			productsTable.setWidth("100%");
			
			productsTable.setColumnOrder("name", "precio", "imageUrl","productUrl");
			productsTable.removeColumn("productKey");
			productsTable.setItems(products);
			
			productsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(100,Unit.PIXELS);
			    image.setHeight(100,Unit.PIXELS);

			    return image;
			}).setCaption("Structureee");
			
			productsTable.setBodyRowHeight(80);
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
	

	@Override
	public Component createComponent() {
		return new ShowAllProductsLayout().load().init().layout();
	}

}
