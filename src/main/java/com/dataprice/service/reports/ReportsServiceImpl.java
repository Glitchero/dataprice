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
	public List<Product> getProductsForPriceMatrixByUpc(String mySeller, Date lastDate, Set<String> competition) {
		return productRepository.getProductsForPriceMatrixByUpc(mySeller, lastDate, competition);
	}

	@Override
	public List<Product> getProductsForPriceMatrixBySku(String mySeller, Date lastDate, Set<String> competition) {
		return productRepository.getProductsForPriceMatrixBySku(mySeller, lastDate, competition);

	}


	
	
}
