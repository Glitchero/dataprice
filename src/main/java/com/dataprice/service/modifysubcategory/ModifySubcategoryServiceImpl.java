package com.dataprice.service.modifysubcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Subcategory;
import com.dataprice.repository.subcategory.SubcategoryRepository;

@Service
public class ModifySubcategoryServiceImpl implements ModifySubcategoryService {

	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@Override
	public void modifySubcategory(Subcategory subcategoryDAO) {
		subcategoryRepository.save(subcategoryDAO);
	}

}
