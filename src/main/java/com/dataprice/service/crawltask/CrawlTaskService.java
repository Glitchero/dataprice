package com.dataprice.service.crawltask;

import java.util.List;

import com.dataprice.model.crawlers.Crawler;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public interface CrawlTaskService {


    /**
     * Retrieve all the products from a specific task.
     *
     * @param task
     * @return list of products
     */
	 Crawler getService(String type);
    
}
