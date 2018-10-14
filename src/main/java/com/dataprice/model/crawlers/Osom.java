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
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

/**
 * Not finished
 * @author rene
 *
 */

@Component
public class Osom extends AbstractCrawler{

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
			
			for (WebElement we : driver.findElements(By.cssSelector("li.ui-listItem a"))) {	   
			    if (we.getAttribute("href")!=null){
			    	linksSet.add(we.getAttribute("href"));
				}
	        }
			
			//Not needed because the pagination already has the first page!!
			/**
			 for (WebElement we : driver.findElements(By.cssSelector("a.itm-link"))) {	
				System.out.println(we.getAttribute("href"));
				linksList.add(new CrawlInfo(we.getAttribute("href")));
		        }
			*/
		
			int con = 0;
			for (String taskLink : linksSet) { //In case we have pagination.
				    driver.get(taskLink);
					con++;
				//	System.out.println("pagina: " + con);
				    Thread.sleep(Configuration.DRIVERDELAY);
					
				    for (WebElement we : driver.findElements(By.cssSelector("a.itm-link"))) {	
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
			   
		 //   System.out.println("url: " + crawlInfo.getUrl());
		    PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 

			String id = ContentParser.parseContent(crawlInfo.getUrl(), Regex.OSOM_ID);
		
			if (id==null)
				return new Product();
			
			id = id.replace(".", "");
		//	System.out.println("El id es:" + id);
			String name = ContentParser.parseContent(urlContent, Regex.OSOM_NAME);
		//	System.out.println("El nombre es: " + name);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();					
			
			String description = "";
			
			
			String oldPrice = ContentParser.parseContent(urlContent, Regex.OSOM_OLDPRICE); 	
		//	System.out.println("oldprice: " + oldPrice);
			if (oldPrice == null) {  
				return new Product();
			}
		
			oldPrice = oldPrice.replace(",", "");
			oldPrice = oldPrice.replace("$", "");
			oldPrice = oldPrice.replace("MXN", "");
			oldPrice = oldPrice.trim();
			
			
			String price = ContentParser.parseContent(urlContent, Regex.OSOM_PRICE); 
		//	System.out.println("Precio especial" + price);
			if (price == null) {  
				price = "";
			}
		
			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replace("MXN", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.OSOM_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
		
						
			String sku = "";
			
			String brand = ContentParser.parseContent(urlContent, Regex.OSOM_BRAND);	
			if (brand == null) {  
				brand = ""; //Unlike name, sometimes we don't have a brand.
			}else	{ ///We add brand in the name variable!! for better search results 
				brand.trim();
				name = name + " " + brand;
				name = name.trim().replaceAll(" +", " ");
				
			}
		//	 System.out.println("La marca es: " + brand);
		//	 System.out.println("Con marca..... " + name);		
			
			String upc = "";			

			if (price.equals("")) {
				
			    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(oldPrice),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			}else {
				
			    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			}
		
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Osom";
	}

}
