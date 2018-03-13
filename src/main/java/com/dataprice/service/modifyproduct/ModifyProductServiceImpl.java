package com.dataprice.service.modifyproduct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Category;
import com.dataprice.model.entity.Product;
import com.dataprice.repository.product.ProductRepository;

@Service
public class ModifyProductServiceImpl implements ModifyProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public void modifyProduct(Product productDAO) {
		productRepository.save(productDAO);
		
	}

	@Override
	public void updateCategoryFromSubcategory(Integer categoryKey, Integer subcategoryKey) {
	List<Product> products = productRepository.getProductsFromSubcategory(subcategoryKey);
	for (Product p : products) {
		p.getCategory().setCategoryId(categoryKey);
		productRepository.save(p);
	}	
	}

}
