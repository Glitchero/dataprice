package com.dataprice.model.crawlers.utils;

public class Regex {

	/**
	 * Demo
	 */
	public final static String SUPLEMENTOSFITNESS_ID = "";
	public final static String SUPLEMENTOSFITNESS_NAME = "<h1 class=\"product-title entry-title\">(.*?)</h1>";
	public final static String SUPLEMENTOSFITNESS_DESCRIPTION = "class=\"product-short-description\">(.*?)<a";
	public final static String SUPLEMENTOSFITNESS_BRAND ="";
	public final static String SUPLEMENTOSFITNESS_SKU ="sku&quot;:&quot;(.*?)&quot";
	public final static String SUPLEMENTOSFITNESS_OLDPRICE = "";
	public final static String SUPLEMENTOSFITNESS_PRICE = "";
	public final static String SUPLEMENTOSFITNESS_IMAGEURL = "class=\"woocommerce-product-gallery__imag.*?><a href=\"(.*?)\">";
	public final static String SUPLEMENTOSFITNESS_IS_AVAILABLE = "";
	
	
	public final static String NUTRITIONDEPOT_ID = "<span>Código Producto:</span>(.*?)<br";
	public final static String NUTRITIONDEPOT_NAME = "<title>(.*?)</title>";
	public final static String NUTRITIONDEPOT_DESCRIPTION = "";
	public final static String NUTRITIONDEPOT_BRAND ="<span>Marca:</span> <a href=.*?>(.*?)</a>";
	public final static String NUTRITIONDEPOT_SKU ="sku&quot;:&quot;(.*?)&quot";
	public final static String NUTRITIONDEPOT_OLDPRICE = "";
	public final static String NUTRITIONDEPOT_PRICE = "<span class=\"main_price\">(.*?)</span>";
	public final static String NUTRITIONDEPOT_IMAGEURL = "<div class=\"image\"><a href=\"(.*?)\" title";
	public final static String NUTRITIONDEPOT_IS_AVAILABLE = "<span>Disponibilidad:</span>(.*?)</div>";
	
	
	public final static String MERCADOLIBRE_ID = "/(ML[A-Z]-[0-9]*)-";
	public final static String MERCADOLIBRE_NAME = "<h1 class=\"item-title__primary \">(.*?)</h1>";
	public final static String MERCADOLIBRE_DESCRIPTION = "";
	public final static String MERCADOLIBRE_BRAND ="<strong>Marca</strong>.*?<span>(.*?)</span>";
	public final static String MERCADOLIBRE_PRICE = "<span class=\"price-tag\">.*?<span class=\"price-tag-symbol\" content=\"(.*?)\">";
	public final static String MERCADOLIBRE_IMAGEURL = "<figure class=\"gallery-image-container.*?<a href=\"(.*?)\" class=\"gallery-trigger";
	public final static String MERCADOLIBRE_SKU = "<strong>Modelo</strong>.*?<span>(.*?)</span>";
	public final static String MERCADOLIBRE_SELLER ="<p class=\"title\">(.*?)</p>";
	public final static String MERCADOLIBRE_SPECIALSELLER ="<a href=\"https://perfil.mercadolibre.com.mx/(.*?)\"";
	
	
			
			
	public final static String AROME_ID = "id=\"product_code.*?\">(.*?)<";
	public final static String AROME_NAME = "<h1 class=\"ty-mainbox-title\" ><label itemprop='name'>(.*?)</label></h1>";
	public final static String AROME_DESCRIPTION = "";
	public final static String AROME_BRAND ="Marca</span>:.*?<a.*?>(.*?)</a>";
	public final static String AROME_PRICE = "<meta content=\"(.*?)\" itemprop=\"price\">";
	public final static String AROME_IMAGEURL = "data-ca-image-id.*?href=\"(.*?)\" title";
	public final static String AROME_SKU = "";

	
	public final static String SANBORNS_ID = "<p>EAN#. (.*?) </p>";
	public final static String SANBORNS_NAME = "<h1 style=\"padding:16px 8px 6px;\">(.*?)</h1>";
	public final static String SANBORNS_DESCRIPTION = "";
	public final static String SANBORNS_BRAND ="";
	public final static String SANBORNS_PRICE = "id=\"precioCambio\">(.*?)<span>";
	public final static String SANBORNS_IMAGEURL = "<div class=\"info-Product\"><div class=\"media-container\"><ul class=\"carrusel-producto\"><li><img src=\"(.*?)\".*?data-zoom-image";
	public final static String SANBORNS_SKU = "";
	public final static String SANBORNS_PRESENTATION = "Presentacion</dt><dd>(.*?)</dd>";
	
	
	public final static String AMAZON_ID = "dp/([0-9a-zA-Z]{10})";
	public final static String AMAZON_NAME = "<span id=\"productTitle\" class=\"a-size-large\">(.*?)</span>";
	public final static String AMAZON_DESCRIPTION = "";
	public final static String AMAZON_BRAND ="<a id=\"brand\".*?>(.*?)</a>";
	public final static String AMAZON_PRICE = "<span id=\"priceblock_.*?price\" class=\"a-size-medium a-color-price\">(.*?)</span>";
	public final static String AMAZON_IMAGEURL = "data-old-hires=.*?&quot;(.*?)&quot;";
	public final static String AMAZON_SKU = "";
	public final static String AMAZON_SELLER = "Vendido por <a href=.*?>(.*?)</a>";
	
	
	/**
	 * Demo ended
	 */
	
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
	
