package com.dataprice.model.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
	private java.util.Calendar day;

	public ProductHistoryKey() {
		
	}
	
	public ProductHistoryKey(String productKey) {
		this.productKey = productKey;
		java.util.Calendar today = java.util.Calendar.getInstance();
		today.set(java.util.Calendar.HOUR_OF_DAY, 0);
		day = today;
	}
	

	
	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}

	public java.util.Calendar getDay() {
		return day;
	}

	public void setDay(java.util.Calendar day) {
		this.day = day;
	}
		
}
