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

## How to create a task

In order to create a task you will need to extend the class __AbstractCrawler__. Also, remember to add __@Component__ on top of the class.

```
//Web page: https://www.arome.mx/

@Component
public class Arome extends AbstractCrawler{

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

				 for (WebElement we : driver.findElements(By.xpath("//*[@id=\"pagination_contents\"]/div[3]/div/div/form/div/div[2]/a"))) {	
					linksList.add(new CrawlInfo(we.getAttribute("href"),"","",0.0,"","",""));
			        }

				 while (driver.findElements(By.cssSelector("a.ty-pagination__item.ty-pagination__btn.ty-pagination__next.cm-history.cm-ajax")).size()>0){		
					driver.findElement(By.cssSelector("a.ty-pagination__item.ty-pagination__btn.ty-pagination__next.cm-history.cm-ajax")).click();						
					Thread.sleep(Configuration.DRIVERDELAY);
					for (WebElement we : driver.findElements(By.xpath("//*[@id=\"pagination_contents\"]/div[3]/div/div/form/div/div[2]/a"))) {	
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
			
			if (urlResponse == null){  //Task fatal error.		
				return null;
	    	}
			
			if (urlResponse.getContent().equals("")){   
				return new Product();
	    	}
		
			String urlContent = urlResponse.getContent(); 

			String id = ContentParser.parseContent(urlContent, Regex.AROME_ID);
			if (id==null)
				return new Product();
			
			String name = ContentParser.parseContent(urlContent, Regex.AROME_NAME);
			if (name==null)
				return new Product();
			name = name.trim();
			name = Jsoup.parse(name).text();
			String description = "";
			/**
			String description = ContentParser.parseContent(urlContent, Regex.MERCADOLIBRE_DESCRIPTION);
			if (description==null)
				description = "No disponible";  //Unlike name, sometimes we don't have a description.
			description = description.trim();
			description = Jsoup.parse(description).text();
			*/
			
			String price = ContentParser.parseContent(urlContent, Regex.AROME_PRICE); 	
			if (price == null) {  
				return new Product();
			}

			price = price.replace(",", "");
			price = price.replace("$", "");
			price = price.trim();
			
			String imageUrl = ContentParser.parseContent(urlContent, Regex.AROME_IMAGEURL);
			if (imageUrl == null) {  
				return new Product();
			}
			
						
			String sku = "";
			
			String brand = ContentParser.parseContent(urlContent, Regex.AROME_BRAND);	
			if (brand == null) {  
				brand = ""; //Unlike name, sometimes we don't have a brand.
			}
			
		//	String upc = id;
			String upc = "";

		      return new Product(id+getCrawlingStrategy(),id,getCrawlingStrategy(),taskDAO,name,description,Double.parseDouble(price),imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
		  //	return new Product(id+"Catalogue",id,"Catalogue",null,name,description,0.00,imageUrl,crawlInfo.getUrl(),sku,upc,brand,taskDAO.getTaskName());
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCrawlingStrategy() {
		return "Arome";
	}

}

```

### Watch our application working with different tasks 
[Arome México](https://www.youtube.com/watch?v=N878vHbl2O8) 

