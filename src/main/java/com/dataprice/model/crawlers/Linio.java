package com.dataprice.model.crawlers;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.crawlers.utils.ContentParser;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.crawlers.utils.FetchResults;
import com.dataprice.model.crawlers.utils.PageFetcher;
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;


/**
 * Esta Inconcluso, falta la parte de la paginación!!!. Además, linio no precisa.
 * @author rene
 *
 */

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
			
			 for (WebElement we : driver.findElements(By.cssSelector("div.catalogue-product.row a"))) {	
				System.out.println(we.getAttribute("href"));
				linksList.add(new CrawlInfo(we.getAttribute("href")));
		        }
		//	 a.page-link.page-link-icon  One way to solve the proble could be finishing whene there are only two.
			
			 while (!(driver.findElements(By.cssSelector("li#pagination_next_bottom.disabled.pagination_next")).size()>0)){		
					driver.findElement(By.cssSelector("li#pagination_next_bottom.pagination_next a")).click();	
					Thread.sleep(Configuration.DRIVERDELAY);
					for (WebElement we : driver.findElements(By.cssSelector("div.catalogue-product.row a"))) {	
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
			price = price.trim();
			
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.LINIO_IMAGEURL);
			 System.out.println(imageUrl);
			if (imageUrl == null) {  
				return new Product();
			}
			imageUrl = "http://expoperfumes.com.mx" + imageUrl;
			

			String sku = "";
			
			String brand = "";
		
			String upc = "";			

		    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Linio";
	}

}