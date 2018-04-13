package com.dataprice.service.showallretails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Retail;
import com.dataprice.repository.retail.RetailRepository;

@Service
public class ShowAllRetailsServiceImpl implements ShowAllRetailsService{
	
	@Autowired
	private RetailRepository retailersRepository;
	
	@Override
	public List<Retail> getAllRetailers() {
		return retailersRepository.getAllRetailers();
	}
	
	
}
