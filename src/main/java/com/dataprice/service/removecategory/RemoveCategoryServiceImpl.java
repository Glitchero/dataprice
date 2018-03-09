package com.dataprice.service.removecategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Category;
import com.dataprice.repository.category.CategoryRepository;

@Service
public class RemoveCategoryServiceImpl implements RemoveCategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public void removeCategory(Category category) {
		categoryRepository.delete(category);
		
	}

}
