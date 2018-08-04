package com.dataprice.service.showallproducts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Product;
import com.dataprice.repository.product.ProductRepository;;

@Service
public class ShowAllProductsServiceImpl implements ShowAllProductsService{

	@Autowired
	private ProductRepository productRepository;
	
	
	@Override
	public List<Product> getAllProducts() {
		return productRepository.getAllProducts();
	}


	@Override
	public List<Product> getAllProductsFromSku(String sku) {
		return productRepository.getAllPrductsFromSku(sku);
	}


	@Override
	public Product getProductFromKey(String productKey) {
		return productRepository.getProductFromKey(productKey);
	}


	@Override
	public List<Product> getAllProductsFromSeller(String seller) {
		return productRepository.getProductsFromSellerName(seller);
	}
	
	@Override
	public List<Product> getProductsFromSellerNameWithMatchesSku(String seller) {
		return productRepository.getProductsFromSellerNameWithMatchesSku(seller);
	}
	
	@Override
	public List<Product> getProductsFromSellerNameWithMatchesUpc(String seller) {
		return productRepository.getProductsFromSellerNameWithMatchesUpc(seller);
	}


	@Override
	public List<Product> getProductsFromSellerNameAndSku(String seller, String sku) {
		return productRepository.getProductsFromSellerNameAndSku(seller, sku);
	}

	
	@Override
	public List<Product> getProductsFromSellerNameAndUpc(String seller, String upc) {
		return productRepository.getProductsFromSellerNameAndUpc(seller, upc);
	}
	

	@Override
	public List<String> getSellersList() {
		return productRepository.getSellersList();
	}


	@Override
	public List<String> getSellersListExceptForSeller(String seller) {
		return productRepository.getSellersListExceptForSeller(seller);
	}


	@Override
	public List<Product> getMatchedProducts(String seller, String sku) {
		return productRepository.getMatchedProducts(seller, sku);
	}
	
	
	
	

}