	/**
	public final static String SORIANA_ID = "ajaxRequest.*?cb=(.*?)&";
	public final static String SORIANA_NAME = "class=\"txtarticulohome\">(.*?)<";
	public final static String SORIANA_DESCRIPTION = "";
	public final static String SORIANA_BRAND ="";
	public final static String SORIANA_OLDPRICE = "class=\"precioarticulo\">(.*?)</span>";
	public final static String SORIANA_PRICE = "class=\"precioarticulohome\">(.*?)</span>";
	public final static String SORIANA_PROMOTION = "";  //Promotion is not rendered. TODO Check this!  
	public final static String SORIANA_IMAGEURL = "class=\"artDetDi1\">.*?<IMG SRC=\"(.*?)\" alt";
	*/
	
	/**
	public final static String SUPERWALMART_ID = "<span itemprop=\"productID\">(.*?)</span>";
	public final static String SUPERWALMART_NAME = "<span itemprop=\"name\">(.*?)</span>";
	public final static String SUPERWALMART_DESCRIPTION = "<meta property=\"og:description\" content=\"(.*?)\" />";
	public final static String SUPERWALMART_BRAND ="";
	public final static String SUPERWALMART_OLDPRICE = "";
	public final static String SUPERWALMART_PRICE = "<span itemprop=\"price\" content=\"(.*?)\">";
	public final static String SUPERWALMART_PROMOTION = "<div class=\"pdp-promotional-text\">(.*?)</div>";    
	public final static String SUPERWALMART_IMAGEURL = "class=\"slider-img u-photo photo\" src=\"(.*?)\" onError";
	*/
	public final static String SUPERWALMART_ID = "/([0-9]{8,15})";
	
	
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
	public final static String LIVERPOOL_DESCRIPTION = "<div class=\"dynDesc\">(.*?)</div>";
	public final static String LIVERPOOL_BRAND ="Marca</span> <span>(.*?)</span>";
	public final static String LIVERPOOL_OLDPRICE = "salePrice\":\"(.*?)\"}";
	public final static String LIVERPOOL_PRICE = "promoPrice\":\"(.*?)\",";
	public final static String LIVERPOOL_PROMOTION = "";  
	public final static String LIVERPOOL_IMAGEURL = "<meta property=\"og:image\" content=\"(.*?)\" />";
	public final static String LIVERPOOL_SKU ="Modelo</span> <span>(.*?)</span>";
	
	
	public final static String SORIANA_ID = "<span class=\"sku\">ID</span><span class=\"code\">(.*?)</span>";
	public final static String SORIANA_NAME = "<div class=\"description\">(.*?)</div>";
	public final static String SORIANA_DESCRIPTION = "";
	public final static String SORIANA_BRAND ="";
	public final static String SORIANA_OLDPRICE = "";
	public final static String SORIANA_PRICE = "<p class=\"price\">(.*?)</p>";
	public final static String SORIANA_PROMOTION = "";  
	public final static String SORIANA_IMAGEURL = "<img id=\"prodDetailsImgZoom\" src=\"(.*?)\" alt";
	
	
	
