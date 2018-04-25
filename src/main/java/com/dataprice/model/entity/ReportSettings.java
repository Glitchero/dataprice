package com.dataprice.model.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReportSettings {

	private Set<String> categories;
	
	private Set<String> competitors; //List of my competitors
	
//	private LocalDate startDate = LocalDate.now(); 
	
//	private LocalDate endDate = LocalDate.now(); 

	private LocalDate lastUpdate = LocalDate.now(); 
	
	private String typeOfReport="Matriz de Precios en Unidades";
	
	public ReportSettings() {

	}
	
	public ReportSettings(Set<String> categories, Set<String> competitors, String typeOfReport, LocalDate lastUpdate) {
		this.categories = categories;
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


	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
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
