package com.dataprice.service.removeproduct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;
import com.dataprice.repository.gender.GenderRepository;
import com.dataprice.repository.product.ProductRepository;

@Service
public class RemoveProductServiceImpl implements RemoveProductService{

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void removeAllProductsFromRetailName(String retailName) {
		     List<Product> products = productRepository.getProductsFromRetailName(retailName);
		     
		     for (Product p : products) {
		    	 productRepository.delete(p);
		     }
		
	}
	


}
