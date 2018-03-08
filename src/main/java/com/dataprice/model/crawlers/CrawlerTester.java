package com.dataprice.model.crawlers;

import java.util.List;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public class CrawlerTester {

	public static void main(String[] args) {
		Task t = new Task();
		t.setRetail("Suplementosfitness");
		t.setSeed("https://www.suplementosfitness.com.mx/categoria-producto/proteinas/");
		t.setTaskName("Proteinas");
		
		Crawler c = new SuplementosFitness();

    	//boolean value = c.executeTask(t);
    	
    
	}
}
