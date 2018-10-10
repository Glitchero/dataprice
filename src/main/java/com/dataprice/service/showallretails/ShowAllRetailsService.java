package com.dataprice.service.showallretails;

import java.util.List;

import com.dataprice.model.entity.Retail;

public interface ShowAllRetailsService {

	public List<Retail> getAllRetailers();
	
	public List<Retail> getAllRetailersFromCountry(String Country);
	
	public Retail getRetailFromId(Integer id);
}
