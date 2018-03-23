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
import com.dataprice.model.entity.ProductKey;
import com.dataprice.model.entity.Task;

@Component
public class Chedraui extends AbstractCrawler{
	
	
	private WebDriver driver = null;
	private List<String> LinksList;


	@Override
	public String getCrawlingStrategy() {
		return "Chedraui";
	}

	@Override
	public synchronized boolean init(String seed) throws InterruptedException {
		this.driver = PhantomFactory.getInstance().getDriver();
		this.driver.get(seed);
		System.out.println("Inicializando Phantom");
		this.LinksList = new LinkedList<String>();
		Thread.sleep(1000);
		return true;
		
	}

	@Override
	public void navigatePages() throws InterruptedException {
        
		getProductsUrl();
		
		while (driver.findElements(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).size()>0){
		    driver.findElement(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).click();
			
		    Thread.sleep(Configuration.DRIVERDELAY);
			
			getProductsUrl();
		}
		
		
	}

	@Override
	public void getProductsUrl() {
		for (WebElement we : driver.findElements(By.xpath("//*[@id='plp_display']/li/a"))) {	   
		    this.LinksList.add(we.getAttribute("href"));
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
			
			String id = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			
	 		 
			String price = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_PRICE); 
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
				
			imageUrl = "https://www.chedraui.com.mx" + imageUrl;
			
				
			return new Product(id,getCrawlingStrategy(),task,name,Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<String> getUrlsFromTask(Task taskDAO) {
		try {
			init(taskDAO.getSeed());
			navigatePages();
			destroy();
			return LinksList;
		} catch (Exception e) {
			//System.out.println("Error en phantom" + e);
			if (this.driver!=null) {
				PhantomFactory.getInstance().removeDriver();
			}
			return null;
		}
	}

	@Override
	public void destroy() throws InterruptedException{
		PhantomFactory.getInstance().removeDriver();		
		Thread.sleep(1000);
	}

}