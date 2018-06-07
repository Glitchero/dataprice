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
import com.dataprice.model.crawlers.utils.PageFetcherWithProxy;
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.crawlers.utils.PhantomFactoryWithProxy;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Component
public class Amazon extends AbstractCrawler{

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
				
				 for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'result_')]/div/div/div/div[1]/div/div/a"))) {
					System.out.println(we.getAttribute("href"));
					linksList.add(new CrawlInfo(we.getAttribute("href")));
			        }
				 
	             int con = 1;
				 while (driver.findElements(By.cssSelector("span.srSprite.pagnNextArrow")).size()>0){	
					driver.findElement(By.cssSelector("span.srSprite.pagnNextArrow")).click();	
					con++;
					System.out.println("pagina: " + con);
					Thread.sleep(Configuration.DRIVERDELAY);
					for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'result_')]/div/div/div/div[1]/div/div/a"))) {	
						System.out.println(we.getAttribute("href"));
						linksList.add(new CrawlInfo(we.getAttribute("href")));
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
		    
			PageFetcherWithProxy pageFetcher = PageFetcherWithProxy.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl());
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 
		
			
			String id = ContentParser.parseContent(crawlInfo.getUrl(), Regex.AMAZON_ID);
			System.out.println(id);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.AMAZON_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			System.out.println(name);
			
			String description = "";
			/**
			String description = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_DESCRIPTION);
			if (description==null)
				description = "No disponible";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			*/
			
			String price = ContentParser.parseContent(urlContent, Regex.AMAZON_PRICE); 	
			if (price == null || price.contains("-")) {  //$941.84 - $3,437.65 Don't want this type of prices!!!.
				return new Product();
			}
			System.out.println(price);
			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.AMAZON_IMAGEURL);
			//System.out.println(imageUrl);
			if (imageUrl == null) {  
				return new Product();
			}			
						
			String sku = "";		
			String brand = "";		
			String upc = "";
			
		
		    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		
			
		} catch (Exception e) {
			//System.out.println(e);
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Amazon";
	}

}
