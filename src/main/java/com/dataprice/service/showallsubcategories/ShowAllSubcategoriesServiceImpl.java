package com.dataprice.service.showallsubcategories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Subcategory;
import com.dataprice.repository.subcategory.SubcategoryRepository;

@Service
public class ShowAllSubcategoriesServiceImpl implements ShowAllSubcategoriesService{

	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@Override
	public List<Subcategory> getAllSubcategories() {
		return subcategoryRepository.getAllSubcategories();
	}

	@Override
	public List<Subcategory> getAllSubcategoriesForCategory(Integer categoryKey) {
		return subcategoryRepository.getAllSubcategoriesForCategory(categoryKey);
	}

}
