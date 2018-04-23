package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Store product equivalences in order to remember the last configuration.
 * @author rene
 *
 */

@Entity
@Table(name="PRODUCTEQUIVALENCES")
public class ProductEquivalences {
	
	@Id
	private String productKey;
	
	@Column(name = "sku")
	private String sku;
	
	@Column(name = "upc")
	private String upc;

	
	
	public ProductEquivalences() {
	}

	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	
	
	
}
