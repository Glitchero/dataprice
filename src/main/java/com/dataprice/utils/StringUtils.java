package com.dataprice.utils;

public enum StringUtils {

	MENU_STUDENT("STUDENTS"),
	MENU_UNIVERSITY("UNIVERSITY"),
	MENU_ADD_STUDENT("Add Students"),
	MENU_REMOVE_STUDENT("Remove Students"),
	MENU_OPERATIONS("Operations"),
	;
	
	private final String string;
	
	private StringUtils(String string) {
		this.string = string;
	}
	
	public String getString() {
		return this.string;
	}
	
}
