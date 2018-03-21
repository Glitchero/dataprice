package com.dataprice.model.crawlers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;

public class CrawlerTester {

	
	
	public static void main(String[] args) {
		/**
		Task t = new Task();
		t.setRetail("Chedraui");
		t.setSeed("https://www.chedraui.com.mx/chedrauistorefront/chedraui/es/Departamentos/Super/Despensa/Galletas%2C-Cereales-y-Barras/c/MC010102?q=%3Arelevance%3Abrand%3ADond%C3%A9&text=&toggleView=grid");
		t.setTaskName("Galletas");
		
		Crawler c = new Chedraui();

		List<Product> prods = c.getProductsFromTask(t);
    	
		for (Product p : prods) {
			System.out.println(p);
		}
		System.out.println("tamaño" + prods.size());
		
		*/
		// 10009090 = 2 h 46 m 49 s
		// 299643635825
		Calendar today = Calendar.getInstance();
		long milliseconds = 0;
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000*60)) % 60);
		int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
		
		today.set(Calendar.HOUR_OF_DAY, hours);
		
		today.set(Calendar.MINUTE, minutes);
		
		today.set(Calendar.SECOND, seconds);
		
		Date date = today.getTime();
		
		System.out.println(date);
		
		
		
	}
	
	
	
}
