package com.dataprice.service.showallsubcategories;

import java.util.List;

import com.dataprice.model.entity.Subcategory;

public interface ShowAllSubcategoriesService {

	public List<Subcategory> getAllSubcategories();
	
	public List<Subcategory> getAllSubcategoriesForCategory(Integer categoryKey);
}
