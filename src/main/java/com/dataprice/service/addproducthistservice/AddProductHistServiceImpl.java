package com.dataprice.service.addproducthistservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.ProductHistory;
import com.dataprice.model.entity.ProductHistoryKey;
import com.dataprice.repository.product.ProductRepository;
import com.dataprice.repository.producthistory.ProductHistoryRepository;

@Service
@Transactional(readOnly=true)
public class AddProductHistServiceImpl implements AddProductHistService{

	@Autowired
	private ProductHistoryRepository productHistoryRepository;
	
	@Transactional
	public void saveProductHist(ProductHistory productHistDAO) {
		ProductHistoryKey productHistoryKey = productHistDAO.getProductHistoryKey();
		
		if (productHistoryRepository.exists(productHistoryKey)) {
			ProductHistory retrievedProductHistory = productHistoryRepository.findOne(productHistoryKey);
			retrievedProductHistory.setPrice(productHistDAO.getPrice());
			productHistoryRepository.save(retrievedProductHistory);
		}else {
			ProductHistory productHist = new ProductHistory();
			productHist.setProductHistoryKey(productHistDAO.getProductHistoryKey());
			productHist.setPrice(productHistDAO.getPrice());
			productHistoryRepository.save(productHist);	
		}	
		
	}

}
