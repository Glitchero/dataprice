package com.dataprice.utils;

public enum StringUtils {

	//MENU
	MENU_REPORTS("Reportes"),
	MENU_BOTS("Bots"),
	MENU_EXPORT_IMPORT("Exportar/Importar"),
	MENU_PRODUCT_MATCHER("Emparejador de Productos"),
	MENU_FEEDS("Feeds"),
	MENU_SETTINGS("Ajustes"),
	MENU_USER("Contraseña"),
	
	//Tasks or Bots
	
	
	//Reports
	
	
	//Export/Import
	
	
	//Product Matcher
	
	
	//Feeds
	
	
	//Settings
	
	
	//User
	;
	
	private final String string;
	
	private StringUtils(String string) {
		this.string = string;
	}
	
	public String getString() {
		return this.string;
	}
	
}
