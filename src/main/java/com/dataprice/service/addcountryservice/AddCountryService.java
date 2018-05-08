package com.dataprice.service.addcountryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Country;
import com.dataprice.repository.country.CountryRepository;


@Service
public class AddCountryService {

	@Autowired
	private CountryRepository countryRepository;
	
	public void saveCountry(Country countryDAO) {
		countryRepository.save(countryDAO);
	}
	
}
