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

import org.slf4j.Logger;


public class CrawlerTester {

		
	public static void main(String[] args) {

		/**
		Task task = new Task();
		task.setSeed("https://www.sephora.com.br/perfumes?cat=37");
		
		task.setTaskName("CABALLERO");
		
		Catalogue crawler = new Catalogue();
		List<CrawlInfo> productsInfo = crawler.getUrlsFromTask(task);
	
		System.out.println("Tamaño total de productos base descargados: " + productsInfo.size());
		int con = 1;
		for (CrawlInfo crawlInfo : productsInfo) {
			System.out.println("------------------------      " + con );
			List<Product> products = crawler.parseProductsFromUrl(crawlInfo, task);
			for (int i = 0; i<products.size();i++){	 
				 System.out.println(products.get(i));
	      	}
			con++;
		}
	    */
		
		
		Task task = new Task();
		task.setSeed("https://www.linio.com.mx/s/perfume-nation");
		
		task.setTaskName("perfumes");
		
		Crawler crawler = new Linio();
		List<CrawlInfo> productsInfo = crawler.getUrlsFromTask(task);
	
		for (CrawlInfo crawlInfo : productsInfo) {
		    Product p = crawler.parseProductFromURL(crawlInfo, task);
			System.out.println(p);
		}
		
		
	}

	
}
