package com.dataprice.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;


@Entity
@Table(name="BRAND")
public class Brand {

	@Id
	@GeneratedValue
	@Column(name = "brand_id")
	private Integer brandId;
	
	@Column(name = "brand_name")
	private String brandName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy="brand")
	private List<Product> products;
	
	public Brand() {
	  	  this.products = new ArrayList<>();
  }
	
	public Brand(String brandName) {
		this();
		this.brandName = brandName;
	}
	
	
	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@PreRemove
	private void removeAssociationsWithChilds() {
	   for (Product p : products) {
	        p.setBrand(null);
	   }
	}

	
	@Override
	public String toString() {
		return this.brandName;
	}
}
