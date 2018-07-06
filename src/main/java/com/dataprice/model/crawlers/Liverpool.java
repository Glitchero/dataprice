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
public class Liverpool extends AbstractCrawler{

	
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

			for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'productName')]"))) {
				linksList.add(new CrawlInfo(we.getAttribute("href")));
		        }
			
			boolean isFirstIteration = true;
			
			//There is always at least one of this elements, but dosent belong to the pagination, really weird!!
			while (driver.findElements(By.cssSelector("i.iconmoon.gid-icon2-liv-right-06")).size()>1){
				   if (driver.findElements(By.cssSelector("i.iconmoon.gid-icon2-liv-right-06")).size()==3) {
					   if (isFirstIteration) {
						   isFirstIteration = false;
						   driver.findElements(By.cssSelector("i.iconmoon.gid-icon2-liv-right-06")).get(0).click();
					   }else {
						   break; 
					   }
				   }else {
					   driver.findElements(By.cssSelector("i.iconmoon.gid-icon2-liv-right-06")).get(1).click();
				   }
				  
				 //  System.out.println("tamaño: " + driver.findElements(By.cssSelector("i.iconmoon.gid-icon2-liv-right-06")).size());
				   Thread.sleep(Configuration.DRIVERDELAY);
			      
				   for (WebElement we : driver.findElements(By.xpath("//*[contains(@id, 'productName')]"))) {	   
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
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl());
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			/**
			String description = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_DESCRIPTION);
			if (description==null)
				description = "No disponible";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			*/
			String description = "";
			
			String price = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_PRICE); 			
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String oldPrice = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_OLDPRICE); 			
			if (oldPrice == null) {  
				return new Product();
			}

			oldPrice = oldPrice.replace(",", "");
			oldPrice = oldPrice.replace("$", "");
			oldPrice = oldPrice.trim();		
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_IMAGEURL);		
			if (imageUrl == null) {  
				return new Product();
			}
			
			String sku = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_SKU);		
			if (sku == null) {  
				sku = ""; //Unlike name, sometimes we don't have a sku.
			}
			
			String brand = ContentParser.parseContent(urlContent, Regex.LIVERPOOL_BRAND);		
			if (brand == null) {  
				brand = ""; //Unlike name, sometimes we don't have a brand.
			}
			
			String upc = "";
			
			Locale currentLocale = Locale.getDefault();
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
			otherSymbols.setDecimalSeparator('.'); 
			NumberFormat df = new DecimalFormat("#.##",otherSymbols);
					
			if (price.equals("0.0")) {
				return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.valueOf(df.format(Double.parseDouble(oldPrice))),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			}else {
				return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.valueOf(df.format(Double.parseDouble(price))),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			}
			
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public String getCrawlingStrategy() {
		return "Liverpool";
	}

	

}
