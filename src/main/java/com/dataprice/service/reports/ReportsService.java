package com.dataprice.service.reports;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dataprice.model.entity.Product;

public interface ReportsService {

	public List<String> getCompetitorsList(String mySeller);
	
	List<String> getCategoryListForSeller(String mySeller);
	
	List<Product> getProductsForPriceMatrixByUpc(String mySeller,Set<String> categories,Date lastDate,Set<String> competition);

	List<Product> getProductsForPriceMatrixBySku(String mySeller,Set<String> categories,Date lastDate,Set<String> competition);

}
