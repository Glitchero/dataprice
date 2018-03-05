package com.dataprice.model.crawlers.utils;

public class Regex {

	public final static String SUPLEMENTOSFITNESS_ID = "data-product_id=\"(.*?)\"";
	public final static String SUPLEMENTOSFITNESS_NAME = "<h1 class=\"product-title entry-title\">(.*?)</h1>";
	public final static String SUPLEMENTOSFITNESS_DESCRIPTION = "class=\"product-short-description\">(.*?)<a";
	public final static String SUPLEMENTOSFITNESS_BRAND ="";
	public final static String SUPLEMENTOSFITNESS_SKU_RAW ="sku&quot;:&quot;(.*?)&quot";
	public final static String SUPLEMENTOSFITNESS_OLDPRICE = "";
	public final static String SUPLEMENTOSFITNESS_PRICE = "display_price&quot;:(.*?),&quot";
	public final static String SUPLEMENTOSFITNESS_IMAGEURL = "class=\"first slide woocommerce-product-gallery__image\"><a href=\"(.*?)\">";
	public final static String SUPLEMENTOSFITNESS_IS_AVAILABLE = "";
	
	
	public final static String NUTRITIONDEPOT_ID = "<span>Código Producto:</span>(.*?)<br";
	public final static String NUTRITIONDEPOT_NAME = "<title>(.*?)</title>";
	public final static String NUTRITIONDEPOT_DESCRIPTION = "";
	public final static String NUTRITIONDEPOT_BRAND ="<span>Marca:</span> <a href=.*?>(.*?)</a>";
	public final static String NUTRITIONDEPOT_SKU_RAW ="sku&quot;:&quot;(.*?)&quot";
	public final static String NUTRITIONDEPOT_OLDPRICE = "";
	public final static String NUTRITIONDEPOT_PRICE = "<span class=\"main_price\">(.*?)</span>";
	public final static String NUTRITIONDEPOT_IMAGEURL = "<div class=\"image\"><a href=\"(.*?)\" title";
	public final static String NUTRITIONDEPOT_IS_AVAILABLE = "<span>Disponibilidad:</span>(.*?)</div>";
	
}
