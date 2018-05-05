package com.dataprice.service.removeproductequivalency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.ProductEquivalences;
import com.dataprice.repository.productequivalences.ProductEquivalencesRepository;

@Service
public class RemoveProductEquivalencyServiceImpl implements RemoveProductEquivalencyService{

	
	@Autowired
	private ProductEquivalencesRepository productEquivalencesRepository;
	
	@Override
	public void removeEquivalency(ProductEquivalences equivalency) {
		if (productEquivalencesRepository.exists(equivalency.getProductKey()))
		    productEquivalencesRepository.delete(equivalency);	
	}

}
