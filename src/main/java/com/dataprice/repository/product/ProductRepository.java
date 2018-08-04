package com.dataprice.repository.product;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

	@Query("select p from Product p where p.seller<>:sellerName and p.checked=1 and p.sku=:skuKey")
	List<Product> getMatchedProducts(@Param("sellerName") String sellerName,@Param("skuKey") String skuKey);
	
	
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
	

	//**********Important***************
	@Query("select p from Product p where p.seller=:sellerName and p.sku=:skuId and DATE(p.updateDay) >= :lastDate and p.checked=1")
	List<Product> getProductsFromSellerNameAndSku(@Param("sellerName") String sellerName, @Param("skuId") String skuId,@Param("lastDate") Date lastDate);
		

	@Query("select p from Product p where p.seller=:sellerName and p.upc=:upcId and DATE(p.updateDay) >= :lastDate and p.checked=1")
	List<Product> getProductsFromSellerNameAndUpc(@Param("sellerName") String sellerName, @Param("upcId") String upcId,@Param("lastDate") Date lastDate);
	
	
	// Queries for settings!!.
	@Query("select p.seller from Product p group by p.seller")
	List<String> getSellersList();
	
	@Query("select p.seller from Product p where p.seller<>:sellerName group by p.seller")
	List<String> getSellersListExceptForSeller(@Param("sellerName") String sellerName);
	
	/**
	 * Get all the competitors in General for a specific seller.
	 * Used in Reports.
	 * @return
	 */

	@Query("select p.seller from Product p where p.seller<>:sellerName group by p.seller")
	List<String> getCompetitorsList(@Param("sellerName") String sellerName);
	
	
	// Query for reports!!. 
	@Query("select p.task.taskName from Product p where p.seller=:sellerName group by p.task.taskName")
    List<String> getCategoryListForSeller(@Param("sellerName") String sellerName);
	
	
	/**
	 * Query for price matrix report. 
	 * Filter by:
	 * 1.- Filter by SellerName
	 * 2.- Products with Matches in UPC from selected competition
	 * 3.- Filter by category
	 * 4.- Filter by dateRange
	 *  
	 * Used in Dashboard and in reports
	 * @param sellerName
	 * @return
	 */
	
	//**********Important***************
	
	@Query("select p from Product p where p.seller=:sellerName and DATE(p.updateDay) >= :lastDate and p.upc <> '' and p.upc in (select p.upc from Product p where DATE(p.updateDay) >= :lastDate and p.checked=1 and p.seller IN (:competition,:sellerName) group by p.upc having count(p.upc)> 1) and p.checked=1")
	List<Product> getProductsForPriceMatrixByUpc(@Param("sellerName") String sellerName, @Param("lastDate") Date lastDate, @Param("competition") Set<String> competition );
		
	
	//@Query("select p from Product p where p.seller=:sellerName and DATE(p.updateDay) >= :lastDate and p.sku <> '' and p.sku in (select p.sku from Product p where p.seller IN (:competition,:sellerName) group by p.sku having count(p.sku)> 1) and p.checked=1")
	//List<Product> getProductsForPriceMatrixBySku(@Param("sellerName") String sellerName, @Param("lastDate") Date lastDate, @Param("competition") Set<String> competition );
	
	
	@Query("select p from Product p where p.seller=:sellerName and DATE(p.updateDay) >= :lastDate and p.sku <> '' and p.sku in (select p.sku from Product p where DATE(p.updateDay) >= :lastDate and p.checked=1 and p.seller IN (:competition,:sellerName) group by p.sku having count(p.sku)> 1) and p.checked=1")
	List<Product> getProductsForPriceMatrixBySku(@Param("sellerName") String sellerName, @Param("lastDate") Date lastDate, @Param("competition") Set<String> competition );
		
	
	//**********Important***************
	/**
	 * Get all the competitors that have at least one match with the mainSeller. Add later parameter date??? I dont know if its convenient, check it later!!.
	 * Used only in Dashboard
	 * @return
	 */
	@Query("select p.seller from Product p where p.seller<>:mainSeller and p.sku <> '' and p.sku in (select p.sku from Product p group by p.sku having count(p.sku)> 1) and p.checked=1 group by p.seller")
	List<String> getCompetitorsBySku(@Param("mainSeller") String mainSeller);
	

	@Query("select p.seller from Product p where p.seller<>:mainSeller and p.upc <> '' and p.upc in (select p.upc from Product p group by p.upc having count(p.upc)> 1) and p.checked=1 group by p.seller")
	List<String> getCompetitorsByUpc(@Param("mainSeller") String mainSeller);
	
	
	
	

	
	
	
	
	
	/////////------------------Queries Not Used---Delete Them Later-------------------------///////////
	
	
	/** I think this is not used, check it with ctrl + h
	 * Get total of products from competition. Add later parameter date!!
	 * Used in Dashboard
	 * @return
	 */
	@Query("select count(p.productKey) from Product p where p.seller<>:mainSeller and p.sku <> '' and p.sku in (select p.sku from Product p where p.seller IN (:competition,:mainSeller) group by p.sku having count(p.sku)> 1)")
	Integer getTotalOfProductsFromCompetitorBySku(@Param("mainSeller") String mainSeller, @Param("competition") String competition);
	

	@Query("select count(p.productKey) from Product p where p.seller<>:mainSeller and p.upc <> '' and p.upc in (select p.upc from Product p where p.seller IN (:competition,:mainSeller) group by p.upc having count(p.upc)> 1)")
	Integer getTotalOfProductsFromCompetitorByUpc(@Param("mainSeller") String mainSeller,@Param("competition") String competition);
	
	
	@Query("select p from Product p where p.seller=:sellerName and p.sku <> '' and p.sku in (select p.sku from Product p group by p.sku having count(p.sku)> 1)")
	List<Product> getProductsFromSellerNameWithMatchesSku(@Param("sellerName") String sellerName);
	
	@Query("select p from Product p where p.seller=:sellerName and p.upc <> '' and p.upc in (select p.upc from Product p group by p.upc having count(p.upc)> 1)")
	List<Product> getProductsFromSellerNameWithMatchesUpc(@Param("sellerName") String sellerName);
	
	@Query("select p from Product p where p.seller=:sellerName and p.sku=:skuId")
	List<Product> getProductsFromSellerNameAndSku(@Param("sellerName") String sellerName, @Param("skuId") String skuId);
		

	@Query("select p from Product p where p.seller=:sellerName and p.upc=:upcId")
	List<Product> getProductsFromSellerNameAndUpc(@Param("sellerName") String sellerName, @Param("upcId") String upcId);
	
}
