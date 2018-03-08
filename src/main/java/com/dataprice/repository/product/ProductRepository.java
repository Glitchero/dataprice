package com.dataprice.repository.product;

import java.util.List;

import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ProductKey;
import com.dataprice.model.entity.Student;

@Repository
public interface ProductRepository extends JpaRepository<Product,ProductKey>{

	@Query("select p from Product p order by p.name")
	List<Product> getAllProducts();	
}
