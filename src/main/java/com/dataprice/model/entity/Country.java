package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="COUNTRY")
public class Country {

	@Id
	@GeneratedValue
	@Column(name = "country_id")
	private Integer countryId;
	
	@Column(name = "country_name")
	private String countryName;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "nickname")
	private String nickname;
	
}
