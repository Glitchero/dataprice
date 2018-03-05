package com.dataprice.model.crawlers;

import java.util.List;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public class NutritionDepot extends AbstractCrawler{

	@Override
	public List<Product> getProductsFromTask(Task taskDAO) {
		initialization(taskDAO.getSeed());
		navigatePages();
		return null;
	}

	@Override
	public String getCrawlingStrategy() {
		// TODO Auto-generated method stub
		return null;
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
