package com.dataprice.model.crawlers;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.springframework.stereotype.Component;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;


@Component
public class ElePerfumes extends AbstractCrawler{

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
			Thread.sleep(Configuration.DRIVERDELAY); 
			System.out.println("Current Url: " + driver.getCurrentUrl());
		//	System.out.println("Current Url: " + driver.getPageSource());
	    
		    for (WebElement we : driver.findElements(By.cssSelector("form.addCart"))) {	
			
				System.out.println("Si entrooooooooooooooo");
	        
				System.out.println("Direeccion : " + we.findElement(By.cssSelector("a")).getAttribute("href"));
				System.out.println("Precio: " + we.findElement(By.cssSelector("p.prod-price.text-primary")).getText());
				System.out.println("titulo: " + we.findElement(By.xpath("div/form/div/div/h5/a[2]")).getText());
				String url = we.findElement(By.cssSelector("a")).getAttribute("href");
	        	  
				String name = we.findElement(By.cssSelector("span.test")).getText();
				name = name.trim(); 
	        	  
				String price = we.findElement(By.cssSelector("div.price")).getText(); 
				price = price.replace(",", "");
				price = price.replace("$", "");
				price = price.replaceAll("[^\\d.]", "");
				price = price.trim();
				price = price.substring(0, price.length()-2);
	        	  
				String seller = we.findElement(By.cssSelector("div.productList-sellerName")).getText();
				seller = seller.replace("Vendido por:", "");
				seller = seller.trim();
				linksList.add(new CrawlInfo(url,name,Double.valueOf(price),seller));
	        		   
	        	  
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCrawlingStrategy() {
		return "ElePerfumes";
	}

}
