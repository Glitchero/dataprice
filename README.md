# DATAPRICE 

Dataprice uses open source software in order to fetch online stores and extract all the necessary information for benchmarking prices. It also provides a framework for extracting e-commerce products data. Dataprice is built in java.

## Use cases

 - Track and compare prices for products in different e-commerce stores.
 - Create your own e-commerce feeds in order to update price comparison shopping engines
 - Scrape online stores (id, name, price, description, url, image, sku, brand and category)
          
## Main Features

 - Create, delete and run scraping tasks with the GUI. 
 - Import and Export tasks with JSON
 - Multithread, you can run several tasks at the same time.
 - Possibility to add a proxy to yout task, in order to avoid ip blocking.
 - Easy installation with docker, in order words, great portability
 - Matching section for normalizing data.
 - Price matrices reports
 - One month price history with plot included. 
 - Authentication and the possibility to change password 
 - Ability to export to csv or excel the data


## Getting Started

These instructions will get you a copy of the project up and running on your server machine.

### Prerequisites

In order to install dataprice software, you will need:

*   Docker (tested on 17.03.2-ce)
*   Java 8
*   Maven (tested on 3.0.5)
*   Putty (for SSH connection, windows only)

### Installing

Please follow the instructions:

```
# Step 1: Install mysql using docker
docker run --name demo-mysql -e MYSQL_ROOT_PASSWORD=12345 -e MYSQL_DATABASE=univers -d mysql:5.7

# Step 2: Install phantomjs driver using docker
docker run --restart unless-stopped -d -p 8910:8910 wernight/phantomjs phantomjs --webdriver=8910

# Step 3
Clone dataprice repository 

# Step 4 : Inside the dataprice folder run the following command.
mvn clean package docker:build

# Step 5 : Once the image is created, run it.
docker run -d     --name dataprice-app     --link demo-mysql:mysql     -p 8080:8080     -e DATABASE_HOST=demo-mysql     -e DATABASE_PORT=3306     -e DATABASE_NAME=univers     -e DATABASE_USER=root     -e DATABASE_PASSWORD=12345     -e DATABASE_DRIVER=com.mysql.jdbc.Driver      glitchero/dataprice

```

In order to see the logs, please use the command:

```
docker logs dataprice-app
```

Finally, go to the browser and see the application in http://xxx.xx.xxx.xxx:8080/login (for local http://localhost:8080/login).

The user is admin and the password is 12345


