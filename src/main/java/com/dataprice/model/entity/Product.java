package com.dataprice.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="PRODUCT")
@IdClass(ProductKey.class)
public class Product {
	
		
	@Id
	private String productId;
	 
	@Id
	private String retail;
	   
	@Column(name = "name")
	private String name;
	
	@Column(name = "precio")
	private Double precio;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "product_url")
	private String productUrl;
	
	//private String brandHelper;
	
	//private String skuHelper;
	
	//private String status;
	
	@Column(name = "pid")
	private String pid;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "gender_id")
	private Gender gender;
		

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "subcategory_id")
	private Subcategory subcategory;
	
	
	public Product() {
		
	}
	
	
	public Product(String productId,String retail,String name, Double precio, String imageUrl, String productUrl) {
		this.productId = productId;
		this.retail = retail;
		this.name = name;
		this.precio = precio;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
	}
	
	/**
	public Product(String productId,String retail,String name, Double precio, String imageUrl, String productUrl, Gender gender) {
		this.productId = productId;
		this.retail = retail;
		this.name = name;
		this.precio = precio;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
		this.gender = gender;
	}
   */
	
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

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getPrecio() {
		return precio;
	}
	
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getProductUrl() {
		return productUrl;
	}
	
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}
	
	
	@Override
	public String toString() {
		return name + "-" + precio;	
	}
	
}
