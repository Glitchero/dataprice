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
public class Linio extends AbstractCrawler{

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
		
			String seller = driver.findElement(By.cssSelector("h2.section-title")).getText();
			
		
			 for (WebElement we : driver.findElements(By.xpath("//*[@id=\"catalogue-product-container\"]/div/a"))) {	
				//System.out.println(we.getAttribute("href"));
				linksList.add(new CrawlInfo(we.getAttribute("href"),"",0.00,seller));
		     }

			 driver.findElement(By.cssSelector("a.page-link.page-link-icon")).click();	
			 Thread.sleep(Configuration.DRIVERDELAY);
			 
			 
			 for (WebElement we : driver.findElements(By.xpath("//*[@id=\"catalogue-product-container\"]/div/a"))) {	
				 linksList.add(new CrawlInfo(we.getAttribute("href"),"",0.00,seller));
			  }
			 
			 
			 while (driver.findElements(By.cssSelector("a.page-link.page-link-icon")).size()>0){	
				    if (driver.findElements(By.cssSelector("a.page-link.page-link-icon")).size()>2) {
				    	driver.findElements(By.cssSelector("a.page-link.page-link-icon")).get(2).click();	
						Thread.sleep(Configuration.DRIVERDELAY);
				    }else {
				    	System.out.println("Break the loop");
				    	break;
				    }
					
				    for (WebElement we : driver.findElements(By.xpath("//*[@id=\"catalogue-product-container\"]/div/a"))) {		
				        //System.out.println(we.getAttribute("href"));
				    	linksList.add(new CrawlInfo(we.getAttribute("href"),"",0.00,seller));
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
			System.out.println("-------------------------------------------------------"); 
		    System.out.println("url: " + crawlInfo.getUrl());
		    PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl());
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 

			String id = ContentParser.parseContent(urlContent, Regex.LINIO_ID);
			System.out.println(id);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.LINIO_NAME);
			System.out.println(name);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			
			String description = "";
			
			String price = ContentParser.parseContent(urlContent, Regex.LINIO_PRICE); 	
			System.out.println(price);
			if (price == null) {  
				return new Product();
			}
		
			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.LINIO_IMAGEURL);
			 
			if (imageUrl == null) {  
				return new Product();
			}
						
			imageUrl = "https:" + imageUrl;
			System.out.println(imageUrl);
			
	//		String seller = ContentParser.parseContent(urlContent, Regex.LINIO_SELLER);	
	//		if (seller == null) {  
	//			return new Product();
	//		}
			System.out.println(crawlInfo.getSeller());
			
			String sku = "";
			
			String brand = "";
		
			String upc = "";			

		    return new Product(id+getCrawlingStrategy(),id,crawlInfo.getSeller(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Linio";
	}

}
