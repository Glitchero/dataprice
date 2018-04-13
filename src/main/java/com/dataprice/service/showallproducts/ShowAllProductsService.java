package com.dataprice.service.showallproducts;

import java.util.List;

import com.dataprice.model.entity.Product;

public interface ShowAllProductsService {

	public List<Product> getAllProducts();
	
	public List<Product> getAllProductsFromPid(String pid);
	
	public Product getProductFromKey(String productKey);
	
	public List<Product> getAllProductsFromSeller(String seller);
	
	public List<Product> getProductsFromSellerNameWithMatches(String seller);
	
	public List<Product> getProductsFromSellerNameAndPid(String seller,String pid);
	
	public List<String> getSellersList();
}
