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
public class Chedraui extends AbstractCrawler{
	
	

	@Override
	public String getCrawlingStrategy() {
		return "Chedraui";
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
			
			
			for (WebElement we : driver.findElements(By.xpath("//*[@id='plp_display']/li/a"))) {	   
				linksList.add(new CrawlInfo(we.getAttribute("href")));
	        }
			
			while (driver.findElements(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).size()>0){
			    driver.findElement(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).click();
				
			    Thread.sleep(Configuration.DRIVERDELAY);
				
			    for (WebElement we : driver.findElements(By.xpath("//*[@id='plp_display']/li/a"))) {	   
			    	linksList.add(new CrawlInfo(we.getAttribute("href")));
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
	public Product parseProductFromURL(CrawlInfo crawlInfo, Task task) {
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
			
			String id = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			String description = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_DESCRIPTION);
			if (description==null)
				description = "";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			 		 
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
			
				
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),task,name,description,Double.valueOf(price),imageUrl,crawlInfo.getUrl());
		} catch (Exception e) {
			return null;
		}
	}
  
	



}