package com.dataprice.model.crawlers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Component
public class Walmart extends AbstractCrawler{
	

	@Override
	public String getCrawlingStrategy() {
		return "Walmart";
	}

	@Override
	public List<CrawlInfo> getUrlsFromTask(Task taskDAO) {
		
		WebDriver driver = null;
		try {
			//Initialization Phase
			driver = PhantomFactory.getInstance().getDriver();
			driver.get(taskDAO.getSeed());
			System.out.println("Inicializando Phantom");
			LinkedList<CrawlInfo> linksList = new LinkedList<CrawlInfo>();
			Thread.sleep(1000);
			
			//Navigation
			Thread.sleep(Configuration.DRIVERDELAY); 
			int con = 0;
	        while (driver.findElements(By.xpath("//*[contains(text(), 'Ver más artículos')]")).get(0).isDisplayed()){
	        	
	        	WebElement el = driver.findElements(By.xpath("//*[contains(text(), 'Ver más artículos')]")).get(0);
				   Actions builder = new Actions(driver); 
				   builder.moveToElement(el, el.getLocation().x, el.getLocation().y);
				   Locatable hoverItem = (Locatable) el;
				   Mouse mouse = ((HasInputDevices) driver).getMouse();
				   mouse.mouseMove(hoverItem.getCoordinates());
				   el.click();	
				 //  System.out.println("Doy click: " + con); 
				   con++;
				   Thread.sleep(Configuration.DRIVERDELAY); 
				   if (con==35) { //Before it was 20
					   break;
				   }
			}

	        for (WebElement we : driver.findElements(By.xpath("//*[@id=\"productsContainer\"]/div"))) {	
	      
	        	   if (we.getLocation().getX()!=0) {

				 //  System.out.println("Direeccion : " + we.findElement(By.cssSelector("a.prvntClck")).getAttribute("href"));
				 //  System.out.println("Precio: " + we.findElement(By.cssSelector("div.price")).getText());
				 //  System.out.println("titulo: " + we.findElement(By.cssSelector("span.test")).getText());
	        	  String url = we.findElement(By.cssSelector("a.prvntClck")).getAttribute("href");
	        	  
	        	  String name = we.findElement(By.cssSelector("span.test")).getText();
	        	  name = name.trim(); 
	        	  
	        	  String price = we.findElement(By.cssSelector("div.price")).getText(); 
	        	  price = price.replace(",", "");
	  			  price = price.replace("$", "");
	  			  price = price.replaceAll("[^\\d.]", "");
	  			  price = price.trim();
	  			  price = price.substring(0, price.length()-2);
	        	  
	  			String seller = we.findElement(By.cssSelector("div.productList-sellerName")).getText();
	  			seller = seller.replace("Vendido por:", "");
	  			seller = seller.trim();
				linksList.add(new CrawlInfo(url,name,Double.valueOf(price),seller));
	        		   
	        	   }
	        }

	      //Destroy
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
			
		} catch (Exception e) {
			System.out.println("Error en phantom" + e);
			
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
	public Product parseProductFromURL(CrawlInfo crawlInfo, Task taskDAO) {
         try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl());
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.WALMART_ID);
			if (id==null)
				return new Product();
		
			
			String description = ContentParser.parseContent(urlContent, Regex.WALMART_DESCRIPTION);
			if (description==null)
				description = "";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.WALMART_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
			imageUrl = imageUrl.trim();
				
			String sku = "";
			String upc = "";
			//Consider other cases!!
		//	if (id.length() == 14) {
		//		upc = id.substring(2);
		//	}
			
			String brand = "";
			
			//return new Product(id+getCrawlingStrategy(),id,crawlInfo.getSeller(),taskDAO,crawlInfo.getProductName(),description,crawlInfo.getPrice(),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,crawlInfo.getProductName(),description,crawlInfo.getPrice(),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());		
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
			HashSet<String> linksSet = new HashSet<String>();
			Thread.sleep(1000);
			
			//Navigation
			for (WebElement we : driver.findElements(By.xpath("//*[@id='atg_store_pagination']/li/a"))) {	   
			    if (we.getAttribute("href")!=null){
			    	linksSet.add(we.getAttribute("href"));
				}
	        }
			
			if (linksSet.size() == 0){   //In case we don't have pagination.
				for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'container-listing_')]/div[1]/div[2]/div[1]/a"))) {	   
					linksList.add(we.getAttribute("href"));
		        }
			}
			
			for (String taskLink : linksSet) { //In case we have pagination.
				driver.get(taskLink);
				
				Thread.sleep(Configuration.DRIVERDELAY);
				
				for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'container-listing_')]/div[1]/div[2]/div[1]/a"))) {	   
					linksList.add(we.getAttribute("href"));
		        }
			}
			
			//Destroy
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
			
		} catch (Exception e) {
			System.out.println("Error en phantom" + e);
			
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
			
			String id = ContentParser.parseContent(urlContent, Regex.WALMART_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.WALMART_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			
	 		 
			String price = ContentParser.parseContent(urlContent, Regex.WALMART_PRICE); 
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.WALMART_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
			imageUrl = imageUrl.trim();
			imageUrl = "https://super.walmart.com.mx" + imageUrl;
				
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),task,name,"",Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {
			return null;
		}
	}

*/


}