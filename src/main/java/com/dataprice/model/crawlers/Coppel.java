package com.dataprice.model.crawlers;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.springframework.stereotype.Component;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.crawlers.utils.ContentParser;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.crawlers.utils.FetchResults;
import com.dataprice.model.crawlers.utils.PageFetcher;
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.crawlers.utils.PhantomFactoryWithProxy;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Component
public class Coppel extends AbstractCrawler{
	
	/**
	 * The error is because there is a redirection in the phantomjs. Check for a solution!!.
	 */

	@Override
	public String getCrawlingStrategy() {
		return "Coppel";
	}

	@Override
	public List<CrawlInfo> getUrlsFromTask(Task taskDAO) {
		
       WebDriver driver = null;
		
		try {
			//Initialization Phase
			driver = PhantomFactoryWithProxy.getInstance().getDriver();
			driver.get(taskDAO.getSeed());
			System.out.println("Inicializando Phantom");
			LinkedList<CrawlInfo> linksList = new LinkedList<CrawlInfo>();
			Thread.sleep(1000);
			//Navigation
			
		    driver.get(taskDAO.getSeed()); //We need another get, because there is redirect
		   		
			System.out.println("semilla" +taskDAO.getSeed());
			System.out.println("current url" + driver.getCurrentUrl());
			
			 
			for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'dijit__WidgetBase_')]/li/div"))) {
				
				String price = we.findElement(By.cssSelector("div.pcontado")).getText(); 						
				price = price.replace(",", "");
	  			price = price.replace("$", "");
	  			price = price.replaceAll("[^\\d.]", "");
	  			price = price.trim();				
				String url = we.findElement(By.tagName("a")).getAttribute("href");	
				System.out.println("Url: " + url);
				System.out.println("Price: " + price);
				linksList.add(new CrawlInfo(url,Double.valueOf(price)));
	        }
		
			

			Thread.sleep(Configuration.DRIVERDELAY);
			/**
			while (driver.findElements(By.cssSelector("i.fa.fa-angle-right")).size()>0){	
			
			
			   WebElement el = driver.findElement(By.cssSelector("i.fa.fa-angle-right"));
			   JavascriptExecutor js = (JavascriptExecutor)driver; 
			   js.executeScript("arguments[0].click();", el);  
			  
			   Thread.sleep(Configuration.DRIVERDELAY);
			    
				if (driver.findElements(By.xpath("//*[starts-with(@id, 'dijit__WidgetBase_')]/li/div")).size()!=0) {
			   
					  for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'dijit__WidgetBase_')]/li/div"))) {				
						String price = we.findElement(By.cssSelector("div.pcontado")).getText(); 						
						price = price.replace(",", "");
			  			price = price.replace("$", "");
			  			price = price.replaceAll("[^\\d.]", "");
			  			price = price.trim();				
						String url = we.findElement(By.tagName("a")).getAttribute("href");		
					//	System.out.println("Url: " + url);
					//	System.out.println("Price: " + price);
						linksList.add(new CrawlInfo(url,Double.valueOf(price)));
			        }
				  
			   }else {
				   
				   break;  //Break the while!!
			   
			   }
				
			}
			*/
			//Destroy
			PhantomFactoryWithProxy.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
			
		} catch (Exception e) {
			System.out.println("Error en phantom" + e);
			try {
				   if (driver!=null) { //Check if driver exists, research another option for checking this.
					   PhantomFactoryWithProxy.getInstance().removeDriver();
				   }
				} catch (Exception e2) {
					return null;
				}
			return null;
		}
	}

	@Override
	public Product parseProductFromURL(CrawlInfo crawlInfo, Task taskDAO) {
        try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			
			//System.out.println(crawlInfo.getUrl());
			//System.out.println("tamaño" + urlResponse.getContent().length());
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.COPPEL_ID);
			
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.COPPEL_NAME);	
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			String description = ContentParser.parseContent(urlContent, Regex.COPPEL_DESCRIPTION);
			if (description==null)
				description = "";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text(); 		 
				
			String imageUrl = ContentParser.parseContent(urlContent, Regex.COPPEL_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
	
			imageUrl = imageUrl.trim();
				
			String sku = "";
			String upc = "";
			String brand = "";
			
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,crawlInfo.getPrice(),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		} catch (Exception e) {
			return null;
		}
	}

   /**
	@Override
	public List<String> getUrlsFromTask(Task taskDAO) {
		
		WebDriver driver = null;
		
		try {
			//Initialization Phase
			driver = PhantomFactory.getInstance().getDriver();
			driver.get(taskDAO.getSeed());
			System.out.println("Inicializando Phantom");
			LinkedList<String> linksList = new LinkedList<String>();
			Thread.sleep(1000);
			//Navigation
			
			
			for (WebElement we : driver.findElements(By.xpath("//*[@id='plp_display']/li/a"))) {	   
				linksList.add(we.getAttribute("href"));
	        }
			
			while (driver.findElements(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).size()>0){
			    driver.findElement(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).click();
				
			    Thread.sleep(Configuration.DRIVERDELAY);
				
			    for (WebElement we : driver.findElements(By.xpath("//*[@id='plp_display']/li/a"))) {	   
			    	linksList.add(we.getAttribute("href"));
		        }
			}
			
			//Destroy
			
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
			
		} catch (Exception e) {
			//System.out.println("Error en phantom" + e);
			try {
				   if (driver!=null) { //Check if driver exists, research another option for checking this.
					   PhantomFactory.getInstance().removeDriver();
				   }
				} catch (Exception e2) {
					return null;
				}
			return null;
		}
	}


	@Override
	public Product parseProductFromURL(String urlStr, Task task) {
		try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(urlStr);
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			
			String description = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_DESCRIPTION);
			if (description==null)
				description = "";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			 		 
			String price = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_PRICE); 
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
				
			imageUrl = "https://www.chedraui.com.mx" + imageUrl;
			
				
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),task,name,description,Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {
			return null;
		}
	}

	*/



}