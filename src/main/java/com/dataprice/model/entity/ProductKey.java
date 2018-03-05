package com.dataprice.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductKey implements Serializable{

	@Column(name = "product_id", nullable = false)
    private String productId;
	
	@Column(name = "retail", nullable = false)
    private String retail;

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
