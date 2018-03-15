package com.dataprice.service.removebrand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Brand;
import com.dataprice.repository.brand.BrandRepository;

@Service
public class RemoveBrandServiceImpl implements RemoveBrandService{

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public void removeBrand(Brand brand) {
		brandRepository.delete(brand);
		
	}
}
