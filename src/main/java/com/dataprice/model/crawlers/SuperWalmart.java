package com.dataprice.model.crawlers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
public class SuperWalmart extends AbstractCrawler{

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
			
			String name = "";
			String price = "";
			String url = "";
			String image = "";
			int counter = 0;
	   								
			for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[3]"))) {	 	
				name = we.findElements(By.xpath("//*[@id=\"root\"]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[2]/a/p")).get(counter).getAttribute("innerHTML"); 			
				System.out.println(name);
				
				if (we.findElements(By.tagName("p")).size()>1) {
					price = we.findElements(By.tagName("p")).get(1).getAttribute("innerHTML");
				}else {
					price = we.findElement(By.tagName("p")).getAttribute("innerHTML");
				}
		
				System.out.println(price);
				price = price.replace(",", "");
	  			price = price.replace("$", "");
	  			price = price.trim();
	  			
				url = we.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[1]/a")).get(counter).getAttribute("href");
				System.out.println(url);
				
				image = we.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[1]/a/div/img")).get(counter).getAttribute("src");
				System.out.println(image);
				linksList.add(new CrawlInfo(url,"",name,Double.valueOf(price),image,"",""));
				counter++;				
			}
		
			counter = 0;
	//		int con = 0;			
			
			Thread.sleep(Configuration.DRIVERDELAY);
			while (driver.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[5]/div/span")).size()>0) { //In case we have pagination.
				
				Thread.sleep(Configuration.DRIVERDELAY);
		//		con++;
				WebElement el = driver.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[5]/div/span")).get(driver.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[5]/div/span")).size()-1);
				JavascriptExecutor js = (JavascriptExecutor)driver; 
				js.executeScript("arguments[0].click();", el); 
				
				Thread.sleep(Configuration.DRIVERDELAY);
				
				
				for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[3]"))) {	 	
					name = we.findElements(By.xpath("//*[@id=\"root\"]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[2]/a/p")).get(counter).getAttribute("innerHTML"); 			
					System.out.println(name);
					
					if (we.findElements(By.tagName("p")).size()>1) {
						price = we.findElements(By.tagName("p")).get(1).getAttribute("innerHTML");
					}else {
						price = we.findElement(By.tagName("p")).getAttribute("innerHTML");
					}
			
					System.out.println(price);
					price = price.replace(",", "");
		  			price = price.replace("$", "");
		  			price = price.trim();
		  			
					url = we.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[1]/a")).get(counter).getAttribute("href");
					System.out.println(url);
					
					image = we.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[4]/div[2]/div/div/div/div/div[1]/a/div/img")).get(counter).getAttribute("src");
					System.out.println(image);
					
					linksList.add(new CrawlInfo(url,"",name,Double.valueOf(price),image,"",""));
					counter++;				
				}
				
				
				
				counter = 0;
				
		//	if (con>10)
		//		break;
		
				if (driver.findElements(By.xpath("//*[starts-with(@id, 'root')]/div/div/main/div[1]/section/div[1]/div[5]/div/span")).size()==3)
					break;
				
				
			}
			
			//Destroy
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
			
		} catch (Exception e) {
			System.out.println("Error en phantom" + e);
			
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
		    /**
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl());
			System.out.println(crawlInfo.getUrl());
			if (urlResponse == null){  //Task fatal error.
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			*/
        	
			String id = ContentParser.parseContent(crawlInfo.getUrl(), Regex.SUPERWALMART_ID);
			System.out.println("el id es:" + id);
			if (id==null)
				return new Product();
			
			String name = crawlInfo.getProductName();	
			System.out.println("El nombre es :" + name);
			String imageUrl = crawlInfo.getImageURl();
			if (imageUrl.length()>223)
				imageUrl="";
		//	System.out.println("La imagen es :" + imageUrl);
			String sku = "";
			String upc = "";
			String brand = "";
			
			return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,"",crawlInfo.getPrice(),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "SuperWalmart";
	}

}
