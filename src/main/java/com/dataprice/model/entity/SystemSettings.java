package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Store a specific setting for the isntance.
 * @author rene
 *
 */

@Entity
@Table(name="SYSTEMSETTINGS")
public class SystemSettings {
	
	
	@Id
	@GeneratedValue
	@Column(name = "system_settings_id")
	private Integer systemSettingsId;
	
	@Column(name = "main_seller")
	private String mainSeller = null;	
	
	@Column(name = "num_retrieved")
	private Double numRetrieved = 10.0;
	 
	@Column(name = "key_type")
	private String keyType ="sku";   //sku or upc
	
	@Column(name = "last_update_in_days")
	private Integer lastUpdateInDays = 1;   
	
	
	public SystemSettings() {
	    	
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

	public Integer getLastUpdateInDays() {
		return lastUpdateInDays;
	}

	public void setLastUpdateInDays(Integer lastUpdateInDays) {
		this.lastUpdateInDays = lastUpdateInDays;
	}
	 
	
	
	

}
