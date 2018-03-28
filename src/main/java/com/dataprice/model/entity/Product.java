package com.dataprice.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
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
	 
	@Field
	@Column(name = "product_id")
	private String productId;
	
	@Field
	@Column(name = "retail")
	private String retail;
	
	@Field
	@Column(name = "name")
	@Analyzer(definition = "customanalyzer")
	private String name;
	
	@Column(name = "precio")
	private Double precio;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "product_url")
	private String productUrl;
	
	//private String brandHelper;
	
	//private String skuHelper;
	
	//private String status;
	
	@Column(name = "pid")
	private String pid;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "gender_id")
	private Gender gender;
		

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "subcategory_id")
	private Subcategory subcategory;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "task_id")
	private Task task;
	


	public Product() {
		
	}
	
	
	public Product(String productKey, String productId,String retail,Task task,String name, Double precio, String imageUrl, String productUrl) {
		this.productKey = productKey;
		this.productId = productId;
		this.retail = retail;
		this.name = name;
		this.precio = precio;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
		this.task = task;
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

	
	public String getRetail() {
		return retail;
	}

	public void setRetail(String retail) {
		this.retail = retail;
	}
   
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getPrecio() {
		return precio;
	}
	
	public void setPrecio(Double precio) {
		this.precio = precio;
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
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}
	
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	
	public Task getTask() {
		return task;
	}


	public void setTask(Task task) {
		this.task = task;
	}
	
	@Override
	public String toString() {
		return productKey + "-" + "name" + "-" + precio;	
	}
	
}
