package com.dataprice.model.crawlers;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public abstract class AbstractCrawler implements Crawler {

	public AbstractCrawler() {
		
	}
	
	
	@Override
	public String toString()
	{
		return "Crawler wey";
	}	
	

}
