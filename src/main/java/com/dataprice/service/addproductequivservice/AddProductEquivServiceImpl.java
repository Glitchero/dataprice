package com.dataprice.service.addproductequivservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.ProductEquivalences;
import com.dataprice.repository.product.ProductRepository;
import com.dataprice.repository.productequivalences.ProductEquivalencesRepository;

@Service
@Transactional(readOnly=true)
public class AddProductEquivServiceImpl implements AddProductEquivService {

	@Autowired
	private ProductEquivalencesRepository productEquivalencesRepository;

	@Override
	public void saveEquivalency(ProductEquivalences equivalencyDAO) {
		ProductEquivalences productEquivalences = new ProductEquivalences();
		productEquivalences.setProductKey(equivalencyDAO.getProductKey());
		productEquivalences.setSku(equivalencyDAO.getSku());
		productEquivalences.setUpc(equivalencyDAO.getUpc());
		productEquivalencesRepository.save(productEquivalences);
		
	}
	
}
