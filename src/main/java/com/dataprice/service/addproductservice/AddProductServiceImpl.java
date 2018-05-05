package com.dataprice.service.addproductservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ProductEquivalences;
import com.dataprice.repository.product.ProductRepository;
import com.dataprice.repository.productequivalences.ProductEquivalencesRepository;

@Service
@Transactional(readOnly=true)
public class AddProductServiceImpl implements AddProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductEquivalencesRepository productEquivalencesRepository;
	
	@Transactional
	public void saveProduct(Product productDAO) {
		
		String productkey = productDAO.getProductKey();
		if (productRepository.exists(productkey)) {
			//Not update Sku and Upc
			Product retrievedProduct = productRepository.findOne(productkey);
			retrievedProduct.setPrice(productDAO.getPrice());
			retrievedProduct.setName(productDAO.getName());
			retrievedProduct.setDescription(productDAO.getDescription());
			retrievedProduct.setImageUrl(productDAO.getImageUrl());
			retrievedProduct.setProductUrl(productDAO.getProductUrl());
			retrievedProduct.setTask(productDAO.getTask());
			retrievedProduct.setUpdateDay(new Date());
			productRepository.save(retrievedProduct);
		}else {
			Product product = new Product();
			product.setProductKey(productDAO.getProductKey());
			product.setProductId(productDAO.getProductId());
			product.setSeller(productDAO.getSeller());
			product.setName(productDAO.getName());
			product.setDescription(productDAO.getDescription());
			product.setPrice(productDAO.getPrice());
			product.setProductUrl(productDAO.getProductUrl());
			product.setImageUrl(productDAO.getImageUrl());
			product.setTask(productDAO.getTask());
			//Added memory for products
			if (productEquivalencesRepository.exists(productDAO.getProductKey())) {
				ProductEquivalences retrievedEquivalency = productEquivalencesRepository.findOne(productDAO.getProductKey());
				product.setSku(retrievedEquivalency.getSku());
				product.setUpc(retrievedEquivalency.getUpc());
				product.setBrand(retrievedEquivalency.getBrand());
				product.setCategory(retrievedEquivalency.getCategory());
				product.setChecked(true);
			}else {
				//Case: There is no memory
				product.setSku(productDAO.getSku());
				product.setUpc(productDAO.getUpc());
				product.setBrand(productDAO.getBrand());
				product.setCategory(productDAO.getCategory());
			}		
			/////

			product.setUpdateDay(productDAO.getUpdateDay());
			productRepository.save(product);	
			
		}	
		
	}
	
}
