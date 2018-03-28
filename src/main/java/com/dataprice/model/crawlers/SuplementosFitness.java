package com.dataprice.model.crawlers;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
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
public class SuplementosFitness extends AbstractCrawler{
	
	

	@Override
	public String getCrawlingStrategy() {
		return "SuplementosFitness";
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
			Thread.sleep(1000);
			
			//Navigation
			for (WebElement we : driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a"))) {	   
				linksList.add(we.getAttribute("href"));
		        }
			
			while (driver.findElements(By.cssSelector("a.next.page-number")).size()>0){
				  
				   driver.findElement(By.cssSelector("a.next.page-number")).click();
				
				   Thread.sleep(Configuration.DRIVERDELAY);
			      
				   for (WebElement we : driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a"))) {	   
					   linksList.add(we.getAttribute("href"));
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
	public Product parseProductFromURL(String urlStr, Task task) {
		try {
		    
			if (Thread.currentThread().isInterrupted()) {
                System.out.println("....run()::Extraction::Scraping::isInterrupted");
                Thread.sleep(1000);
            } 
			
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(urlStr);
			
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_NAME);
			if (name==null)
				return new Product();
			
			name = name.trim();
			
			
			String price = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_PRICE); 
			if (price == null) {  
				return new Product();
			}

			String imageUrl = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}

			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),task,name,Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {			
			return null;
		}
	}

	


}