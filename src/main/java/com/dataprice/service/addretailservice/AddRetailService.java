package com.dataprice.service.addretailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Retail;
import com.dataprice.repository.retail.RetailRepository;


@Service
public class AddRetailService {

	@Autowired
	private RetailRepository retailRepository;
	
	public void saveRetail(Retail retailDAO) {
		retailRepository.save(retailDAO);
	}
	
}
