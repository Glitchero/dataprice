package com.dataprice.model.crawlers.utils;

public class CrawlInfo {

	private String url;
	private double price;
	private String productName;
	
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
	
	
}
