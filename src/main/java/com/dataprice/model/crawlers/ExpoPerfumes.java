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

@Component
public class ExpoPerfumes extends AbstractCrawler{

	/**
	 * **IMPORTANT**, THE PAGE MUST BE LOADED WTH THE MAXIMUM NUMBER OF PRODUCTS!!.
	 */
	
	
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
				
				for (WebElement we : driver.findElements(By.cssSelector("a.pagenav"))) {	   
				    if (we.getAttribute("href")!=null){
				    	linksSet.add(we.getAttribute("href"));
					}
		        }
				
				 for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'row_')]/table/tbody/tr[1]/td/a"))) {	
					System.out.println(we.getAttribute("href"));
					linksList.add(new CrawlInfo(we.getAttribute("href")));
			        }
				 
				int con = 1;
				for (String taskLink : linksSet) { //In case we have pagination.
					    driver.get(taskLink);
						con++;
						System.out.println("pagina: " + con);
					    Thread.sleep(Configuration.DRIVERDELAY);
						
					    for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'row_')]/table/tbody/tr[1]/td/a"))) {	
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

			String id = ContentParser.parseContent(crawlInfo.getUrl(), Regex.EXPOPERFUMES_ID);
			 System.out.println(id);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.EXPOPERFUMES_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			
			String description = "";
			
			String price = ContentParser.parseContent(urlContent, Regex.EXPOPERFUMES_PRICE); 	
			 
			if (price == null) {  
				return new Product();
			}
		
			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replace(" ", "");
			price = price.trim();
			System.out.println(price);
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.EXPOPERFUMES_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
			imageUrl = "http://expoperfumes.com.mx" + imageUrl;
			 System.out.println(imageUrl);

			String sku = "";
			
			String brand = ContentParser.parseContent(urlContent, Regex.EXPOPERFUMES_BRAND);	
			if (brand == null) {  
				brand = ""; //Unlike name, sometimes we don't have a brand.
			}else	{ ///We add brand in the name variable!! for better search results 
				brand.trim();
				name = name + " " + brand;
			}
			 System.out.println(brand);
			 System.out.println("Con marca..... " + name);
			String upc = "";			

		    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "ExpoPerfumes";
	}

}
