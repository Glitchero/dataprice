package com.dataprice.service.showallproducts;

import java.util.List;
import java.util.Set;

import com.dataprice.model.entity.Product;

public interface ShowAllProductsService {

	public List<Product> getMatchedProducts(String seller,String sku);
	
	public List<Product> getAllProducts();
	
	public List<Product> getAllPrductsForTimePlot(String sku,Set<String> competitors, String mainSeller);
	
	public Product getProductFromKey(String productKey);
	
	public List<Product> getAllProductsFromSeller(String seller);
	
	//Reports
	public List<Product> getProductsFromSellerNameWithMatchesSku(String seller);
	
	public List<Product> getProductsFromSellerNameWithMatchesUpc(String seller);
	
	public List<Product> getProductsFromSellerNameAndSku(String seller,String sku);
	
	public List<Product> getProductsFromSellerNameAndUpc(String seller,String upc);
	
	public List<String> getSellersList();
	
	public List<String> getSellersListExceptForSeller(String seller);
}
