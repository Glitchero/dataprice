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
	
	@Query("select p from Product p where p.sku=:skuId")
	List<Product> getAllPrductsFromSku(@Param("skuId") String skuId);	
	
	@Query("select p from Product p where p.productKey=:productMainKey")
	Product getProductFromKey(@Param("productMainKey") String productKey);	
		
	@Query("select p from Product p where p.seller=:sellerName")
	List<Product> getProductsFromSellerName(@Param("sellerName") String sellerName);
	
	@Query("select count(p.productKey) from Product p")
	Integer getNumOfProducts();
	
	
	//Queries for reports for both squ and upc!!
	/**
	@Query("select p from Product p where p.seller=:sellerName and p.sku is not null and p.sku in (select p.sku from Product p group by p.sku having count(p.sku)> 1)")
	List<Product> getProductsFromSellerNameWithMatches(@Param("sellerName") String sellerName);
	
	@Query("select p from Product p where p.seller=:sellerName and p.sku=:skuId")
	List<Product> getProductsFromSellerNameAndSku(@Param("sellerName") String sellerName, @Param("skuId") String skuId);
	*/
	
	@Query("select p from Product p where p.seller=:sellerName and p.sku <> '' and p.sku in (select p.sku from Product p group by p.sku having count(p.sku)> 1)")
	List<Product> getProductsFromSellerNameWithMatchesSku(@Param("sellerName") String sellerName);
	
	@Query("select p from Product p where p.seller=:sellerName and p.sku=:skuId")
	List<Product> getProductsFromSellerNameAndSku(@Param("sellerName") String sellerName, @Param("skuId") String skuId);
	
	
	@Query("select p from Product p where p.seller=:sellerName and p.upc <> '' and p.upc in (select p.upc from Product p group by p.upc having count(p.upc)> 1)")
	List<Product> getProductsFromSellerNameWithMatchesUpc(@Param("sellerName") String sellerName);
	
	@Query("select p from Product p where p.seller=:sellerName and p.upc=:upcId")
	List<Product> getProductsFromSellerNameAndUpc(@Param("sellerName") String sellerName, @Param("upcId") String upcId);
	
	
	// Query for settings!!.
	@Query("select p.seller from Product p group by p.seller")
	List<String> getSellersList();
	
}
