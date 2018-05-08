package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="RETAIL")
public class Retail {
	//@GeneratedValue
	@Id
	@Column(name = "retail_id")
	private Integer retailId;
	
	@Column(name = "retail_name")
	private String retailName;
	
	@Column(name = "crawler_name")
	private String crawlerName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;

	public Retail() {
		//Default Constructor
	}
	
	public Integer getRetailId() {
		return retailId;
	}

	public void setRetailId(Integer retailId) {
		this.retailId = retailId;
	}

	public String getRetailName() {
		return retailName;
	}

	public void setRetailName(String retailName) {
		this.retailName = retailName;
	}

	public String getCrawlerName() {
		return crawlerName;
	}

	public void setCrawlerName(String crawlerName) {
		this.crawlerName = crawlerName;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return this.retailName;
	}
}
