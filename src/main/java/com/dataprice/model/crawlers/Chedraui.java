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
	
	
	private WebDriver driver;
	private List<Product> ProdList;
	private List<String> LinksList;
	private Task taskDAO;

	@Override
	public String getCrawlingStrategy() {
		return "Chedraui";
	}

	@Override
	public boolean init(String seed) {
		this.driver = PhantomFactory.getInstance().getDriver();
		this.driver.get(seed);
		System.out.println("Inicializando Phantom");
		this.LinksList = new LinkedList<String>();
		ProdList = new LinkedList<Product>();
		return true;
		
	}

	@Override
	public void navigatePages() {
        
		getProductsUrl();
		
		while (driver.findElements(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).size()>0){
		    driver.findElement(By.cssSelector("a.glyphicon.glyphicon-chevron-right")).click();
			try {
				Thread.sleep(4*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getProductsUrl();
		}
		
				
		PhantomFactory.getInstance().removeDriver();		
		
		
		
	}

	@Override
	public void getProductsUrl() {
		for (WebElement we : driver.findElements(By.xpath("//*[@id='plp_display']/li/a"))) {	   
		    this.LinksList.add(we.getAttribute("href"));
        }
	}

	@Override
	public Product parseProductFromURL(String urlStr) {
		try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(urlStr);
			
			if (urlResponse == null){
				return null;
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_ID);
			if (id==null)
				return null;
			
			String name = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_NAME);
			if (name==null)
				return null;
			name = name.trim();
			
	 		 
			String price = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_PRICE); 
			if (price == null) {  
				return null;
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.CHEDRAUI_IMAGEURL);
			if (imageUrl == null) {  
				return null;
			}
				
			imageUrl = "https://www.chedraui.com.mx" + imageUrl;
			
				
			return new Product(id,getCrawlingStrategy(),taskDAO,name,Double.valueOf(price),imageUrl,urlStr);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<String> getUrlsFromTask(Task taskDAO) {
		try {
			this.taskDAO = taskDAO;
			init(taskDAO.getSeed());
			navigatePages();
			return LinksList;
		} catch (Exception e) {
			return new LinkedList<String>();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}