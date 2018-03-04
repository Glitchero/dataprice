package com.dataprice.model.crawlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Component
public class Liverpool extends AbstractCrawler{

	@Override
	public List<Product> getProductsFromTask(Task taskDAO) {
		List<Product> products = new ArrayList<Product>();
		Product p1 = new Product("Rompecabezas",100.9,"No disponible","No disponible");
		Product p2 = new Product("Nintendo",100.9,"No disponible","No disponible");
		Product p3 = new Product("Chamarra",100.9,"No disponible","No disponible");
		Product p4 = new Product("Cama",100.9,"No disponible","No disponible");
		Product p5 = new Product("Silla",100.9,"No disponible","No disponible");
		Product p6 = new Product("Blusa",100.9,"No disponible","No disponible");
		
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
		return "Liverpool";
	}

}
