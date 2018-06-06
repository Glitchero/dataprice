package com.dataprice.model.crawlers.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;

import javax.net.ssl.SSLContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import java.security.cert.X509Certificate;

import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.message.BasicHeader;
import org.apache.http.impl.client.HttpClientBuilder;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;


import org.apache.http.HttpHost;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

public class PageFetcher {

	private static ConcurrentHashMap<String, PageFetcher> instances = new ConcurrentHashMap<String, PageFetcher>();
	private static String USER_AGENT="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
	private static String REFERRER="http://www.google.com";
    protected static long lastFetchTime = 0;
    protected final Object mutex = new Object();
    protected PoolingHttpClientConnectionManager connectionManager;
    protected CloseableHttpClient httpClient;
    //DNS RESOLVER 
    private DnsResolver dnsResolver = new SystemDefaultDnsResolver();
    
    private Collection<BasicHeader> defaultHeaders = new HashSet<BasicHeader>();

    
    private PageFetcher() {
    	
    	//Constructor
    	RequestConfig requestConfig = RequestConfig.custom()
                .setExpectContinueEnabled(false)
             //   .setCookieSpec(CookieSpecs.STANDARD)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setRedirectsEnabled(false)
                .setSocketTimeout(20000)
                .setConnectTimeout(30000)
                .build();
    	
    	RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
        connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
        
            try { 
                // By always trusting the ssl certificate
                SSLContext sslContext =
                        SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                            @Override
                            public boolean isTrusted(final X509Certificate[] chain, String authType) {
                                return true;
                            }
                        }).build();
                SSLConnectionSocketFactory sslsf =
                        new SniSSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                connRegistryBuilder.register("https", sslsf);
            } catch (Exception e) {
                System.out.println("Exception thrown while trying to register https");
                System.out.println(e);
            }
            
            Registry<ConnectionSocketFactory> connRegistry = connRegistryBuilder.build();
            connectionManager =
                    new SniPoolingHttpClientConnectionManager(connRegistry, dnsResolver);
            connectionManager.setMaxTotal(100);
            connectionManager.setDefaultMaxPerRoute(100);

            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
           
            clientBuilder.setDefaultRequestConfig(requestConfig);
            clientBuilder.setConnectionManager(connectionManager);
            clientBuilder.setUserAgent(USER_AGENT);
            clientBuilder.setDefaultHeaders(defaultHeaders);
        
            Map<AuthScope, Credentials> credentialsMap = new HashMap<>();
            if (Configuration.ProxyHost != null) {
                if (Configuration.ProxyUsername != null) {
                    AuthScope authScope = new AuthScope(Configuration.ProxyHost, Configuration.ProxyPort);
                    Credentials credentials = new UsernamePasswordCredentials(Configuration.ProxyUsername,Configuration.ProxyPassword);
                    credentialsMap.put(authScope, credentials);
                }

                HttpHost proxy = new HttpHost(Configuration.ProxyHost, Configuration.ProxyPort);
                clientBuilder.setProxy(proxy);
                System.out.println("Working through Proxy: " + proxy.getHostName());
            }

        
            if (!credentialsMap.isEmpty()) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsMap.forEach((AuthScope authscope, Credentials credentials) -> {
                    credentialsProvider.setCredentials(authscope, credentials);
                });
                clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                clientBuilder.addInterceptorFirst(new BasicAuthHttpRequestInterceptor());
            }
            
            
            httpClient = clientBuilder.build();
          
      
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
	
	
	
	public FetchResults getURLContent(String urlStr) {
		
		FetchResults fetchResults = new FetchResults();
		try {
			URL url = new URL(urlStr);
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("User-Agent", USER_AGENT);
			HttpURLConnection.setFollowRedirects(false); 
			
			// Applying Politeness delay
            synchronized (mutex) {
                long now = (new Date()).getTime();
                if ((now - lastFetchTime) < Configuration.POLITENESSDELAY) {
                     try {
						   Thread.sleep(Configuration.POLITENESSDELAY - (now - lastFetchTime));
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
			System.out.println("El servido tiene algún problema con la peticiÃ³n, de repente pasa!!");
			return new FetchResults();
		}
	}
	
	
   public FetchResults getURLContentWithProxy(String urlStr) {
		
		FetchResults fetchResults = new FetchResults();
		try {

		    HttpUriRequest request = newHttpUriRequest(urlStr);
					
			// Applying Politeness delay
            synchronized (mutex) {
                long now = (new Date()).getTime();
                if ((now - lastFetchTime) < Configuration.POLITENESSDELAY) {
                     try {
						   Thread.sleep(Configuration.POLITENESSDELAY - (now - lastFetchTime));
					     } catch (InterruptedException e) {
						   // TODO Auto-generated catch block
						   //e.printStackTrace();
						   //System.out.println("....run()::Extraction::Scraping::isInterrupted");
						   return null;
					  }
                }
                lastFetchTime = (new Date()).getTime();
            }

            
            CloseableHttpResponse response = httpClient.execute(request);
			int code = response.getStatusLine().getStatusCode();
			BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),Charset.forName("UTF-8")));
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
			return new FetchResults();
		}
	}


   /**
    * Creates a new HttpUriRequest for the given url. The default is to create a HttpGet without
    * any further configuration. Subclasses may override this method and provide their own logic.
    *
    * @param url the url to be fetched
    * @return the HttpUriRequest for the given url
    */
   protected HttpUriRequest newHttpUriRequest(String url) {
       return new HttpGet(url);
   }
	
	
}