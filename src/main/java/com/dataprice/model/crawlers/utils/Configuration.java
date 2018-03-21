package com.dataprice.model.crawlers.utils;

public class Configuration {

	public final static String LOGPATH = "";

	/** Tasks Configuration.
	 * ITERATOR_STRATEGY = Original
	 * ITERATOR_STRATEGY = Random
	 * ITERATOR_STRATEGY = ActiveHosts
	 */
	public final static String ITERATOR_STRATEGY = "Random";
	public final static int NUMBEROFCORES = 3; 
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
	public final static int DRIVERDELAY = 4000; //5 seconds , units = milliseconds
	public final static int POLITENESSDELAY = 1000; // 1 second for politeness within the same host!.
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
	
}