package com.dataprice.service.addbrand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Brand;
import com.dataprice.model.entity.Subcategory;
import com.dataprice.repository.brand.BrandRepository;

@Service
public class AddBrandServiceImpl implements AddBrandService{

	@Autowired
	private BrandRepository brandRepository;
	
	@Override
	public void saveBrand(Brand brandDAO) {
		Brand brand = new Brand();
		brand.setBrandName(brandDAO.getBrandName());
		brand.setCategory(brandDAO.getCategory());
		brandRepository.save(brand);
	}

}
