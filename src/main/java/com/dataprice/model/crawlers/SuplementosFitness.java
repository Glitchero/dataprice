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
	
	
	private WebDriver driver;
	private List<Product> ProdList;
	private List<String> LinksList;
	

	@Override
	public List<Product> getProductsFromTask(Task taskDAO) {
		initialization(taskDAO.getSeed());
		navigatePages();
		 
		for (int i=0;i<this.LinksList.size(); i++){
				Product prod = parseProductFromURL(this.LinksList.get(i));
				if (prod!=null){
						   this.ProdList.add(prod);
				}
		}
				
		this.LinksList = null; //GC deal with this.
				
		
		return ProdList;
	}

	@Override
	public String getCrawlingStrategy() {
		return "SuplementosFitness";
	}

	@Override
	public boolean initialization(String seed) {
		this.driver = PhantomFactory.getInstance().getDriver();
		this.driver.get(seed);
		
		//Check if at least one product is present on the seed!. 
		Boolean isPresent = this.driver.findElements(By.xpath("//*[@id='main']/div/div[1]/div/div[1]/div/div/div[2]/div[1]/div[1]/a")).size() > 0;

		if (isPresent){
					System.out.println("Inicializando Phantom");
					LinksList = new LinkedList<String>();
					ProdList = new LinkedList<Product>();
					return true;
		}else{
		    		System.out.println("No products found");
		    		PhantomFactory.getInstance().removeDriver();		
		    		return false;
		 }
		
	}

	@Override
	public void navigatePages() {
		getProductsUrl();
		
		while (driver.findElements(By.cssSelector("a.next.page-number")).size()>0){
			  
			   driver.findElement(By.cssSelector("a.next.page-number")).click();
			
			   try {   
				   Thread.sleep(Configuration.DRIVERDELAY);
		    	} catch (InterruptedException e) {
				   e.printStackTrace();
		 	    }  
			     
			getProductsUrl();
		}
		
				
		PhantomFactory.getInstance().removeDriver();		
		
		
		
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
		 PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(urlStr);
			
			if (urlResponse == null){
				return null;
	    	}
	    	
			String urlContent = urlResponse.getContent(); 
			
			String id = ContentParser.parseContent(urlContent, Regex.SUPLEMENTOSFITNESS_ID);
			if (id==null)
				return null;
			
			ProductKey pKey = new ProductKey();
			pKey.setProductId(id);
			pKey.setRetail(getCrawlingStrategy());
			
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
					
			
			return new Product(name,Double.valueOf(price),imageUrl,urlStr, pKey);
	}

}
