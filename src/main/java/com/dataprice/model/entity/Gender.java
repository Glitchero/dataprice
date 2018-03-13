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
@Table(name="GENDER")
public class Gender {

	@Id
	@GeneratedValue
	@Column(name = "gender_id")
	private Integer genderId;
	
	@Column(name = "gender_name")
	private String genderName;
	
	@OneToMany(mappedBy="gender")
	private List<Product> products;
	
	public Gender() {
	  	  this.products = new ArrayList<>();
    }
	
	public Gender(String genderName) {
		this();
		this.genderName = genderName;
	}
	
	
	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderName() {
		return genderName;
	}
	
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}


	@PreRemove
	private void removeAssociationsWithChilds() {
	   for (Product p : products) {
	        p.setGender(null);
	   }
	}
	
	@Override
	public String toString() {
		return this.genderName;
	}
	
	
}
