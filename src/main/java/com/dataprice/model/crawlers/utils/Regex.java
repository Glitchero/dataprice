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
	
	
	/**
	 * Supermercados
	 */
	
	public final static String CHEDRAUI_ID = "class=\"sku\">UPC</span><span class=\"code\">(.*?)</span>";
	public final static String CHEDRAUI_NAME = "class=\"active\">(.*?)</li>";
	public final static String CHEDRAUI_DESCRIPTION = "<div class=\"description\">(.*?)</div>";
	public final static String CHEDRAUI_BRAND ="class=\"product-details_attribute_value\">(.*?)</span>";
	public final static String CHEDRAUI_OLDPRICE = "class=\"chedstrikethrough\">(.*?)</span>";
	public final static String CHEDRAUI_PRICE = "class=\"price price-colour-final-pdp\">(.*?)</p>";
	public final static String CHEDRAUI_PROMOTION = "<p class=\"promotion\">(.*?)</p>";    
	public final static String CHEDRAUI_IMAGEURL = "class=\"item\"> <img class=\"lazyOwl\" data-src=\"(.*?)\" alt";
	
	
	public final static String SORIANA_ID = "ajaxRequest.*?cb=(.*?)&";
	public final static String SORIANA_NAME = "class=\"txtarticulohome\">(.*?)<";
	public final static String SORIANA_DESCRIPTION = "";
	public final static String SORIANA_BRAND ="";
	public final static String SORIANA_OLDPRICE = "class=\"precioarticulo\">(.*?)</span>";
	public final static String SORIANA_PRICE = "class=\"precioarticulohome\">(.*?)</span>";
	public final static String SORIANA_PROMOTION = "";  //Promotion is not rendered. TODO Check this!  
	public final static String SORIANA_IMAGEURL = "class=\"artDetDi1\">.*?<IMG SRC=\"(.*?)\" alt";
	
	
	
	public final static String WALMART_ID = "<span itemprop=\"productID\">(.*?)</span>";
	public final static String WALMART_NAME = "<span itemprop=\"name\">(.*?)</span>";
	public final static String WALMART_DESCRIPTION = "<meta property=\"og:description\" content=\"(.*?)\" />";
	public final static String WALMART_BRAND ="";
	public final static String WALMART_OLDPRICE = "";
	public final static String WALMART_PRICE = "<span itemprop=\"price\" content=\"(.*?)\">";
	public final static String WALMART_PROMOTION = "<div class=\"pdp-promotional-text\">(.*?)</div>";    
	public final static String WALMART_IMAGEURL = "class=\"slider-img u-photo photo\" src=\"(.*?)\" onError";
	
	
	
	public final static String LACOMER_ID = "id=\"artean\" value=\"(.*?)\"/>";
	public final static String LACOMER_NAME = "<span class=\"txt-product-name\">(.*?)</span.*?br>"; 
	public final static String LACOMER_DESCRIPTION = "<p class=\"deta_descripcion\" itemprop=\"description\">(.*?)</p>";
	public final static String LACOMER_BRAND = "<span class=\"txt-product-name\">(.*?)</span>";
	public final static String LACOMER_OLDPRICE = "<span class=\"txt-line-through\">(.*?)</span>";
	public final static String LACOMER_PRICE = "<span class=\"txt-whitout-line\">(.*?)</span>";
	public final static String LACOMER_PROMOTION = "<span class=\"precio_nxm\">(.*?)</span>";    
	public final static String LACOMER_IMAGEURL = "<img id=\"target-src\" class=\"img-product-detail centerImg\" style=\"width: auto\".*?src=\"(.*?)\".*?data";
	
	
	
	public final static String SUPERAMA_ID = "<span class=\"\">UPC:(.*?)</span>";
	public final static String SUPERAMA_NAME = "id=\"nombreProductoDetalle\" itemprop=\"name\">(.*?)</h2>";
	public final static String SUPERAMA_DESCRIPTION = "<div class=\"detail-description-content\">(.*?)</div>";
	public final static String SUPERAMA_BRAND ="";
	public final static String SUPERAMA_OLDPRICE = "<span class=\"detail-span-product__rollback\">(.*?)</span>";
	public final static String SUPERAMA_PRICE = "<div class=\"detail-description-price\">.*?<span>(.*?)</span>";
	public final static String SUPERAMA_PROMOTION = "<span class=\"icon-super_ahorros\"></span>(.*?)</a>";  
	public final static String SUPERAMA_IMAGEURL = "<img id=\"zoomSuperamaImage\" src=\"(.*?)\" alt";
	
	
	public final static String LIVERPOOL_ID = "<span id=\"sku\">Código de Producto.: (.*?)</span>";
	public final static String LIVERPOOL_NAME = "<h1>(.*?)</h1>";
	public final static String LIVERPOOL_DESCRIPTION = "";
	public final static String LIVERPOOL_BRAND ="";
	public final static String LIVERPOOL_OLDPRICE = "";
	public final static String LIVERPOOL_PRICE = "<span itemprop=\"price\".*?>(.*?)</span>";
	public final static String LIVERPOOL_PROMOTION = "";  
	public final static String LIVERPOOL_IMAGEURL = "<meta property=\"og:image\" content=\"(.*?)\" />";
	
}
