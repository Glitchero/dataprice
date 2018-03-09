package com.dataprice.service.showallcategories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Category;
import com.dataprice.repository.category.CategoryRepository;

@Service
public class ShowAllCategoriesServiceImpl implements ShowAllCategoriesService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.getAllCategories();
	}

}
