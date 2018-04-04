package com.dataprice.model.crawlers.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.LinkedList;

import com.dataprice.model.entity.Product;




/**
 * This class holds all transactions to the MySQL database, including the
 * connection itself. It contains the minimum set of operations to add and
 * select all products from database. For more complex systems, other ORMs and
 * persistence solutions could be explored.
 * 
 * This DAO assumes that the proper permissions were already given to the
 * specified user for the given database.
 * 
 * @author Rafael Rezende
 * 
 */
public class ProductDAO {

	/** MySQL driver */
//	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
	/** MySQL URL */
	//private final static String URL = "jdbc:mysql://localhost/Precios";
	private final static String URL = "jdbc:mysql://localhost:3306/univers?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	/** MySQL username */
	private final static String USERNAME = "root";
	/** MySQL password */
	private final static String PASSWORD = "Harbinger1945";
	/** Unique connection to the MySQL database */
	private static Connection connection = null;

	//Hacer lista de retailers y escribir campos!!
	//Debo tener proceso de obtener equivalencias nuevas por clave/sku (solo SKU!!!). En algunos casos debemos de limpiar el sku quitando o poniendo guión por ejemplo.
	//También el proceso de equivalenicas debe tener memoria, por eso ocuapamos la tabla de paso "Equivalencias".
	
	
	/**
	 * Sería deseable tener la siguiente estructura de datos:
	 * TABLA Originales.- Contiene la información (raw) de los retailers. Tiene clave compuesta única (cadena,sku,name,color), la talla no afecta el precio por eso no se considera!!!
	 *                    En caso de que no tengamos algún valor en la clave podemos poner un valor como: no_disponible
	 *                    Mínimo requerimos tres campos para identificar el producto como la cadena, name y color. O tambien cadena, sku. Depende del retailer!!!
	 *                    Ejemplo si el retail no cuenta con tales campos (que formen la unicidad) entonces no agregar a la base de datos.
	 * TABLA Precios.- Contiene foreign keys a la tabla Originales, guardamos los precios históricos. Contiene primary key autoincremental.
	 * TABLA Equivalencias.- Contiene foreign keys a la tabla Originales y contiene foreign keys a la tabla Precios. Campos dado de alta y mejor descripción deben de incluirse. 
	 * TABLA Productos.- Contiene primary key auto incremento. 
	/**
	 * Serialize object content into DB. Insert operations for an existing key
	 * will update product name and price (ON DUPLICATE KEY)
	 */
	//private static final String PRECIOS_INSERT_ORIGINALES = "INSERT INTO Precios.Originales(cadena, sku, nombre,color,marca,precio_actual,url,imgpath) VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE precio_actual=VALUES(precio_actual),actualizado = CURRENT_TIMESTAMP";
	private static final String PRECIOS_INSERT_ORIGINALES = "INSERT INTO univers.product(product_id,retail,name,precio,image_url,product_url) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE precio=VALUES(precio)";

     /**
	 * Deserialize object from DB. This query selects all items from the product
	 * table. New or a more general queries should be created for a dynamic
	 * request.
	 */
	//private static final String PRECIOS_SELECT = "SELECT sku, nombre, precio_actual FROM Precios.Originales";
	/**
	 * Create table if it does not exist. Warning: User permission required in
	 * the current database!
	 */
	//private static final String PRECIOS_CREATE_ORIGINALES = "CREATE TABLE IF NOT EXISTS Precios.Originales(cadena VARCHAR(200) NOT NULL,sku VARCHAR(200),nombre VARCHAR(200),color VARCHAR(200),marca VARCHAR(200),precio_actual VARCHAR(200) NOT NULL,url VARCHAR(200),imgpath VARCHAR(200),actualizado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,CONSTRAINT pk_OriginalID PRIMARY KEY (cadena,sku,nombre,color))";
	 private static final String PRECIOS_CREATE_ORIGINALES = "CREATE TABLE IF NOT EXISTS univers.product(product_id VARCHAR(200) NOT NULL,retail VARCHAR(200) NOT NULL,name VARCHAR(200),precio DOUBLE,image_url VARCHAR(300),product_url VARCHAR(300), PRIMARY KEY (product_id,retail))";


	/**
	 * This function attempts to connect to the database if there is no existing
	 * connection already open.
	 * 
	 * @return Successful connection to database.
	 */
	public static boolean connect() {
		// if there is no existing connection, create a new one
		if (connection == null) {
			try {
				// load the MySQL driver
				Class.forName(DRIVER);
				// create connection
				connection = DriverManager.getConnection(URL, USERNAME,PASSWORD);
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				System.err.println("Connection failed");
				return false;
			}
			
		}

		// attempt to create table when connection is created
		//createOriginalTable();

		return true;
	}

	/**
	 * Create table "originales" if it does not exist.
	 */
	public static void createOriginalTable() {

		try {
			PreparedStatement statement = connection.prepareStatement(
					PRECIOS_CREATE_ORIGINALES, Statement.RETURN_GENERATED_KEYS);
			// execute query
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Create table failed");
		}
	}

	
	
	
	/**
	 * Disconnect function is needed.
	 */
	public static boolean disconnect() {
		 //If the connection is active close it.	
			if (connection != null) {
			   try {
			    	connection.close();
			   } catch (SQLException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
				   System.err.println("Database disconnection failed");
				   return false;
			   }
		   }
		   return true;	
	}
	
	

	
	public static void batchInsert(LinkedList<Product> prodList) {
		
		// check if there is an existing connection
		if (connection == null) {
			System.err.println("Connection failed");
			return;
		}

		try {
			System.out.println("entre aqui");
			PreparedStatement statement = connection.prepareStatement(PRECIOS_INSERT_ORIGINALES);

			final int batchSize = 10;
			int count = 0;
			
			for (Product prod: prodList) {

		  	  // complete statement with Product fields
			  statement.setString(1,prod.getProductId());
			  statement.setString(2,prod.getRetail());
			  statement.setString(3,prod.getName());
			  statement.setDouble(4, prod.getPrice());
			  statement.setString(5,prod.getImageUrl());
			  statement.setString(6,prod.getProductUrl());
			  statement.addBatch();

		      if(++count % batchSize == 0) {
		    	  System.out.println("Batch inserting"); 
		    	  statement.executeBatch();
			  }
		    
			}
	    	
			statement.executeBatch(); // insert remaining records
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Add product failed");
		}
	}
}
