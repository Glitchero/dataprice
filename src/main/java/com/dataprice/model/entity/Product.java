package com.dataprice.model.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.*;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.bridge.builtin.ByteBridge;


@Indexed
@AnalyzerDef(name = "customanalyzer",
tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
filters = {
  @TokenFilterDef(factory = LowerCaseFilterFactory.class),
  @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
    @Parameter(name = "language", value = "Spanish")
  })
})
@Entity
@Table(name="PRODUCT")
public class Product {
	
	@Id
	private String productKey="";
	 
	@Column(name = "product_id")
	private String productId = "";
	
	@Field(index=Index.YES, analyze=Analyze.NO)
	@Column(name = "seller")
	private String seller = "";
	
	@Field(index=Index.YES, analyze=Analyze.YES)
	@Column(name = "name")
	@Analyzer(definition = "customanalyzer")
	private String name = "";
	
	@Lob 
	@Column(name = "description",length = 512)
	private String description="";
	
	@Column(name = "price")
	private Double price=0.0;

	@Column(name = "image_url")
	private String imageUrl="";
	
	@Column(name = "product_url",length = 512)
	private String productUrl="";
	
	@Field(index=Index.YES, analyze=Analyze.NO)
	@Column(name = "sku")
	private String sku="";
	
	@Field(index=Index.YES, analyze=Analyze.NO)
	@Column(name = "upc")
	private String upc="";
	
	@Field(index=Index.YES, analyze=Analyze.NO)
	@Column(name = "brand")
	private String brand="";
	
	@Column(name = "category")
	private String category="";
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "task_id")
	private Task task;

	@Column(name = "checked")
	private boolean checked = false;
	
	@Column(name = "affiliate")
	private String affiliate = null;
	
	//@Temporal(TemporalType.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_day", nullable = false)
	private Date updateDay = new Date();

	public Product() {
		//Default Constructor
	}
		
	public Product(String productKey, String productId,String seller,Task task,String name,String description, Double price, String imageUrl, String productUrl,String sku, String upc, String brand, String category) {
		this.productKey = productKey;
		this.productId = productId;
		this.seller = seller;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
		this.task = task;
		this.sku = sku;
		this.upc = upc;
		this.brand = brand;
		this.category = category;
		this.updateDay = new Date();
	}

	
	public String getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(String affiliate) {
		this.affiliate = affiliate;
	}

	public Date getUpdateDay() {
		return updateDay;
	}

	public void setUpdateDay(Date updateDay) {
		this.updateDay = updateDay;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getProductKey() {
		return productKey;
	}

	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}


	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	
	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}
   
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getProductUrl() {
		return productUrl;
	}
	
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	
	public String getSku() {
		return sku;
	}
	
	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	@Override
	public String toString() {
		return productKey + "-" + name + "-" + price + "-" +brand + "-" + imageUrl;	
	}
	
	
	//In case we use a map!! Do we need to override hashcode?
	 @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result
	                + ((productKey == null) ? 0 : productKey.hashCode());
	        return result;
	    }
	 
	 //Needed for removing object from list!.
	 
	 @Override
	    public boolean equals(final Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        final Product other = (Product) obj;
	        if (productKey == null) {
	            if (other.productKey != null)
	                return false;
	        } else if (!productKey.equals(other.productKey))
	            return false;
	        return true;
	    }
	
	
}
