package com.dataprice.service.modifybrand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Brand;
import com.dataprice.repository.brand.BrandRepository;

@Service
public class ModifyBrandServiceImpl implements ModifyBrandService{

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public void modifyBrand(Brand brandDAO) {
		brandRepository.save(brandDAO);
		
	}
	
}
