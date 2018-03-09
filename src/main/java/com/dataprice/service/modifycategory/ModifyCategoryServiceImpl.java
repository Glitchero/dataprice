package com.dataprice.service.modifycategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Category;
import com.dataprice.repository.category.CategoryRepository;

@Service
public class ModifyCategoryServiceImpl implements ModifyCategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public void modifyCategory(Category categoryDAO) {
		categoryRepository.save(categoryDAO);	
	}

}
