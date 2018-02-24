package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="STUDENT")
public class Student {
	
	  @Id
	  @GeneratedValue
	  @Column(name = "id")
      private Integer id;
      
	  @Column(name = "first_name")
      private String firstName;
      
	  @Column(name = "last_name")
      private String lastName;
      
	  @Column(name = "age")
      private Integer age;
       
	  @Column(name = "gender")
      private String gender;
      
      public Student() {
    	  
      }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
   
	@Override
	public String toString() {
		return firstName + "-" + lastName + "-" + age;
		
	}
}
