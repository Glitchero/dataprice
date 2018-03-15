package com.dataprice.service.showallbrands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Brand;
import com.dataprice.repository.brand.BrandRepository;

@Service
public class ShowAllBrandsServiceImpl implements ShowAllBrandsService {

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public List<Brand> getAllBrands() {
		return brandRepository.getAllBrands();
	}

	@Override
	public List<Brand> getAllBrandsForCategory(Integer categoryKey) {
		return brandRepository.getAllBrandsForCategory(categoryKey);
	}
	
}