	public final static String COPPEL_ID = "<span class=\"sku\">sku: (.*?)</span>";
	public final static String COPPEL_NAME = "<h1 id=\"main_header_name\" class=\"main_header\">(.*?)</h1>";
	public final static String COPPEL_DESCRIPTION = "<div id=\"desc\">(.*?)</div>";
	public final static String COPPEL_BRAND ="";
	public final static String COPPEL_OLDPRICE = "";
	public final static String COPPEL_PRICE = "";
	public final static String COPPEL_PROMOTION = "";  
	public final static String COPPEL_IMAGEURL = "<a id=\"Zoomer\"  href=\"(.*?)\" title";

	

	
	public final static String WALMART_ID = "<meta property=\"og:image\" content=\"https://www.walmart.com.mx/images/products/img_medium/(.*?)m.jpg\">";
	public final static String WALMART_NAME = "";
	public final static String WALMART_DESCRIPTION = "description\":\"(.*?)\",";
	public final static String WALMART_BRAND ="";
	public final static String WALMART_OLDPRICE = "";
	public final static String WALMART_PRICE = "";
	public final static String WALMART_PROMOTION = "";    
	public final static String WALMART_IMAGEURL = "<meta property=\"og:image\" content=\"(.*?)\">";


	
	
	public final static String PERFUMESMEXICO_ID = "name=\"id_product\" value=\"(.*?)\" id=\"product_page_product_id\"";
	public final static String PERFUMESMEXICO_NAME = "<h1 itemprop=\"name\">(.*?)</h1>";
	public final static String PERFUMESMEXICO_DESCRIPTION = "";
	public final static String PERFUMESMEXICO_BRAND ="<span itemprop=\"brand\" class=\"editable\">(.*?)</span>";
	public final static String PERFUMESMEXICO_PRICE = "<span id=\"our_price_display\" itemprop=\"price\">(.*?)</span>";
	public final static String PERFUMESMEXICO_IMAGEURL = "<img id=\"bigpic\" itemprop=\"image\" src=\"(.*?)\" title";
	public final static String PERFUMESMEXICO_SKU = "";
	public final static String PERFUMESMEXICO_PRESENTATION = "<td>Presentación</td>.*?<td>(.*?)</td>";

    public final static String PERFUMESONLINE_ID = "<strong  itemprop=\"sku\">(.*?)</strong>";
    public final static String PERFUMESONLINE_NAME = "<h1 itemprop=\"name\">(.*?)</h1>";
    public final static String PERFUMESONLINE_DESCRIPTION = "";
    public final static String PERFUMESONLINE_BRAND = "";
    public final static String PERFUMESONLINE_PRICE = "<span class=\"regular-price\" id=\"product-price-.*?<span class=\"price\">(.*?)</span>";
    public final static String PERFUMESONLINE_IMAGEURL = "<div class=\"main-image\" >.*?href=\"(.*?)\" itemprop=\"image\">";
    public final static String PERFUMESONLINE_SKU = "";
    
    
    public final static String EXPOPERFUMES_ID = "product_id=(.*?)&";
	public final static String EXPOPERFUMES_NAME = "<span style=\"font-size:30px;line-height:35px;\">(.*?)</span>";
	public final static String EXPOPERFUMES_DESCRIPTION = "";
	public final static String EXPOPERFUMES_BRAND ="<span style=\"font-size:16px;\">.*?ml by (.*?)-.*?</span>";
	public final static String EXPOPERFUMES_PRICE = "<span class=\"productPrice\">(.*?)</span>";
	public final static String EXPOPERFUMES_IMAGEURL = "<td width=\"500\" align=\"center\">.*?<img src=\"(.*?)\" border=\"0\" />.*?</td>";
	public final static String EXPOPERFUMES_SKU = "";
	
	
	public final static String LINIO_ID = "<td itemprop=\"sku\">(.*?)</td>";
	public final static String LINIO_NAME = "<h1 class=\"col-xs-12 col-md-9 col-lg-10\">.*?<span itemprop=\"name\">(.*?)</span>.*?</h1>";
	public final static String LINIO_DESCRIPTION = "";
	public final static String LINIO_BRAND ="";
	public final static String LINIO_PRICE = "<div class=\"price price-main\">(.*?)</div>";
	public final static String LINIO_IMAGEURL = "<div id=\"image-product\" class=\"swiper-slide\">.*?<img src=\".*?\" data-lazy=\"(.*?)\" alt";
	public final static String LINIO_SKU = "";
	public final static String LINIO_SELLER = "<h4>Enviado y Vendido por:</h4>.*?<a class=\"link-lower\" href=.*?>(.*?)</a>";    
	public final static String LINIO_SPECIALSELLER = "<h4 class=\"seller-linio\">(.*?)</h4>";  
 
	
	
