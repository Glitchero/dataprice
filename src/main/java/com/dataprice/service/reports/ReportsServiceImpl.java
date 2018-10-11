package com.dataprice.service.reports;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Product;
import com.dataprice.repository.product.ProductRepository;
import com.dataprice.repository.producthistory.ProductHistoryRepository;

@Service
@Transactional(readOnly=true)
public class ReportsServiceImpl implements ReportsService{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductHistoryRepository productHistoryRepository;
	

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

	@Override
	public List<Product> getProductsFromSellerNameAndSku(String seller, String sku, Date lastDate) {
		return productRepository.getProductsFromSellerNameAndSku(seller, sku, lastDate);
	}

	@Override
	public List<Product> getProductsFromSellerNameAndUpc(String seller, String upc, Date lastDate) {
		return productRepository.getProductsFromSellerNameAndUpc(seller, upc, lastDate);
	}

	@Override
	public Double getHistoricalPriceFromKey(String key, Calendar date) {
		return productHistoryRepository.getHistoricalPriceFromKey(key, date);
	}

	@Override
	public List<Product> getProductsForFeedNoMatches(String mySeller, Date lastDate) {
		return productRepository.getProductsForFeedNoMatches(mySeller, lastDate);
	}


	
	
}
