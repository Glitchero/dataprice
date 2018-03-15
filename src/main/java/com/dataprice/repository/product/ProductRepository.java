package com.dataprice.repository.product;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ProductKey;


@Repository
public interface ProductRepository extends JpaRepository<Product,ProductKey>{

	@Query("select p from Product p order by p.name")
	List<Product> getAllProducts();	
	
	@Query("select p from Product p where p.pid=:pidId")
	List<Product> getAllPrductsFromPid(@Param("pidId") String pidId);	
	
	@Query("select p from Product p where p.productId=:productKey and p.retail=:retailKey")
	Product getProductFromKey(@Param("productKey") String productId,@Param("retailKey") String retail);	
	
	@Query("select p from Product p where p.subcategory.subcategoryId=:subcategoryKey")
	List<Product> getProductsFromSubcategory(@Param("subcategoryKey") Integer subcategoryKey);
	
	@Query("select p from Product p where p.retail=:retailName")
	List<Product> getProductsFromRetailName(@Param("retailName") String retailName);
}
