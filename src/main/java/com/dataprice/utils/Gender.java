package com.dataprice.utils;

public enum Gender {

	MALE("male"), FEMALE("female");

	private final String string;

	private Gender(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}