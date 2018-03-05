package com.dataprice.service.addproductservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Product;
import com.dataprice.repository.product.ProductRepository;

@Service
public class AddProductServiceImpl implements AddProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void saveProduct(Product productDAO) {
		Product product = new Product();
		product.setName(productDAO.getName());
		product.setPrecio(productDAO.getPrecio());
		product.setProductUrl(productDAO.getProductUrl());
		product.setImageUrl(productDAO.getImageUrl());
		product.setProductKey(productDAO.getProductKey());
		productRepository.save(product);
	}
	
}
