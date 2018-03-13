package com.dataprice.service.addsubcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Subcategory;
import com.dataprice.repository.subcategory.SubcategoryRepository;

@Service
public class AddSubcategoryServiceImpl implements AddSubcategoryService{

	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@Override
	public void saveSubcategory(Subcategory subcategoryDAO) {
		Subcategory subcategory = new Subcategory();
		subcategory.setSubcategoryName(subcategoryDAO.getSubcategoryName());
		subcategory.setCategory(subcategoryDAO.getCategory());
		subcategoryRepository.save(subcategory);
	}

}
