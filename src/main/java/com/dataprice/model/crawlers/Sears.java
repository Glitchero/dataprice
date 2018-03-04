package com.dataprice.model.crawlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Component
public class Sears extends AbstractCrawler{

	@Override
	public List<Product> getProductsFromTask(Task taskDAO) {
		List<Product> products = new ArrayList<Product>();
		Product p1 = new Product("Zapato",5.0,"No disponible","No disponible");
		Product p2 = new Product("Proteina",15.0,"No disponible","No disponible");
		Product p3 = new Product("Camisa",5.0,"No disponible","No disponible");
		Product p4 = new Product("Cama",15.0,"No disponible","No disponible");
		Product p5 = new Product("Proteina",105.0,"No disponible","No disponible");
		Product p6 = new Product("Chamarra",105.0,"No disponible","No disponible");
		
		products.add(p1);
		products.add(p2);
		products.add(p3);
		products.add(p4);
		products.add(p5);
		products.add(p6);
		
		try        
    	{
    	    Thread.sleep(5000);
    	} 
    	catch(InterruptedException ex) 
    	{
    	    Thread.currentThread().interrupt();
    	}
		
		return products;
	}

	@Override
	public String getCrawlingStrategy() {
		return "Sears";
	}

}
