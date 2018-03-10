package com.dataprice.service.showallproducts;

import java.util.List;

import com.dataprice.model.entity.Product;

public interface ShowAllProductsService {

	public List<Product> getAllProducts();
	
	public List<Product> getAllProductsFromPid(String pid);
	
	public Product getProductFromKey(String productKey,String retailKey);
}
