package com.dataprice.model.crawlers;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
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
import org.openqa.selenium.support.ui.Select;


@Component
public class HomeDepot extends AbstractCrawler{

	@Override
	public List<CrawlInfo> getUrlsFromTask(Task taskDAO) {
		WebDriver driver = null;
		
		try {
			String[] geography = taskDAO.getTaskName().split(",");

			//Initialization Phase
			driver = PhantomFactory.getInstance().getDriver();		
			driver.get("http://www.homedepot.com.mx");
			Thread.sleep(5000);
	
			//Geographic initialization
			System.out.println("Ir a buscar tiendas");  
			driver.get("https://www.homedepot.com.mx/buscador-de-tiendas");
			Thread.sleep(5*1000);
		
			Select dropdown = new Select(driver.findElement(By.cssSelector("select#selectState.drop_down_country")));	
			//dropdown.selectByValue("13634");
			dropdown.selectByVisibleText(geography[0]);
			System.out.println("Estado seleccionado");
			Thread.sleep(3*1000);
			
			
			Select dropdown1 = new Select(driver.findElement(By.cssSelector("select#selectCity.drop_down_country")));	
			dropdown1.selectByVisibleText(geography[1]);
			System.out.println("Ciudad seleccionada");
			Thread.sleep(3*1000);
			
			
			driver.findElement(By.cssSelector("a#cityGo.button_primary")).click();
			System.out.println("Buscar tienda");
			Thread.sleep(5*1000);
			
			
		//	driver.findElement(By.xpath("//*[contains(@id, 'addPhysicalStoreToCookie')]/a")).click();
			
			 WebElement el = driver.findElements(By.xpath("//*[contains(text(),\"Hacer mi tienda\")]")).get(0);
			   Actions builder = new Actions(driver); 
			   builder.moveToElement(el, el.getLocation().x, el.getLocation().y);
			   Locatable hoverItem = (Locatable) el;
			   Mouse mouse = ((HasInputDevices) driver).getMouse();
			   mouse.mouseMove(hoverItem.getCoordinates());
			   el.click();	
			   
			System.out.println("Clic en la primera tienda");
			Thread.sleep(5*1000);
			
			driver.findElement(By.cssSelector("a#btnshopping.btn.btn2")).click();
			System.out.println("Continuar comprando");
			Thread.sleep(3*1000);
			System.out.println(driver.getCurrentUrl());
			Thread.sleep(5*1000);
			
			
			//Go to task
			System.out.println("Volviendo a dar clic");
		    driver.get(taskDAO.getSeed());
			System.out.println("Inicializando Phantom");
			LinkedList<CrawlInfo> linksList = new LinkedList<CrawlInfo>();
			Thread.sleep(5000);
			
			
			//Navigation		   
			int con = 0;
			for (WebElement we : driver.findElements(By.cssSelector("div.product_name a"))) {	
				String url = we.getAttribute("href");
				System.out.println(url);
				//String name = driver.findElements(By.cssSelector("div.product_name span")).get(con).getText();
				//System.out.println(name);
				
				String price = driver.findElements(By.xpath("//*[contains(@id, 'product_price_')]")).get(con).getText();
				System.out.println(price);
				//$54.50 $3850 Ahorras: $16.00
				if (!price.equals("")) {	
					if (price.contains("Ahorras")) {
						String[] prices = price.split(" ");
						price = prices[2];
					}
					price = price.replace(",", "");
	  				price = price.replace("$", "");
	  				price = price.trim();
	  				price = price.substring(0, price.length() - 2) + "." + price.substring(price.length() - 2, price.length());
	  				System.out.println(price);	
	  			//	Thread.sleep(5000);
					linksList.add(new CrawlInfo(url,Double.valueOf(price)));			    
				 }
				
				con++;
			 }
		
			
			//Check
			
			int total_products_int= Integer.valueOf(driver.findElement(By.cssSelector("span.number")).getText());
		
			int base = 20;
			if (total_products_int>20) {
				int loops = total_products_int/20;
				
				for (int i =0;i<loops;i++) {
					
					driver.get(taskDAO.getSeed().replaceAll("productBeginIndex:(\\d+)&", "productBeginIndex:" + Integer.toString(base) + "&"));
					Thread.sleep(5000);	
					
					con = 0;
					for (WebElement we : driver.findElements(By.cssSelector("div.product_name a"))) {	
						String url = we.getAttribute("href");
						System.out.println(url);
						//String name = driver.findElements(By.cssSelector("div.product_name span")).get(con).getText();
						//System.out.println(name);
						
						String price = driver.findElements(By.xpath("//*[contains(@id, 'product_price_')]")).get(con).getText();
						System.out.println(price);
						//$54.50 $3850 Ahorras: $16.00
						if (!price.equals("")) {	
							if (price.contains("Ahorras")) {
								String[] prices = price.split(" ");
								price = prices[2];
							}
							price = price.replace(",", "");
			  				price = price.replace("$", "");
			  				price = price.trim();
			  				price = price.substring(0, price.length() - 2) + "." + price.substring(price.length() - 2, price.length());
			  				System.out.println(price);	
			  			//	Thread.sleep(5000);
							linksList.add(new CrawlInfo(url,Double.valueOf(price)));			    
						 }
						
						con++;
					 }
								
					
					base = base + 20;
				}
		
			}
				
				
				
				
				
				
				
			/**
			 while (!(driver.findElements(By.cssSelector("a#WC_SearchBasedNavigationResults_pagination_link_right_categoryResults.right_arrow.invisible")).size()>0)){	
				    System.out.println("Si entro");
				    Thread.sleep(Configuration.DRIVERDELAY);
				    
				    WebElement el = driver.findElements(By.cssSelector("a#WC_SearchBasedNavigationResults_pagination_link_right_categoryResults.right_arrow")).get(0);
					   Actions builder = new Actions(driver); 
					   builder.moveToElement(el, el.getLocation().x, el.getLocation().y);
					   Locatable hoverItem = (Locatable) el;
					   Mouse mouse = ((HasInputDevices) driver).getMouse();
					   mouse.mouseMove(hoverItem.getCoordinates());
					   el.click();	
					   
				//	driver.findElement(By.cssSelector("a#WC_SearchBasedNavigationResults_pagination_link_right_categoryResults.right_arrow")).click();	
					System.out.println(driver.getCurrentUrl());
					Thread.sleep(Configuration.DRIVERDELAY);
					for (WebElement we : driver.findElements(By.cssSelector("div.product_name a"))) {	
						System.out.println(we.getAttribute("href"));
					    linksList.add(new CrawlInfo(we.getAttribute("href")));
					 }					
			 }	
			*/
	
			//Destroy
			PhantomFactory.getInstance().removeDriver();		
			Thread.sleep(1000);
			return linksList;
		}  catch (Exception e) {
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
			    System.out.println("-------------------------------------------------------"); 
			    System.out.println("url: " + crawlInfo.getUrl());
			  			    		    
			    //PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
		    			
			    //FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
				
				
				
			    //if (urlResponse == null){  //Task fatal error.		
			    //		return null;
			    //	}
				
			    //	if (urlResponse.getContent().equals("")){   
			    //		return new Product();
			    //	}
			
				

				String id = ContentParser.parseContent(crawlInfo.getUrl(), Regex.HOMEDEPOT_ID);
				System.out.println(id);
				if (id==null)
					return new Product();
				
				String name = ContentParser.parseContent(crawlInfo.getUrl(), Regex.HOMEDEPOT_NAME);
				if (name.contains("/")) {
					String[] names = name.split("/");
					name = names[names.length-1];
				}
				name = name.replace("-", " ");
				System.out.println(name);
				
				
				String description = "";
				
				String imageUrl = "";
							
				
				System.out.println(imageUrl);
				
				String sku = "";
				
				String brand = "";
			
				String upc = "";			
			
			    return new Product(id+getCrawlingStrategy()+taskDAO.getTaskName(),id,getCrawlingStrategy(),taskDAO,name,description,crawlInfo.getPrice(),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());			
				
			} catch (Exception e) {
				return null;
			}
	}

	@Override
	public String getCrawlingStrategy() {
		return "HomeDepot";
	}

}