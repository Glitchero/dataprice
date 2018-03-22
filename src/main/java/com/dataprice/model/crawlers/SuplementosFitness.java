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
public class SuplementosFitness extends AbstractCrawler{
	
	
	private WebDriver driver = null;
	private List<String> LinksList;
	private Task taskDAO;


	@Override
	public String getCrawlingStrategy() {
		return "SuplementosFitness";
	}

	@Override
	public boolean init(String seed) throws InterruptedException{
		this.driver = PhantomFactory.getInstance().getDriver();
		this.driver.get(seed);
		System.out.println("Inicializando Phantom");
	    LinksList = new LinkedList<String>();
	    Thread.sleep(1000);
	    return true;
	}

	@Override
	public void navigatePages() throws InterruptedException {
		
		getProductsUrl();
		
		while (driver.findElements(By.cssSelector("a.next.page-number")).size()>0){
			  
			   driver.findElement(By.cssSelector("a.next.page-number")).click();
			
			   Thread.sleep(Configuration.DRIVERDELAY);
		      
			   getProductsUrl();
		}
						
		
		
	}

	@Override
	public void getProductsUrl() {
		for (WebElement we : driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a"))) {	   
			   // System.out.println(we.getAttribute("href"));
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
			
			String id = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_ID);
			if (id==null)
				return null;
			
			String name = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_NAME);
			if (name==null)
				return null;
			name = name.trim();
			
	 		 
			String price = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_PRICE); 
			if (price == null) {  
				return null;
			}

			String imageUrl = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_IMAGEURL);
			if (imageUrl == null) {  
				return null;
			}
					
			
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
			destroy();
			return LinksList;
		} catch (InterruptedException ie) {
			if (this.driver!=null) {
				PhantomFactory.getInstance().removeDriver();
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error en phantom" + e);
			if (this.driver!=null) {
				PhantomFactory.getInstance().removeDriver();
			}
			return new LinkedList<String>();
		}
	}

	@Override
	public void destroy() throws InterruptedException{
		PhantomFactory.getInstance().removeDriver();		
		Thread.sleep(1000);
	}

}