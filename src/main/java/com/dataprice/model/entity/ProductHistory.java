package com.dataprice.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Store the history of the products.
 * @author rene
 *
 */

@Entity
@Table(name="PRODUCTHISTORY")
public class ProductHistory {

	@EmbeddedId
    private ProductHistoryKey productHistoryKey;
	
	@Column(name = "price")
    private Double price;
	
	public ProductHistory(){
		
	}
	
	public ProductHistory(ProductHistoryKey productHistoryKey, Double price){
		this.productHistoryKey = productHistoryKey;
		this.price = price;
	}
	
	public ProductHistoryKey getProductHistoryKey() {
		return productHistoryKey;
	}


	public void setProductHistoryKey(ProductHistoryKey productHistoryKey) {
		this.productHistoryKey = productHistoryKey;
	}


	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}


	
}
