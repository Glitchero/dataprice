package com.dataprice.service.reports;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Product;
import com.dataprice.repository.product.ProductRepository;

@Service
@Transactional(readOnly=true)
public class ReportsServiceImpl implements ReportsService{

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<String> getCompetitorsList(String mySeller) {
		return productRepository.getCompetitorsList(mySeller);
	}

	@Override
	public List<String> getCategoryListForSeller(String mySeller) {
		return productRepository.getCategoryListForSeller(mySeller);
	}

	@Override
	public List<Product> getProductsForPriceMatrixByUpc(String mySeller, Set<String> categories, Date lastDate,
			Set<String> competition) {
		return productRepository.getProductsForPriceMatrixByUpc(mySeller, categories, lastDate, competition);
	}

	@Override
	public List<Product> getProductsForPriceMatrixBySku(String mySeller, Set<String> categories, Date lastDate,
			Set<String> competition) {
		return productRepository.getProductsForPriceMatrixBySku(mySeller, categories, lastDate, competition);

	}


	
	
}
