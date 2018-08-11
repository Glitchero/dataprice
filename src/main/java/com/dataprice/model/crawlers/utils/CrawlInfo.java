package com.dataprice.model.crawlers.utils;

public class CrawlInfo {

	private String url;
	private double price;
	private String productName;
	private String seller;
	private String id;
	private String imageURl;
	private String brand;
	
	
	public CrawlInfo(String url, String brand) {
		this.url = url;
		this.brand = brand;
	}
	
	
	public CrawlInfo(String url,String id, String productName, double price, String imageUrl) {
		this.url = url;	
		this.id = id;
		this.productName = productName;
		this.price = price;	
		this.imageURl = imageUrl;
	}
	
	
	public CrawlInfo(String url, String productName, String imageUrl, double price) {
		this.url = url;	
		this.productName = productName;
		this.price = price;	
		this.imageURl = imageUrl;
	}
	
	
	
	
	public CrawlInfo(String url, String productName, double price, String seller) {
		this.url = url;
		this.productName = productName;
		this.price = price;
		this.seller = seller;
	}
	
	public CrawlInfo(String id, double price, String url) {
		this.id = id;
		this.url = url;
		this.price = price;
	}
	
	
	public CrawlInfo(String url, String productName, double price) {
		this.url = url;
		this.productName = productName;
		this.price = price;
	}
	
	public CrawlInfo(String url, double price) {
		this.url = url;
		this.price = price;
	}
	
	public CrawlInfo(String url) {
		this.url = url;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getImageURl() {
		return imageURl;
	}


	public void setImageURl(String imageURl) {
		this.imageURl = imageURl;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
	
	
}
