package com.dataprice.service.removesubcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Subcategory;
import com.dataprice.repository.subcategory.SubcategoryRepository;

@Service
public class RemoveSubcategoryServiceImpl implements RemoveSubcategoryService{

	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@Override
	public void removeSubcategory(Subcategory subcategory) {
		subcategoryRepository.delete(subcategory);
		
	}

}
