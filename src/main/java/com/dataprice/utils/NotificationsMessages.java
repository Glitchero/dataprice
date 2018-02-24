package com.dataprice.utils;

public enum NotificationsMessages {

	STUDENT_SAVE_VALIDATION_ERROR("Person could not be saved, please check error messages for each field.");
	
	private final String string;

	private NotificationsMessages(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
