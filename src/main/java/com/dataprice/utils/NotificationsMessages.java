package com.dataprice.utils;

public enum NotificationsMessages {

	STUDENT_SAVE_VALIDATION_ERROR_DESCRIPTION("Person could not be saved, please check error messages for each field."),
	STUDENT_SAVE_VALIDATION_ERROR_TITLE("ERROR"),
	STUDENT_SAVE_SUCCESS_TITLE("SAVE"),
	STUDENT_SAVE_SUCCESS_DESCRIPTION("Students has been saved!"),
	STUDENT_REMOVE_SUCCESS_TITLE("REMOVE"),
	STUDENT_REMOVE_SUCCESS_DESCRIPTION("Students has been removed!");
	
	private final String string;

	private NotificationsMessages(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
