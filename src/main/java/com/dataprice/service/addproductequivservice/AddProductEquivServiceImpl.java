package com.dataprice.service.addproductequivservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.ProductEquivalences;
import com.dataprice.model.entity.ProductHistory;
import com.dataprice.model.entity.ProductHistoryKey;
import com.dataprice.repository.product.ProductRepository;
import com.dataprice.repository.productequivalences.ProductEquivalencesRepository;

@Service
public class AddProductEquivServiceImpl implements AddProductEquivService {

	@Autowired
	private ProductEquivalencesRepository productEquivalencesRepository;

	@Override
	public void saveEquivalency(ProductEquivalences equivalencyDAO) {
		
		
		
		String equivalencyKey = equivalencyDAO.getProductKey();
		
		if (productEquivalencesRepository.exists(equivalencyKey)) {
			ProductEquivalences retrievedEquivalency = productEquivalencesRepository.findOne(equivalencyKey);
			retrievedEquivalency.setSku(equivalencyDAO.getSku());
			retrievedEquivalency.setUpc(equivalencyDAO.getUpc());
			retrievedEquivalency.setBrand(equivalencyDAO.getBrand());
			retrievedEquivalency.setCategory(equivalencyDAO.getCategory());
			productEquivalencesRepository.save(retrievedEquivalency);
		}else {
			ProductEquivalences productEquivalences = new ProductEquivalences();
			productEquivalences.setProductKey(equivalencyDAO.getProductKey());
			productEquivalences.setSku(equivalencyDAO.getSku());
			productEquivalences.setUpc(equivalencyDAO.getUpc());
			productEquivalences.setBrand(equivalencyDAO.getBrand());
			productEquivalences.setCategory(equivalencyDAO.getCategory());
			System.out.println(productEquivalences);
			productEquivalencesRepository.save(productEquivalences);	
		}

	}
	
}
