package com.dataprice.model.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReportSettings {

//	private Set<String> categories;
	
	private Set<String> competitors; //List of my competitors
	
//	private LocalDate startDate = LocalDate.now(); 
	
//	private LocalDate endDate = LocalDate.now(); 

	private LocalDate lastUpdate = LocalDate.now().minus(Period.ofDays(1));  //Between today and yesterday
	
	private String typeOfReport="Matriz de Precios con Indicadores";
	
	public ReportSettings() {
		
	}
	
    public ReportSettings(Integer days) {
    	this.lastUpdate = LocalDate.now().minus(Period.ofDays(days));
	}
	
	public ReportSettings(Set<String> competitors, String typeOfReport, LocalDate lastUpdate) {
		this.competitors = competitors;
		this.typeOfReport = typeOfReport;
		this.lastUpdate = lastUpdate;
	}

	
	public String getTypeOfReport() {
		return typeOfReport;
	}


	public void setTypeOfReport(String typeOfReport) {
		this.typeOfReport = typeOfReport;
	}


	public Set<String> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(Set<String> competitors) {
		this.competitors = competitors;
	}

	public LocalDate getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	
	
}
