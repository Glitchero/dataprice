package com.dataprice.model.crawlers;

import java.util.List;

import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public interface Crawler {

	//public List<Product> getProductsFromTask(Task taskDAO);
	 
	//public boolean executeTask(Task taskDAO);
	
	public List<CrawlInfo> getUrlsFromTask(Task taskDAO);
	
	public Product parseProductFromURL(CrawlInfo crawlInfo,Task taskDAO);
	
	public String getCrawlingStrategy();
	 
}
