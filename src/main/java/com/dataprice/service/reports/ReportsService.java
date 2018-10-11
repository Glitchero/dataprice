package com.dataprice.service.reports;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dataprice.model.entity.Product;

public interface ReportsService {

	public List<String> getCompetitorsList(String mySeller);
	
	List<String> getCategoryListForSeller(String mySeller);
	
	List<Product> getProductsForPriceMatrixByUpc(String mySeller,Date lastDate,Set<String> competition);

	List<Product> getProductsForPriceMatrixBySku(String mySeller,Date lastDate,Set<String> competition);

	public List<Product> getProductsFromSellerNameAndSku(String seller,String sku,Date lastDate);
	
	public List<Product> getProductsFromSellerNameAndUpc(String seller,String upc,Date lastDate);
	
	public Double getHistoricalPriceFromKey(String key, Calendar date); 
	
	List<Product> getProductsForFeedNoMatches(String mySeller,Date lastDate);
	
}
