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
import com.dataprice.service.crawltask.CrawlTaskServiceImpl;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CrawlerTester {

		
	public static void main(String[] args) {

		Task task = new Task();
		task.setSeed("https://www.amazon.com.mx/s/ref=nb_sb_noss_1?__mk_es_MX=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Daps&field-keywords=perfumes+hombre&lo=fashion&rh=i%3Aaps%2Ck%3Aperfumes+hombre");
		
		task.setTaskName("perfumes");
		
		Crawler crawler = new Amazon();
		List<CrawlInfo> productsInfo = crawler.getUrlsFromTask(task);
	
		for (CrawlInfo crawlInfo : productsInfo) {
		    Product p = crawler.parseProductFromURL(crawlInfo, task);
			System.out.println(p);
		}

		
		//-----------------------------------------------------------------------------------------
		/**
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 

		WebClient webClient = new WebClient();
	//	webClient.getOptions().setJavaScriptEnabled(true);
		  String pageAsXml = "";
		  HtmlPage page;
		try {
			page = webClient.getPage("https://www.walmart.com.mx/Linea-Blanca/Lavadoras-y-Secadoras/Lavadoras/Lavadora-Mabe-18-Kg-Blanca_00075763896986");
	        webClient.setJavaScriptTimeout(10000);
	        webClient.waitForBackgroundJavaScript(10000);
	        //just wait
	        try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pageAsXml = page.asXml();
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PrintWriter out = new PrintWriter("filename.txt");
			out.println(pageAsXml);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(pageAsXml);
		webClient.close();
         */
	
	
	
	
	/** EXTRA STUFF
	Calendar today = Calendar.getInstance();
	long milliseconds = 1140784;
	int seconds = (int) (milliseconds / 1000) % 60 ;
	int minutes = (int) ((milliseconds / (1000*60)) % 60);
	int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
	
	today.set(Calendar.HOUR_OF_DAY, hours);
	
	today.set(Calendar.MINUTE, minutes);
	
	today.set(Calendar.SECOND, seconds);
	
	Date date = today.getTime();
	
	System.out.println(date);
	
	String value = "5831";
	System.out.println(Double.parseDouble(value));
	Locale currentLocale = Locale.getDefault();
	DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
	otherSymbols.setDecimalSeparator('.'); 
	NumberFormat df = new DecimalFormat("#.##",otherSymbols);
	System.out.print(Double.valueOf(df.format(Double.parseDouble(value))));
	*/
	
	}
}
