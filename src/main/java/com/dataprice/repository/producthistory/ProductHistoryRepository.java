package com.dataprice.repository.producthistory;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.ProductHistory;
import com.dataprice.model.entity.ProductHistoryKey;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory,ProductHistoryKey>{

	@Query("select h.price from ProductHistory h where h.productHistoryKey.productKey=:key and h.productHistoryKey.day=:day")
	Double getHistoricalPriceFromKey(@Param("key") String key,@Param("day") Calendar day);
	
}
