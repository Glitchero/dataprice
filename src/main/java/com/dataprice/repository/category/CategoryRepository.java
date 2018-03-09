package com.dataprice.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{

	@Query("select c from Category c order by c.categoryName")
	List<Category> getAllCategories();
}
