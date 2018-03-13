package com.dataprice.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name="SUBCATEGORY")
public class Subcategory {

	@Id
	@GeneratedValue
	@Column(name = "subcategory_id")
	private Integer subcategoryId;
	
	@Column(name = "subcategory_name")
	private String subcategoryName;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy="subcategory")
	private List<Product> products;

	
	public Subcategory() {
	  	  this.products = new ArrayList<>();
    }
	
	public Subcategory(String subcategoryName) {
		this();
		this.subcategoryName = subcategoryName;
	}
	
	
	public Integer getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Integer subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public String getSubcategoryName() {
		return subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
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
	        p.setSubcategory(null);
	   }
	}
	
	//@PostPersist
	//private void forceCategoryAssociationsWithChilds() {
	//	   for (Product p : products) {
	//	        p.setCategory(category);
	//	   }
	//	}
	
	@Override
	public String toString() {
		return this.subcategoryName;
	}
	
	
}
