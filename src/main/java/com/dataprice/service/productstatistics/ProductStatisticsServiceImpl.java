package com.dataprice.service.productstatistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.repository.product.ProductRepository;

@Service
@Transactional(readOnly=true)
public class ProductStatisticsServiceImpl implements ProductStatisticsService {

	@Autowired
	private ProductRepository productRepository;
	
	
	@Override
	public Integer getNumOfProducts() {
		return productRepository.getNumOfProducts();
	}

	@Override
	public Integer getNumOfProductsWithoutPid() {
		return productRepository.getNumOfProductsWithoutPid();
	}

}
