package com.dataprice.service.addcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Category;

import com.dataprice.repository.category.CategoryRepository;

@Service
public class AddCategoryServiceImpl implements AddCategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public void saveCategory(Category categoryDAO) {	
		Category category = new Category();
		category.setCategoryName(categoryDAO.getCategoryName());
		categoryRepository.save(category);
	}

}
