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
		return null;
	}

	@Override
	public String getCrawlingStrategy() {
		return "Sears";
	}

	@Override
	public boolean initialization(String seed) {
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigatePages() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getProductsUrl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Product parseProductFromURL(String urlStr) {
		// TODO Auto-generated method stub
		return null;
	}

}
