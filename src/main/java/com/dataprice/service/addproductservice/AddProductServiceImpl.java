package com.dataprice.service.addproductservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Product;
import com.dataprice.repository.product.ProductRepository;

@Service
@Transactional(readOnly=true)
public class AddProductServiceImpl implements AddProductService {

	@Autowired
	private ProductRepository productRepository;

	@Transactional
	public void saveProduct(Product productDAO) {
		
		String productkey = productDAO.getProductKey();
		if (productRepository.exists(productkey)) {
			Product retrievedProduct = productRepository.findOne(productkey);
			retrievedProduct.setPrecio(productDAO.getPrecio());
			retrievedProduct.setName(productDAO.getName());
			retrievedProduct.setImageUrl(productDAO.getImageUrl());
			retrievedProduct.setProductUrl(productDAO.getProductUrl());
			retrievedProduct.setTask(productDAO.getTask());
			
			productRepository.save(retrievedProduct);
		}else {
			
			Product product = new Product();
			product.setProductKey(productDAO.getProductKey());
			product.setProductId(productDAO.getProductId());
			product.setRetail(productDAO.getRetail());
			product.setName(productDAO.getName());
			product.setPrecio(productDAO.getPrecio());
			product.setProductUrl(productDAO.getProductUrl());
			product.setImageUrl(productDAO.getImageUrl());
			product.setTask(productDAO.getTask());
			productRepository.save(product);	
			
		}	
		
	}
	
}
