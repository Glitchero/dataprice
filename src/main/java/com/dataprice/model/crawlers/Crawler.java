package com.dataprice.model.crawlers;

import java.util.List;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public interface Crawler {

	//public List<Product> getProductsFromTask(Task taskDAO);
	 
	//public boolean executeTask(Task taskDAO);
	
	public List<String> getUrlsFromTask(Task taskDAO);
	
	public Product parseProductFromURL(String url);
	
	public String getCrawlingStrategy();
	 
}
