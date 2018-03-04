package com.dataprice.service.crawltask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.crawlers.Crawler;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Service
public class CrawlTaskServiceImpl implements CrawlTaskService{

	@Autowired
    private List<Crawler> services;

    private static final Map<String, Crawler> myServiceCache = new HashMap<>();

    @PostConstruct
    public void initMyServiceCache() {
        for(Crawler service : services) {
            myServiceCache.put(service.getCrawlingStrategy(), service);
        }
    }

	@Override
	public Crawler getService(String type) {
		    Crawler service = myServiceCache.get(type);
	        if(service == null) throw new RuntimeException("Unknown service type: " + type);
	        return service;
	}


}
