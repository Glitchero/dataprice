package com.dataprice.model.crawlers.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;



public class PageFetcher {

	private static ConcurrentHashMap<String, PageFetcher> instances = new ConcurrentHashMap<String, PageFetcher>();
	private static String USER_AGENT="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
	private static String REFERRER="http://www.google.com";
    protected static long lastFetchTime = 0;
    protected final Object mutex = new Object();


    
    private PageFetcher() {
 
    }
    
    
	public static PageFetcher getInstance(String which) 
	{
		PageFetcher result = instances.get(which);

	    if (result == null) 
	    {
	    	PageFetcher m = new PageFetcher();
	        result = instances.putIfAbsent(which, m);

	        if (result == null)
	            result = m;
	    }

	    return result;
	}
	
	
	
	public FetchResults getURLContent(String urlStr, int politenessDelay) {
		
		FetchResults fetchResults = new FetchResults();
		try {
			//CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			URL url = new URL(urlStr);
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("User-Agent", USER_AGENT);
			HttpURLConnection.setFollowRedirects(true); //In case we have 300 code, we follow the redirect!!!.
			connection.setConnectTimeout(12000);
			connection.setReadTimeout(12000);

			// Applying Politeness delay
            synchronized (mutex) {
                long now = (new Date()).getTime();
                if ((now - lastFetchTime) < politenessDelay) {
                     try {
						   Thread.sleep(politenessDelay - (now - lastFetchTime));
					     } catch (InterruptedException e) {
						   // TODO Auto-generated catch block
						   //e.printStackTrace();
						   //System.out.println("....run()::Extraction::Scraping::isInterrupted");
						   return null;
					  }
                }
                lastFetchTime = (new Date()).getTime();
            }
			connection.connect();
			int code = connection.getResponseCode();
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),Charset.forName("UTF-8")));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
			    sb.append(line);
			}
			fetchResults.setUrl(urlStr);
			fetchResults.setContent(sb.toString());
			fetchResults.setServercode(code);
			
			return fetchResults;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);  
			System.out.println("El servido tiene algún problema con la peticiÃ³n, de repente pasa!!");
			return new FetchResults();
		}
	}
	
	
 
  
	
	
}