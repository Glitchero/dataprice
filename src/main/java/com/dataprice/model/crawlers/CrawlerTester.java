package com.dataprice.model.crawlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.crawlers.utils.ContentParser;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.crawlers.utils.Regex;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Retail;
import com.dataprice.model.entity.Task;
import com.dataprice.service.addproductservice.AddProductService;
import com.dataprice.service.crawltask.CrawlTaskServiceImpl;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.openqa.selenium.By;
import org.slf4j.Logger;


public class CrawlerTester {

		
	public static void main(String[] args) {

	

		Task task = new Task();
		task.setSeed("http://www.homedepot.com.mx/materiales-de-construccion/basicos-de-construccion#facet:&productBeginIndex:0&facetLimit:&orderBy:&pageView:grid&minPrice:&maxPrice:&pageSize:&");
	//	task.setSeed("http://www.homedepot.com.mx");
		task.setTaskName("Ciudad de México,Del. Álvaro Obregón");
		
		Crawler crawler = new HomeDepot();
		List<CrawlInfo> productsInfo = crawler.getUrlsFromTask(task);
	
		for (CrawlInfo crawlInfo : productsInfo) {
		    Product p = crawler.parseProductFromURL(crawlInfo, task);
			System.out.println(p);
		}

		
	/**	
		int total_products_int= 126;
		int base = 20;
	    String url = "http://www.homedepot.com.mx/materiales-de-construccion/basicos-de-construccion#facet:&productBeginIndex:0&facetLimit:&orderBy:&pageView:grid&minPrice:&maxPrice:&pageSize:&";
		if (total_products_int>20) {
			int loops = total_products_int/20;
			
			for (int i =0;i<loops;i++) {
				url = url.replaceAll("productBeginIndex:(\\d+)&", "productBeginIndex:" + Integer.toString(base) + "&");
				base = base + 20;
				System.out.println(url);
			}
	
		}
	*/
			
		
		
		
						
		
	}

	
}
