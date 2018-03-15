package com.dataprice.repository.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataprice.model.entity.Brand;


public interface BrandRepository extends JpaRepository<Brand,Integer>{

	@Query("select b from Brand b order by b.brandName")
	List<Brand> getAllBrands();
	
	@Query("select b from Brand b where b.category.categoryId=:categoryKey")
	List<Brand> getAllBrandsForCategory(@Param("categoryKey") Integer categoryKey);
	
}