package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name="PRICESTRATEGY")
public class PriceStrategy {
	/**
	 @Id
	 @GeneratedValue
	 @Column(name = "strategy_id")
     private Integer strategyId;
	 
	 @Column(name = "strategy_name")
     private String strategyName;
	 
	 @Column(name = "strategy_definition")
     private String strategyDefinition;
	 
	 @Column(name = "my_position")
     private String myPosition;
	 
	 @Column(name = "competitor_position")
     private String competitorPosition;
	 
	 @Column(name = "by_price")
	 private Double byPrice=0.0;
	 
	 @Column(name = "margin")  //% i am willing to lose in order to get a competitive price
	 private Double margin=0.0;

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getStrategyDefinition() {
		return strategyDefinition;
	}

	public void setStrategyDefinition(String strategyDefinition) {
		this.strategyDefinition = strategyDefinition;
	}

	public String getMyPosition() {
		return myPosition;
	}

	public void setMyPosition(String myPosition) {
		this.myPosition = myPosition;
	}

	public String getCompetitorPosition() {
		return competitorPosition;
	}

	public void setCompetitorPosition(String competitorPosition) {
		this.competitorPosition = competitorPosition;
	}

	public Double getByPrice() {
		return byPrice;
	}

	public void setByPrice(Double byPrice) {
		this.byPrice = byPrice;
	}

	public Double getMargin() {
		return margin;
	}

	public void setMargin(Double margin) {
		this.margin = margin;
	}
   */
	 

}
