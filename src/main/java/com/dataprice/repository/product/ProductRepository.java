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


@Repository
public interface ProductRepository extends JpaRepository<Product,String>{

	@Query("select p from Product p order by p.name")
	List<Product> getAllProducts();	
	
	@Query("select p from Product p where p.pid=:pidId")
	List<Product> getAllPrductsFromPid(@Param("pidId") String pidId);	
	
	@Query("select p from Product p where p.productKey=:productMainKey")
	Product getProductFromKey(@Param("productMainKey") String productKey);	
	
	@Query("select p from Product p where p.subcategory.subcategoryId=:subcategoryKey")
	List<Product> getProductsFromSubcategory(@Param("subcategoryKey") Integer subcategoryKey);
	
	@Query("select p from Product p where p.seller=:sellerName")
	List<Product> getProductsFromSellerName(@Param("sellerName") String sellerName);
	
	@Query("select count(p.productKey) from Product p")
	Integer getNumOfProducts();
	
	@Query("select count(p.productKey) from Product p where p.pid is null")
	Integer getNumOfProductsWithoutPid();
	
	@Query("select p from Product p where p.seller=:sellerName and p.pid is not null and p.pid in (select p.pid from Product p group by p.pid having count(p.pid)> 1)")
	List<Product> getProductsFromSellerNameWithMatches(@Param("sellerName") String sellerName);
	
	@Query("select p from Product p where p.seller=:sellerName and p.pid=:pidId")
	List<Product> getProductsFromSellerNameAndPid(@Param("sellerName") String sellerName, @Param("pidId") String pidId);
	
	@Query("select p.seller from Product p group by p.seller")
	List<String> getSellersList();
	
}
