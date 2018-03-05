package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="PRODUCT")
public class Product {
	
	@EmbeddedId
    private ProductKey productKey;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "precio")
	private Double precio;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "product_url")
	private String productUrl;
	
	
	public Product() {
		
	}
	
	
	public Product(String name, Double precio, String imageUrl, String productUrl,ProductKey productKey) {
		this.name = name;
		this.precio = precio;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
	    this.productKey = productKey;
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
	
	public ProductKey getProductKey() {
		return productKey;
	}


	public void setProductKey(ProductKey productKey) {
		this.productKey = productKey;
	}


	@Override
	public String toString() {
		return name + "-" + precio;	
	}
	
}
