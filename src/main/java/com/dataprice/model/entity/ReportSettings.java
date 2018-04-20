package com.dataprice.model.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReportSettings {

	private Set<String> categories;
	
	private Set<String> competitors; //List of my competitors
	
	private Set<String> fields; //List of extra fields to add to reports, such as brand, url, imageLink, etc.

	private LocalDate startDate; 
	
	private LocalDate endDate; 

	public ReportSettings(Set<String> categories, Set<String> competitors, Set<String> fields, LocalDate startDate, LocalDate endDate) {
		this.categories = categories;
		this.competitors = competitors;
		this.fields = fields;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public Set<String> getFields() {
		return fields;
	}

	public void setFields(Set<String> fields) {
		this.fields = fields;
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
