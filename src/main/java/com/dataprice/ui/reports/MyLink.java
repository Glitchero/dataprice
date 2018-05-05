package com.dataprice.ui.reports;

import com.vaadin.ui.Link;

public class MyLink extends Link {

	 String name;
	 
	 public MyLink(String name) {
		 this.name = name;
		 
	 }
	@Override
	public String toString() {
		return name;	
	}

}