	public final static String OSOM_ID = "-([0-9]{6}).html";
	public final static String OSOM_NAME = "<h1 id=\"prd-name\" class=\"h3 txtLight\">(.*?)</h1>";
	public final static String OSOM_DESCRIPTION = "";
	public final static String OSOM_BRAND ="<h2 id=\"prd-brand\" class=\"h2\">(.*?)</h2>";
	public final static String OSOM_OLDPRICE = "<span class=\"strong.*?id=\"prd-price\">(.*?)</span>";
	public final static String OSOM_PRICE = "<span itemprop=\"price\" id=\"prd-special-price\" class=\"mts\">.*?<strong>(.*?)</strong>"; 
	public final static String OSOM_IMAGEURL = "<a class=\"prd-imageBox\" id=\"prdZoomBox\">.*?src=\"(.*?)\" width";
	public final static String OSOM_SKU = "";
	
	
	
	public final static String SEARS_ID = "data-flix-ean=\"(.*?)\"";
	public final static String SEARS_NAME = "<h1>(.*?)</h1>";
	public final static String SEARS_DESCRIPTION = "";
	public final static String SEARS_BRAND ="data-flix-brand=\"(.*?)\"";
	public final static String SEARS_OLDPRICE = "";
	public final static String SEARS_PRICE = "<div class=\"precio\">Precio Internet:(.*?)</div>";
	public final static String SEARS_IMAGEURL = "<a class=\"foto_enlace\" href=\"(.*?)\">";
	public final static String SEARS_SKU = "";
	
	                
	public final static String SEPHORA_NAME = "<li class=\"product\">\\s+<strong>(.*?)</strong>\\s+</li>";
    public final static String SEPHORA_BOXES= "<label for=\"super_group.*?>(.*?)</label>";
	public final static String SEPHORA_PRESENTATION= "<p class=\"reference info.*?\">(.*?)</p>";
	public final static String SEPHORA_ID = "<p class=\"reference\">REF:#(.*?)</p>";
	public final static String SEPHORA_IMAGESRC ="<img id=\"image-main\".*?src=\"(.*?)\".*?>";
//	public final static String SEPHORA_BRAND ="onclick=\"sephora.achorAnimate('#brand','html, body')\">(.*?)</a>";
	public final static String SEPHORA_BOXIMAGESRC ="<img class=\"img\".*?src=\"(.*?)\" alt";
	
	
	public final static String LAEUROPEA_ID = "<div class=\"c-product-meta detailItem\" data-code=\"(.*?)\"";
	public final static String LAEUROPEA_NAME = "<h3 class=\"c-font-bold\">(.*?)</h3>";
	public final static String LAEUROPEA_PRICE = "<div class=\"c-product-price\">(.*?)<";
	public final static String LAEUROPEA_IMAGEURL ="<div class=\"c-zoom\"><img src=\"(.*?)\"";
			
	
	
	public final static String PRISSA_ID = "name=\"id_product\" value=\"(.*?)\"";
	public final static String PRISSA_NAME ="<title>(.*?)</title>";
	public final static String PRISSA_PRICE ="<span itemprop=\"price\" content=\"(.*?)\">";
	public final static String PRISSA_IMAGEURL ="<img data-image-large-src=\"(.*?)\"";
	
	
	public final static String HOMEDEPOT_ID = "-([0-9]{6})";
	public final static String HOMEDEPOT_NAME ="http://www.homedepot.com.mx/.*?/(.*?)-[0-9]{6}";

	
	
}
