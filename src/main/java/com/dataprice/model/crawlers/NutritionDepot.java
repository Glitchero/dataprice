package com.dataprice.model.crawlers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.crawlers.utils.ContentParser;
import com.dataprice.model.crawlers.utils.FetchResults;
import com.dataprice.model.crawlers.utils.PageFetcher;
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

@Component
public class NutritionDepot extends AbstractCrawler{
	

	@Override
	public String getCrawlingStrategy() {
		return "NutritionDepot";
	}


	@Override
	public List<String> getUrlsFromTask(Task taskDAO) {
		WebDriver driver = null;
		try {
			//Initialization Phase
			driver = PhantomFactory.getInstance().getDriver();
			driver.get(taskDAO.getSeed());
			System.out.println("Inicializando Phantom");
			LinkedList<String> linksList = new LinkedList<String>();
			HashSet<String> linksSet = new HashSet<String>();
			Thread.sleep(1000);
			
			//Navigation
			
			for (WebElement we : driver.findElements(By.xpath("//*[@id='content']/div[5]/div[1]/a"))) {	   
			    if (we.getAttribute("href")!=null){
					linksSet.add(we.getAttribute("href"));
				}
	        }
			
			if (linksSet.size() == 0){   //In case we don't have pagination.
				for (WebElement we : driver.findElements(By.xpath("//*[@id='content']/ul/li/div/div[1]/a"))) {	   
					   // System.out.println(we.getAttribute("href"));
					      linksList.add(we.getAttribute("href"));
			        }
			}
			
			for (String taskLink : linksSet) {	
				 driver.get(taskLink);				
				 Thread.sleep(Configuration.DRIVERDELAY);
				 System.out.println("Go to WebPage");
				  
				 for (WebElement we : driver.findElements(By.xpath("//*[@id='content']/ul/li/div/div[1]/a"))) {	   
					   // System.out.println(we.getAttribute("href"));
					      linksList.add(we.getAttribute("href"));
			     }
			}

			//Destroy
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
			
		} catch (Exception e) {
			System.out.println("Error en phantom" + e);
			
			try {
			   if (driver!=null) { //Do i hace to remove the null!!
				   PhantomFactory.getInstance().removeDriver();
			   }
			} catch (Exception e2) {
				System.out.println("Error en phantom 2" + e2);
				return null;
			}
			
			return null;
		} 
		
		
	}


	@Override
	public Product parseProductFromURL(String urlStr, Task task) {
		try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(urlStr);
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.NUTRITIONDEPOT_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.NUTRITIONDEPOT_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			
	 		 
			String price = ContentParser.parseContent(urlContent, Regex.NUTRITIONDEPOT_PRICE); 
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.NUTRITIONDEPOT_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}

			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),task,name,Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {
			return null;
		}
	}



}