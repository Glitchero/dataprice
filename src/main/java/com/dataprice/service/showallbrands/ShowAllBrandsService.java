package com.dataprice.service.showallbrands;

import java.util.List;

import com.dataprice.model.entity.Brand;

public interface ShowAllBrandsService {

    public List<Brand> getAllBrands();
	
	public List<Brand> getAllBrandsForCategory(Integer categoryKey);
}
