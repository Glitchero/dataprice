package com.dataprice.model.entity;

public class Product {

	private String name;
	//Faltaría Cadena
	private Double precio;
	private String imageUrl;
	private String productUrl;
	
	public Product(String name, Double precio, String imageUrl, String productUrl) {
		super();
		this.name = name;
		this.precio = precio;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
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
	
	@Override
	public String toString() {
		return name + "-" + precio;	
	}
	
}
