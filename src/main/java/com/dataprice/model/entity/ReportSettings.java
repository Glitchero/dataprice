package com.dataprice.model.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReportSettings {

	private Set<String> categories;
	
	private Set<String> competitors; //List of my competitors
	
	private LocalDate startDate = LocalDate.now(); 
	
	private LocalDate endDate = LocalDate.now(); 

	private String typeOfReport="Matriz de Precios";
	
	public ReportSettings() {

	}
	
	public ReportSettings(Set<String> categories, Set<String> competitors, String typeOfReport, LocalDate startDate, LocalDate endDate) {
		this.categories = categories;
		this.competitors = competitors;
		this.typeOfReport = typeOfReport;
		this.startDate = startDate;
		this.endDate = endDate;
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

	
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
}
