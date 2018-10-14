package com.dataprice.model.crawlers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.crawlers.utils.ContentParser;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.crawlers.utils.FetchResults;
import com.dataprice.model.crawlers.utils.MultipleContentParser;
import com.dataprice.model.crawlers.utils.PageFetcher;
import com.dataprice.model.crawlers.utils.PhantomFactory;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;



/**
 * Special crawler for generating fragance catalogue, not used in the GUI
 * @author rene
 *
 */
public class Catalogue extends AbstractCrawler{

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

				for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'add-product-')]"))) {
					String brand = we.findElement(By.cssSelector("div.product-manufacturer")).getText(); 						
					brand = brand.trim();				
					String url = we.findElement(By.cssSelector("a.product-image")).getAttribute("href");	
					linksList.add(new CrawlInfo(url,"","",0.0,"","",brand));
		        }

				 while (driver.findElements(By.cssSelector("a.next.i-next")).size()>0) {
					driver.findElement(By.cssSelector("a.next.i-next")).click();						
					Thread.sleep(Configuration.DRIVERDELAY);
					
					for (WebElement we : driver.findElements(By.xpath("//*[starts-with(@id, 'add-product-')]"))) {
						String brand = we.findElement(By.cssSelector("div.product-manufacturer")).getText(); 						
						brand = brand.trim();				
						String url = we.findElement(By.cssSelector("a.product-image")).getAttribute("href");	
						linksList.add(new CrawlInfo(url,"","",0.0,"","",brand));
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
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Special method 
	 * @param crawlInfo
	 * @param taskDAO
	 * @return
	 */
	
	public List<Product> parseProductsFromUrl(CrawlInfo crawlInfo, Task taskDAO) {
        try {
		    
        	ArrayList<Product> productList = new ArrayList<Product>();
	    	 
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			System.out.println("La url es:" + crawlInfo.getUrl());
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new ArrayList<Product>();
	    	}
		
			String urlContent = urlResponse.getContent(); 

			
        	ArrayList<String> boxes = MultipleContentParser.parseMultipleContent(urlContent, Regex.SEPHORA_BOXES);

        	if (boxes==null)
				return new ArrayList<Product>();
        	
        	
        	
    		String name = ContentParser.parseContent(urlContent, Regex.SEPHORA_NAME);
			if (name==null)
				return new ArrayList<Product>();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			String description = "";
	
			String sku = "";

		//	String brand = "";
			
		//	String brand = ContentParser.parseContent(urlContent, Regex.SEPHORA_BRAND);
		//	System.out.println("marca" + brand);
		//	if (brand ==null)
		//		brand = "";
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.SEPHORA_IMAGESRC);
			if (imageUrl ==null)
				imageUrl = "";
			
			
			for (int i = 0;i<boxes.size();i++){
				
				String id = ContentParser.parseContent(boxes.get(i), Regex.SEPHORA_ID);
				String upc = id;
				
				String presentation = ContentParser.parseContent(boxes.get(i), Regex.SEPHORA_PRESENTATION);
				String fullName = name + " " + presentation;
				
				String boxImageUrl = ContentParser.parseContent(boxes.get(i), Regex.SEPHORA_BOXIMAGESRC);
		
				
				if (id!=null && presentation!=null) {				
					if (boxImageUrl!=null) {
						productList.add(new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),null,fullName,description,0.00,boxImageUrl,crawlInfo.getUrl(),sku,upc,crawlInfo.getBrand(),taskDAO.getTaskName()));
					}else {
						productList.add(new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),null,fullName,description,0.00,imageUrl,crawlInfo.getUrl(),sku,upc,crawlInfo.getBrand(),taskDAO.getTaskName()));
					}				
				
				}

			}
			
		    return productList;
		
			
		} catch (Exception e) {
			return null;
		}
	}
	
	
	@Override
	public String getCrawlingStrategy() {
		return "Catalogue";
	}

}
