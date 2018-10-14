package com.dataprice.model.crawlers;

import java.util.HashSet;
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
public class Sanborns extends AbstractCrawler{

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

			//	System.out.println("La página actual es: " + driver.getCurrentUrl());
				for (WebElement we : driver.findElements(By.xpath("/html/body/main/section[2]/div/div/div/div[2]/div[3]/ul/li/a"))) {	   
				    if (we.getAttribute("href")!=null){
				    	linksSet.add(we.getAttribute("href"));
					}
		        }
				
				
				 for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'promocion')]/div[2]/a"))) {	
				//	System.out.println(we.getAttribute("href"));
					 linksList.add(new CrawlInfo(we.getAttribute("href"),"","",0.0,"","",""));
			        }
             
				
				 for (String taskLink : linksSet) { //In case we have pagination.
						driver.get(taskLink);
						
						Thread.sleep(Configuration.DRIVERDELAY);
						
						 for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'promocion')]/div[2]/a"))) {	
							//	System.out.println(we.getAttribute("href"));
							 linksList.add(new CrawlInfo(we.getAttribute("href"),"","",0.0,"","",""));
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
		  //  System.out.println("entro a escrapear");
		  //  System.out.println("url: " + crawlInfo.getUrl());
		    PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			
			if (urlResponse == null){  //Task fatal error.		
				System.out.println("si falla");
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 
		//	System.out.println("Server code: " + urlResponse.getResponse());
		//	System.out.println("Tamaño de regresado: " + urlContent.length());
			String id = ContentParser.parseContent(urlContent, Regex.SANBORNS_ID);
		//	System.out.println(id);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.SANBORNS_NAME);
		//	System.out.println(name);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			String presentation = ContentParser.parseContent(urlContent, Regex.SANBORNS_PRESENTATION);	
			if (presentation != null) {  
				presentation.trim();
				name = name + " " + presentation;
			}
			
			
			String description = "";
			/**
			String description = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_DESCRIPTION);
			if (description==null)
				description = "No disponible";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			*/
			
			String price = ContentParser.parseContent(urlContent, Regex.SANBORNS_PRICE); 	
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.SANBORNS_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
			
						
			String sku = "";
			
			String brand = "";			
			
		//	String upc = id;			
			String upc = "";
		    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		//	return new Product(id+"Catalogue",id,"Catalogue",null,name,description,0.00,imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Sanborns";
	}

}
