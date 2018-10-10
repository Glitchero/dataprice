package com.dataprice.service.showallcountries;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Country;
import com.dataprice.repository.country.CountryRepository;

@Service
@Transactional(readOnly=true)
public class ShowAllCountriesServiceImpl implements ShowAllCountriesService{

	@Autowired
	private CountryRepository countryRepository;
	
	@Override
	public List<String> getAllCountriesNames() {
		return countryRepository.getAllCountriesNames();
	}

}
