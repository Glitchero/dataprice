package com.dataprice.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


public class ProductKey implements Serializable{

	
    private String productId;
	
	
    private String retail;


	public ProductKey(String productId, String retail) {
		this.productId = productId;
		this.retail = retail;
	}

	public ProductKey() {
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRetail() {
		return retail;
	}

	public void setRetail(String retail) {
		this.retail = retail;
	}
	
    
}
