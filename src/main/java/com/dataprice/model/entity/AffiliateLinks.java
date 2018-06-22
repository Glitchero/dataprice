package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AFFILIATELINKS")
public class AffiliateLinks {
	
	
	    //@GeneratedValue
		@Id
		@Column(name = "amazon_id")
		private String amazonId = "";
		
		@Column(name = "link")
		private String link = null;

		
		public AffiliateLinks(String amazonId, String link) {
			this.amazonId = amazonId;
			this.link = link;
		}

		public String getAmazonId() {
			return amazonId;
		}

		public void setAmazonId(String amazonId) {
			this.amazonId = amazonId;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}
		
		
}
