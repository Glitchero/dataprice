package com.dataprice.repository.subcategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataprice.model.entity.Subcategory;


public interface SubcategoryRepository extends JpaRepository<Subcategory,Integer>{

	@Query("select s from Subcategory s order by s.subcategoryName")
	List<Subcategory> getAllSubcategories();
	
	@Query("select s from Subcategory s where s.category.categoryId=:categoryKey")
	List<Subcategory> getAllSubcategoriesForCategory(@Param("categoryKey") Integer categoryKey);
	
}
