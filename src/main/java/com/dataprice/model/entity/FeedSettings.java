package com.dataprice.model.entity;

import java.time.LocalDate;
import java.time.Period;

public class FeedSettings {

	private String seller;  
	
	private LocalDate lastUpdate = LocalDate.now().minus(Period.ofDays(1));
	
    public FeedSettings() {
		//Constructor
	}

    public FeedSettings(Integer days) {
    	this.lastUpdate = LocalDate.now().minus(Period.ofDays(days));
	}
    
    
	public String getSeller() {
		return seller;
	}


	public void setSeller(String seller) {
		this.seller = seller;
	}


	public LocalDate getLastUpdate() {
		return lastUpdate;
	}


	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
    
    
}
