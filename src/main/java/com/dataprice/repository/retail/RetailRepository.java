package com.dataprice.repository.retail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Retail;

@Repository
public interface RetailRepository extends JpaRepository<Retail,Integer>{
	
	@Query("select r from Retail r order by r.retailName")
	List<Retail> getAllRetailers();	
	
	@Query("select r from Retail r where r.retailId=:retailKey")
	Retail getRetailFromId(@Param("retailKey") Integer retailKey);

}
