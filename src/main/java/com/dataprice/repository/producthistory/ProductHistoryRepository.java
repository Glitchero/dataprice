package com.dataprice.repository.producthistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.ProductHistory;
import com.dataprice.model.entity.ProductHistoryKey;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory,ProductHistoryKey>{

	
	
}
