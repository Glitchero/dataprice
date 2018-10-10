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
	
	@Column(name = "country_selected")
	private String countrySelected = null;
	
	@Column(name = "num_retrieved")
	private Double numRetrieved = 10.0;
	 
	@Column(name = "key_type")
	private String keyType ="sku";   //sku or upc
	
	@Column(name = "last_update_in_days")
	private Integer lastUpdateInDays = 1;  
	
	@Column(name = "cores")
	private Integer cores = 1;
	
	@Column(name = "stop_words")
	private String stopWords = "";
	
	/**
	@Column(name = "proxy_host")
	private String proxyHost = "";
	
	@Column(name = "proxy_port")
	private String proxyPort = "";
	
	@Column(name = "proxy_username")
	private String proxyUsername = "";
	
	@Column(name = "proxy_password")
	private String proxyPassword = "";
	*/
	
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


	public Integer getLastUpdateInDays() {
		return lastUpdateInDays;
	}


	public void setLastUpdateInDays(Integer lastUpdateInDays) {
		this.lastUpdateInDays = lastUpdateInDays;
	}


	public Integer getCores() {
		return cores;
	}


	public void setCores(Integer cores) {
		this.cores = cores;
	}


	public String getStopWords() {
		return stopWords;
	}


	public void setStopWords(String stopWords) {
		this.stopWords = stopWords;
	}

    /**
	public String getProxyHost() {
		return proxyHost;
	}


	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}


	public String getProxyPort() {
		return proxyPort;
	}


	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}


	public String getProxyUsername() {
		return proxyUsername;
	}


	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}


	public String getProxyPassword() {
		return proxyPassword;
	}


	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
   */

	public String getCountrySelected() {
		return countrySelected;
	}


	public void setCountrySelected(String countrySelected) {
		this.countrySelected = countrySelected;
	}
	 
	 
	 
}
