package com.dataprice.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class ProductHistoryKey implements Serializable{

	@Column(name = "ProductKey", nullable = false)
	private String productKey;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "Day", nullable = false)
	private Date day;

	public ProductHistoryKey() {
		
	}
	
	public ProductHistoryKey(String productKey) {
		this.productKey = productKey;
		day = new Date();
	}
	

	
	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}
		
}
