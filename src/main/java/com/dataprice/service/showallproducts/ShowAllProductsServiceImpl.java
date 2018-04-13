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
	public List<Product> getAllProductsFromPid(String pid) {
		return productRepository.getAllPrductsFromPid(pid);
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
	public List<Product> getProductsFromSellerNameWithMatches(String seller) {
		return productRepository.getProductsFromSellerNameWithMatches(seller);
	}


	@Override
	public List<Product> getProductsFromSellerNameAndPid(String seller, String pid) {
		return productRepository.getProductsFromSellerNameAndPid(seller, pid);
	}


	@Override
	public List<String> getSellersList() {
		return productRepository.getSellersList();
	}
	
	
	
	

}
