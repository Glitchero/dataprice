package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="COUNTRY")
public class Country {
	
	//@GeneratedValue
	@Id
	@Column(name = "country_id")
	private Integer countryId;
	
	@Column(name = "country_name")
	private String countryName;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "nickname")
	private String nickname;
	
	public Country() {
		//Default Constructor
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	
	
	
}
