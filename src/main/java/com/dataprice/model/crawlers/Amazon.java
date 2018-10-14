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

/**
 * Maybe the key should be:
 * 
 * SKU + Seller + getCrawlingStrategy() instead of id+getCrawlingStrategy() (only for cases where id is not unique like amazon)
 * , in amazon for sure the key should be AmazonId + Seller + getCrawlingStrategy()
 * 
 * For seller: there are only two options: getCrawlingStrategy() or crawlInfo.getSeller(). The getseller is only for stores, the other one is for categories or search.
 * @author tatua
 *
 */

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
				
		
				for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'result_')]"))) {
					String url = we.findElement(By.cssSelector("a.a-link-normal.a-text-normal")).getAttribute("href");	
				//	System.out.println(url);
					linksList.add(new CrawlInfo(url,"","",0.0,"","",""));
			    }
								
				
	             int con = 1;
				 while (driver.findElements(By.cssSelector("span.srSprite.pagnNextArrow")).size()>0){	
					driver.findElement(By.cssSelector("span.srSprite.pagnNextArrow")).click();	
					con++;
					System.out.println("pagina: " + con);
					Thread.sleep(Configuration.DRIVERDELAY);
					for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'result_')]"))) {
						String url = we.findElement(By.cssSelector("a.a-link-normal.a-text-normal")).getAttribute("href");	
						linksList.add(new CrawlInfo(url,"","",0.0,"","",""));
				    }
					if (con==10) {  //before it was 40 , later it was 44
						break;
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
        	
		    System.out.println("url: " + crawlInfo.getUrl());
		//	PageFetcherWithProxy pageFetcher = PageFetcherWithProxy.getInstance(getCrawlingStrategy());
        	PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			
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
			System.out.println(name);
			if (name==null) {
				return new Product();
			}
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			if (name.length()>254) {
				name = name.substring(0, 253);
				//return new Product();
			}
			
		   //System.out.println(name);
			
			String description = "";
			
			
			String price = ContentParser.parseContent(urlContent, Regex.AMAZON_PRICE); 	
			if (price == null || price.contains("-")) {  //$941.84 - $3,437.65 Don't want this type of prices!!!.
				return new Product();
			}
		//	System.out.println(price);
			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.AMAZON_IMAGEURL);
		//	System.out.println(imageUrl);
			if (imageUrl == null) {  
				return new Product();
			}			
			
			String sku = "";		
			String brand = ContentParser.parseContent(urlContent, Regex.AMAZON_BRAND);	
			if (brand == null) {  
				brand = "";
			}	
			brand = brand.trim();
			
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
