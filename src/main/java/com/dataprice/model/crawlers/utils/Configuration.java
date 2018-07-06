package com.dataprice.model.crawlers.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Configuration {

	public final static String LOGPATH = "";

	/** Tasks Configuration.
	 * ITERATOR_STRATEGY = Original
	 * ITERATOR_STRATEGY = Random
	 * ITERATOR_STRATEGY = ActiveHosts
	 */
	public final static String ITERATOR_STRATEGY = "Random";
	public final static int NUMBEROFCORES = 2; 
	public final static int LIMIT_NUMBER_OF_TASKS = 5000;

	/** Crawler Configuration.
	 * DRIVERDELAY = In case of an error, catch the error, so the thread is cancelled and can be reloaded again later.
	 *               It would be nice to have the option for element is present, so we don't need to assign time. 
	 *               This would replace the DRIVERDELAY for a explicit delay.
	 *               Example: (https://seleniumatfingertips.wordpress.com/tag/pageloadtimeout/)
	 *               WebElement element = driver.findElement(By.linkText(“Home”));
                     WebDriverWait wait = new WebDriverWait(driver, 20);
                     wait.until(ExpectedConditions.visibilityOf(element));
	 *               
	 */
	
	public final static int MAXLOADPAGEDELAY = 30; //30 seconds (minimum), units = seconds
	public final static int DRIVERDELAY = 7000; //5 seconds , units = milliseconds
	public final static int LONGDRIVERDELAY = 20000; //20 seconds for 

	public final static int POLITENESSDELAY = 1000; // 1 second for politeness within the same host!.
	public final static int LONGPOLITENESSDELAY = 5000; // 9 second for politeness within the same host!.

	public final static int LIMIT_NUMBER_OF_URLS_PER_TASK = 5000;
	public final static boolean USER_AGENT_ROTATION = false;
	public final static String DRIVER = "PhantomJS"; //Default_driver

	public final static int MAXTIMEPERTASK = 600000; // 10 minutes per task Should be implemented. Think How??

	
	/** Database Configuration.
	 */
	public final static String MYSQL_USERNAME = "";
	public final static String MYSQL_PASSWORD = "";
	public final static String MYSQL_DATABASE = "";
	public final static String MYSQL_SERVER = "";
	public final static int BATCH_LIMIT = 100;

	
	/** Automatic mailing Configuration.
	 */
	
	public final static String GMAIL_USERNAME = "";
	public final static String GMAIL_PASSWORD = "";
	
	
	
	/** Proxy Configuration
	 */
	
	public final static String ProxyHost = "us-wa.proxymesh.com";
	public final static int ProxyPort = 31280;

//	public final static String ProxyHost = "open.proxymesh.com";
//	public final static int ProxyPort = 31280;
	public final static String ProxyUsername = "Glitchero";
	public final static String ProxyPassword = "Harbinger1989?";
	
	
	
	
	public final static String[] StopWords = {"100ML", "50ML", "200ML", "400ML", "600ML", "75ML", "115ML", "240ML", "100", "50", "200", 
			  "400", "600", "75", "115", "240", "236", "MILILITROS", "ML", "EAU", "DE", "EDT", "PARFUM", 
			  "PERFUME", "PERFUMES", "TOILETTE", "BY", "HOMBRE", "MUJER", "WOMAN", "MEN", "PARFUM","WOMEN","MAN","FOR","PARA","90ML","90"
			  ,"80ML","80","CABALLERO","DAMA","3.4OZ","OZ","125ML","125"};   
	
	public final static  Set<String> stopWordsSet = new HashSet<String>(Arrays.asList(StopWords)); 
	
}