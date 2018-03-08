package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	
	public Gender() {
	  	  
    }
	
	
	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}


	@Override
	public String toString() {
		return this.genderName;
	}
	
	
}
