package com.dataprice.model.crawlers.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PhantomFactory
{
   	
   private PhantomFactory()
   {
      //Constructor
   }
   
   private static PhantomFactory instance = new PhantomFactory();

   public static PhantomFactory getInstance()
   {
      return instance;
   }

   ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() // thread local driver object for webdriver
   {
      @Override
      protected WebDriver initialValue()
      {
    	/** 
    	String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
  		
  		DesiredCapabilities desiredCaps = new DesiredCapabilities();
  		desiredCaps.setJavascriptEnabled(true);
  		desiredCaps.setCapability("takesScreenshot", false);
  		desiredCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/home/rene/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
  		desiredCaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent", USER_AGENT);

  		ArrayList<String> cliArgsCap = new ArrayList();
  		cliArgsCap.add("--web-security=false");
  		cliArgsCap.add("--ssl-protocol=any");
  		cliArgsCap.add("--ignore-ssl-errors=true");
  		//cliArgsCap.add("--webdriver-loglevel=ERROR");
  		cliArgsCap.add("--webdriver-loglevel=NONE");
  		
  		desiredCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);

  		WebDriver phantomDriver = new PhantomJSDriver(desiredCaps);
  		
    	*/
    	/**  
  		WebDriver driver = null;
  		try {
			      driver = new RemoteWebDriver(
				    new URL("http://172.17.0.3:8910"),
				    DesiredCapabilities.phantomjs());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		*/
    	  
    	////////////////////////  
    	  
    	  String PROXY = "us-wa.proxymesh.com:31280";

    	  org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
    	  proxy.setHttpProxy(PROXY)
    	       .setFtpProxy(PROXY)
    	       .setSslProxy(PROXY);
    	  
    	// set some options
    	  DesiredCapabilities dc = DesiredCapabilities.phantomjs();
    	  dc.setCapability(CapabilityType.PROXY, proxy);

    	  
       WebDriver driver = null;
    		try {
  			      driver = new RemoteWebDriver(
  				    new URL("http://172.17.0.3:8910"),dc);
  		} catch (MalformedURLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    	//////////////////////////  
    	  
  	 //   phantomDriver.manage().timeouts().pageLoadTimeout(Configuration.MAXLOADPAGEDELAY, TimeUnit.SECONDS);
  		
  		return driver; 
  		
  	//	return phantomDriver; 
    	  
      }
   };

   public WebDriver getDriver() 
   {
      return driver.get();
   }

   public void removeDriver() 
   {
	 // driver.get().close();
      driver.get().quit();
      driver.remove();
   }
}
