package com.dataprice.model.crawlers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
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
public class MercadoLibre extends AbstractCrawler{

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
				 //int pag = 0;
                 //int con = 0;
                 //pag++;
				 for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'ML')]/div[1]/div/div/a"))) {	
					//System.out.println(we.getAttribute("href"));
					linksList.add(new CrawlInfo(we.getAttribute("href")));
					//con++;
			        }
				 //System.out.println("Current page" + driver.getCurrentUrl());
				 //System.out.println("Pagina" + pag);
				 //System.out.println("Total de links" + con);
				 //Thread.sleep(5*1000);
				 //con = 0;
				 //pag++;
			
				 while (!(driver.findElements(By.cssSelector("li.pagination__next.pagination--disabled")).size()>0)){		
				 //	driver.findElement(By.cssSelector("//*[@id='results-section']/div[2]/ul/li[12]/a")).click();	
					driver.findElement(By.cssSelector("li.pagination__next a")).click();						
					Thread.sleep(Configuration.DRIVERDELAY);
					for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'ML')]/div[1]/div/div/a"))) {
						 linksList.add(new CrawlInfo(we.getAttribute("href")));
						// con++;
					}
					
				 //	System.out.println("Current page" + driver.getCurrentUrl());
				 //	System.out.println("Pagina" + pag);
				 //	System.out.println("Total de links" + con);
				 //	Thread.sleep(5*1000);
				 //	con = 0;
				 //	pag++;
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
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl());
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 
			
			//Id obtained from Product URL.
			String id = ContentParser.parseContent(crawlInfo.getUrl(), Regex.MERCADOLIBRE_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			//System.out.println(name);
			String description = "";
			/**
			String description = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_DESCRIPTION);
			if (description==null)
				description = "No disponible";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			*/
			
			String price = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_PRICE); 		
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_IMAGEURL);	
			if (imageUrl == null) {  
				return new Product();
			}
			
			String seller = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_SELLER);	
			if (seller == null) {  
				return new Product();
			}
			//System.out.println(seller);
			
			String sku = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_SKU);		
			if (sku == null) {  
				sku = ""; //Unlike name, sometimes we don't have a sku.
			}
			
			String brand = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_BRAND);	
			if (brand == null) {  
				brand = ""; //Unlike name, sometimes we don't have a brand.
			}
			
			String upc = "";
			

		    return new Product(id+getCrawlingStrategy(),id,seller,taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "MercadoLibre";
	}

}