## Installation videos (Subtitles in English, please activate them)

 - [Installation in Ubuntu Server Digital Ocean](https://www.youtube.com/watch?v=IKwTQ51pTnc&t=4s)
 - [How to Test a Scraper Locally with eclipse](https://www.youtube.com/watch?v=-r4mlMg-WpI&list=PLuAkh4GnBZuG1Rw49SC_R2ZwLWhXuu2pX)

## QuickStart

In order to create a task you will need to extend the class __AbstractCrawler__. Also, remember to add __@Component__ on top of the class. The package has to be com.dataprice.model.crawlers.

```
@Component
public class Laeuropea extends AbstractCrawler{

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
			for (WebElement we : driver.findElements(By.cssSelector("div.c-overlay-content a"))) {	
				linksList.add(new CrawlInfo(we.getAttribute("href"),"","",0.0,"","",""));
			}

			while (!(driver.findElements(By.cssSelector("li.c-next.disabled")).size()>0)){		
				driver.findElement(By.cssSelector("li.c-next a")).click();	
				Thread.sleep(Configuration.DRIVERDELAY);
				for (WebElement we : driver.findElements(By.cssSelector("div.c-overlay-content a"))) {	
					linksList.add(new CrawlInfo(we.getAttribute("href"),"","",0.0,"","",""));
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
        try {
		    
			PageFetcher pageFetcher = PageFetcher.getInstance(getCrawlingStrategy());
	    	
			FetchResults urlResponse = pageFetcher.getURLContent(crawlInfo.getUrl(),1000);
			System.out.println(crawlInfo.getUrl());
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 

			String id = ContentParser.parseContent(urlContent, Regex.LAEUROPEA_ID);
			System.out.println(id);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.LAEUROPEA_NAME);
			System.out.println(name);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			
			String description = "";
			
			String price = ContentParser.parseContent(urlContent, Regex.LAEUROPEA_PRICE); 	
			System.out.println(price);
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.LAEUROPEA_IMAGEURL);
			System.out.println(imageUrl);
			if (imageUrl == null) {  
				return new Product();
			}
			
			imageUrl = "https://www.laeuropea.com.mx" + imageUrl;
						
			String sku = "";
			
			String brand = "";
			
			String upc = "";

		    return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Laeuropea";  //Name of the store. Recommendation, use the class name.
	}

}
```

As can be seen in the above code, there are three main functions that should be overridden:

*   __getUrlsFromTask__ .- Receives a task object (which contains the seed url) and return a crawlInfo object. The crawlInfo object contains all the information that can be gathered from a page with pagination, for example the url, name or price. In the example above, the line of code "new CrawlInfo(we.getAttribute("href"),"","",0.0,"","","")" only stores the url in the crawlInfo object. crawlInfo is then passed to the method parseProductFromURL.
*   __parseProductFromURL__ .- Receives a crawlInfo and a task object. This method extracts the information from a product page (the product page dosen't have a pagination) and returns a product object. The product object includes all the information from a product. 
*   __getCrawlingStrategy__ .- Returns the store name.

Use the regex class (in package com.dataprice.model.crawlers.utils) to store the regexes:

```
public final static String LAEUROPEA_ID = "<div class=\"c-product-meta detailItem\" data-code=\"(.*?)\"";
public final static String LAEUROPEA_NAME = "<h3 class=\"c-font-bold\">(.*?)</h3>";
public final static String LAEUROPEA_PRICE = "<div class=\"c-product-price\">(.*?)<";
public final static String LAEUROPEA_IMAGEURL ="<div class=\"c-zoom\"><img src=\"(.*?)\"";

```

Finally create the country and the reail in the database using the Initializer class (in package com.dataprice):

```
Country mexico = new Country();
mexico.setCountryId(1);
mexico.setCountryName("México");
mexico.setCurrency("Peso MXN");
mexico.setNickname("MX");
addCountryService.saveCountry(mexico);	
			
Retail retail = new Retail();
retail.setRetailId(1); //Assign a number
retail.setRetailName("La Europea");
retail.setCrawlerName("Laeuropea"); // The name given in getCrawlingStrategy.
retail.setCountry(mexico);		
addRetailService.saveRetail(retail);
```

### Use a proxy for difficult web-sites like Amazon

Use PhantomFactoryWithProxy and PageFetcherWithProxy instead of PhantomFactory and PageFetcher respectively. Finally, add your credentials in the class configuration (in package com.dataprice.model.crawlers.utils).

```
/** Proxy Configuration*/
	
public final static String ProxyHost = "Write here proxyHost";
public final static int ProxyPort = 0; //Write here proxyport

public final static String ProxyUsername = "Write here Username";
public final static String ProxyPassword = "Write here Password";
```

### More examples (Have fun checking our examples)

You can find these classes in package com.dataprice.model.crawlers.

Crawler  | Official Website   | Video
------------- | ------------- | -------------
Amazon             | https://www.amazon.com.mx/  | [Amazon video](https://www.youtube.com/watch?v=N878vHbl2O8) 
Arome              | https://www.arome.mx/
Chedraui           | https://www.chedraui.com.mx/
Laeuropea          | https://www.laeuropea.com.mx
Linio              | https://www.linio.com.mx/
Liverpool          | https://www.liverpool.com.mx
MercadoLibre       | https://www.mercadolibre.com.mx/
Osom               | https://www.osom.com
PerfumesMexico     | http://www.perfumesmexico.com/
PerfumesOnline     | http://www.perfumesonline.com.mx/
Prissa             | https://www.prissa.com.mx
Sanborns           | https://www.sanborns.com.mx/
Soriana            | https://www.soriana.com
Walmart            | https://www.walmart.com.mx/
SuperWalmart  	   | https://super.walmart.com.mx/
Suplementosfitness | https://www.suplementosfitness.com.mx/


## Do you have questions or suggestions?, need a specific store crawler? you can contact me on facebook.   
[DATAPRICE Facebook](https://www.facebook.com/Dataprice-197830137522387) 

