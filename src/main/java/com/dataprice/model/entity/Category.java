package com.dataprice.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name="CATEGORY")
public class Category {

	@Id
	@GeneratedValue
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@OneToMany(mappedBy="category")
	private List<Product> products;
	
	public Category() {
	  	  this.products = new ArrayList<>();
    }
	
	public Category(String categoryName) {
		this();
		this.categoryName = categoryName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	@PreRemove
	private void removeAssociationsWithChilds() {
	   for (Product p : products) {
	        p.setCategory(null);
	   }
	}
	
	@Override
	public String toString() {
		return this.categoryName;
	}
	
	
}
