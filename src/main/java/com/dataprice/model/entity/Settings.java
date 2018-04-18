package com.dataprice.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Store the settings for a specific user.
 * @author rene
 *
 */

@Entity
@Table(name="SETTINGS")
public class Settings {

	/**
	 * Id automatically populated with the user_id. 
	 * Important to define default values for new users.
	 */
	@Id
	@Column(unique = true, nullable = false)
	private Integer id;
		
	/** user with this associated settings */
	@MapsId
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private User user;
	
	@Column(name = "main_seller")
	private String mainSeller = null;	
	
	@Column(name = "num_retrieved")
	private Double numRetrieved = 10.0;
	 
	@Column(name = "key_type")
	private String keyType ="sku";   //sku or upc
	
    public Settings() {
    	
    }
	 

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}



	public String getMainSeller() {
		return mainSeller;
	}

	public void setMainSeller(String mainSeller) {
		this.mainSeller = mainSeller;
	}

	public Double getNumRetrieved() {
		return numRetrieved;
	}

	public void setNumRetrieved(Double numRetrieved) {
		this.numRetrieved = numRetrieved;
	}

	public String getKeyType() {
		return keyType;
	}


	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	 
	 
	 
}
