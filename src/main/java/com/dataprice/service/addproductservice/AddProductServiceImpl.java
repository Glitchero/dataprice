package com.dataprice.service.addproductservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ProductKey;
import com.dataprice.repository.product.ProductRepository;

@Service
@Transactional(readOnly=true)
public class AddProductServiceImpl implements AddProductService {

	@Autowired
	private ProductRepository productRepository;

	@Transactional
	@Override
	public void saveProduct(Product productDAO) {
		
		ProductKey pk= new ProductKey(productDAO.getProductId(),productDAO.getRetail());
		
		if (productRepository.exists(pk)) {
			Product rp = productRepository.findOne(pk);
			//rp.setPrecio(0.0); This was only for checking that it was working
			productRepository.save(rp);
		}else {
			
			Product product = new Product();
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
