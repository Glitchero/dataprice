package com.dataprice.model.crawlers;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class SuplementosFitness extends AbstractCrawler{
	
	

	@Override
	public String getCrawlingStrategy() {
		return "SuplementosFitness";
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

			//	for (WebElement we : driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a"))) {	   
				for (WebElement we : driver.findElements(By.xpath("//*[@id=\"main\"]/div/div[1]/div/div[1]/div/div/div[2]"))) {	   
					String prices = we.findElement(By.cssSelector("span.price")).getText(); 
					//Remove spaces in start and end (not in the middle!)
					prices = prices.replaceAll("^\\s+", "");
					prices = prices.replaceAll("\\s+$", "");
		  			String[] pricesArray = prices.split("\\s+");		  			
		  			String price = "";
		  			System.out.println("lengh: " + pricesArray.length);
		  			if (pricesArray.length>2) {
		  				price = pricesArray[2]; //Third position ddd MXN ddd MXN
		  			}else {
		  				price = pricesArray[0];
		  			}
		  			//Clean the price
					price = price.replace(",", "");
		  			price = price.replace("$", "");
		  		//	price = price.replaceAll("[^\\d.]", "");
		  			price = price.trim(); 
		  			System.out.println("precio final: " + price);
		  		
		  			String url = we.findElement(By.tagName("a")).getAttribute("href");	
		  			System.out.println("url: " + url);
		  			String id = we.findElement(By.cssSelector("div.yith-wcwl-add-button.show")).findElement(By.tagName("a")).getAttribute("data-product-id");	
		  			System.out.println("id: " + id);
					linksList.add(new CrawlInfo(id, Double.parseDouble(price), url));
			        }
				
		
				while (driver.findElements(By.cssSelector("a.next.page-number")).size()>0){
					  
					   driver.findElement(By.cssSelector("a.next.page-number")).click();
					
					   Thread.sleep(Configuration.DRIVERDELAY);
				      
					   for (WebElement we : driver.findElements(By.xpath("//*[@id=\"main\"]/div/div[1]/div/div[1]/div/div/div[2]"))) {	   
							String prices = we.findElement(By.cssSelector("span.price")).getText(); 
							//Remove spaces in start and end (not in the middle!)
							prices = prices.replaceAll("^\\s+", "");
							prices = prices.replaceAll("\\s+$", "");
				  			String[] pricesArray = prices.split("\\s+");		  			
				  			String price = "";
				  			System.out.println("lengh: " + pricesArray.length);
				  			if (pricesArray.length>2) {
				  				price = pricesArray[2]; //Third position ddd MXN ddd MXN
				  			}else {
				  				price = pricesArray[0];
				  			}
				  			//Clean the price
							price = price.replace(",", "");
				  			price = price.replace("$", "");
				  		//	price = price.replaceAll("[^\\d.]", "");
				  			price = price.trim(); 
				  			System.out.println("precio final: " + price);
				  		
				  			String url = we.findElement(By.tagName("a")).getAttribute("href");	
				  			System.out.println("url: " + url);
				  			String id = we.findElement(By.cssSelector("div.yith-wcwl-add-button.show")).findElement(By.tagName("a")).getAttribute("data-product-id");	
				  			System.out.println("id: " + id);
							linksList.add(new CrawlInfo(id, Double.parseDouble(price), url));
					     }
				}
			
				//Destroy
				PhantomFactory.getInstance().removeDriver();		
				Thread.sleep(1000);
				return linksList;
			}  catch (Exception e) {
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
	public Product parseProductFromURL(CrawlInfo crawlInfo, Task taskDAO) {
         try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 
			
			//Id obtained from Product URL.
		//	String id = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_ID);
		//	if (id==null)
		//		return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
		
			String description = "";
			/**
			String description = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_DESCRIPTION);
			if (description==null)
				description = "No disponible";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			*/
			
	
		//	String price = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_PRICE); 		
		//	if (price == null) {  
		//		return new Product();
		//	}

		//	price = price.replace(",", "");
		//	price = price.replace("$", "");
		//	price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_IMAGEURL);	
			if (imageUrl == null) {  
				return new Product();
			}
			
			String sku = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_SKU);		
			if (sku == null) {  
				sku = ""; //Unlike name, sometimes we don't have a sku.
			}
			
			if (sku.length()>2)  //Two last characters is the flavor! XD.
				sku = sku.substring(0, sku.length()-2);
			
			
			String brand = "";

			
			String upc = "";
			

		    return new Product(crawlInfo.getId()+getCrawlingStrategy(),crawlInfo.getId(),getCrawlingStrategy(),taskDAO,name,description,crawlInfo.getPrice(),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		
			
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
			for (WebElement we : driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a"))) {	   
				linksList.add(we.getAttribute("href"));
		        }
			
			while (driver.findElements(By.cssSelector("a.next.page-number")).size()>0){
				  
				   driver.findElement(By.cssSelector("a.next.page-number")).click();
				
				   Thread.sleep(Configuration.DRIVERDELAY);
			      
				   for (WebElement we : driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a"))) {	   
					   linksList.add(we.getAttribute("href"));
			        }
			}
			
			//Destroy
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
		}  catch (Exception e) {
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
		    
			if (Thread.currentThread().isInterrupted()) {
                System.out.println("....run()::Extraction::Scraping::isInterrupted");
                Thread.sleep(1000);
            } 
			
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(urlStr);
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_NAME);
			if (name==null)
				return new Product();
			
			name = name.trim();
			
			
			String price = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_PRICE); 
			if (price == null) {  
				return new Product();
			}

			String imageUrl = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}

			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),task,name,"",Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {			
			return null;
		}
	}

	*/


}