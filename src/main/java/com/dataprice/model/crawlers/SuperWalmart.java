package com.dataprice.model.crawlers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
public class SuperWalmart extends AbstractCrawler{

	@Override
	public List<CrawlInfo> getUrlsFromTask(Task taskDAO) {
		WebDriver driver = null;
		try {
			//Initialization Phase
			driver = PhantomFactory.getInstance().getDriver();
			driver.get(taskDAO.getSeed());
			System.out.println("Inicializando Phantom");
			LinkedList<CrawlInfo> linksList = new LinkedList<CrawlInfo>();
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
					linksList.add(new CrawlInfo(we.getAttribute("href")));
		        }
			}
			
			for (String taskLink : linksSet) { //In case we have pagination.
				driver.get(taskLink);
				
				Thread.sleep(Configuration.DRIVERDELAY);
				
				for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'container-listing_')]/div[1]/div[2]/div[1]/a"))) {	   
					linksList.add(new CrawlInfo(we.getAttribute("href")));
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
			
			String id = ContentParser.parseContent(urlContent, Regex.SUPERWALMART_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.SUPERWALMART_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			
	 		 
			String price = ContentParser.parseContent(urlContent, Regex.SUPERWALMART_PRICE); 
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.SUPERWALMART_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
			imageUrl = imageUrl.trim();
			imageUrl = "https://super.walmart.com.mx" + imageUrl;
				
			String sku = "";
			String upc = "";
			//Consider other cases!!
			if (id.length() == 14) {
				upc = id.substring(2);
			}
			String brand = "";
			
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,"",Double.valueOf(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "SuperWalmart";
	}

}
