package com.dataprice.utils;

public enum StudentsStringUtils {
	MAIN_MENU("MAIN MENU"),
	SHOW_ALL_STUDENTS("SHOW ALL STUDENTS"), //
	FIRST_NAME("First name"), //
	LAST_NAME("Last name"),//
	AGE("Age"),//
	UNIVERSITY("University"),//
	GENDER("Gender"), //
	SAVE("Save"),//
	CANCEL("Cancel"),//
	REMOVE_STUDENT("Remove");
	
private final String string;
	
	private StudentsStringUtils(String string) {
		this.string = string;
	}
	
	public String getString() {
		return this.string;
	}
	
	
}
