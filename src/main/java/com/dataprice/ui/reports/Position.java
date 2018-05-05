package com.dataprice.ui.reports;

import java.util.LinkedList;
import java.util.List;

public class Position {

	private final String cheaperPosition = "Soy más barato";
	private final String equalPosition = "Mismo precio";
	private final String expensivePosition = "Soy más caro";
	
	private String position = "";	
	private List<String> positionsList = null;
	
	
	public Position() {
		this.positionsList = new LinkedList<String>();
		this.positionsList.add(cheaperPosition);
		this.positionsList.add(equalPosition);
		this.positionsList.add(expensivePosition);
	}
	
		
	public String getCheaperPosition() {
		return cheaperPosition;
	}
	
	
	public String getEqualPosition() {
		return equalPosition;
	}
	
	
	public String getExpensivePosition() {
		return expensivePosition;
	}
	
	
	public List<String> getPositionsList() {
		return positionsList;
	}
	
	public void setCurrentPositionToCheaper() {
		this.position = cheaperPosition;
	}
	
	public void setCurrentPositionToEqual() {
		this.position = equalPosition;
	}
	
	public void setCurrentPositionToExpensive() {
		this.position = expensivePosition;
	}
	
	public String getCurrentPosition() {
		return position;
	}
	
	
} 
