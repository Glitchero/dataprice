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
	public Product getProductFromKey(String productKey, String retailKey) {
		return productRepository.getProductFromKey(productKey,retailKey);
	}
	

	

}