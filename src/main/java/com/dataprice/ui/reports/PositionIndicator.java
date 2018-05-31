package com.dataprice.ui.reports;

import java.text.DecimalFormat;

import com.dataprice.ui.products.Icon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;


public class PositionIndicator extends HorizontalLayout implements Indicator{

	
	private Label label;
	private Link link;
    private Icon icon;
    private Position position;
    private Double competitorPrice;
    private DecimalFormat df = new DecimalFormat("####,###,###.00");
    
    public PositionIndicator(Double mainPrice, Double competitorPrice, String url) {
    	this.competitorPrice = competitorPrice;
    	label = new Label();
    	
      	label.setContentMode(ContentMode.HTML);
      	position = new Position();
      	link = new Link();
      	ExternalResource externalResourceLink = new ExternalResource(url);
      	link.setCaption("chécame");
      	link.setResource(externalResourceLink);
      	link.setTargetName("_blank");
      	if (mainPrice<competitorPrice) {
	        	label.setValue("<font size = \"3\" color=\"black\">" + df.format(competitorPrice));	
	        	icon = new Icon(VaadinIcons.ARROW_DOWN);
	        	addComponent(label);
	        	addComponent(icon);
	        	addComponent(link);
	        	position.setCurrentPositionToCheaper();
      	}else {
      		if (mainPrice>competitorPrice) {
	        	label.setValue("<font size = \"3\" color=\"black\">" + df.format(competitorPrice));
	        	icon = new Icon(VaadinIcons.ARROW_UP);
	        	icon.setRedColor();
	        	addComponent(label);
	        	addComponent(icon);
	        	addComponent(link);
	        	position.setCurrentPositionToExpensive();
      		}else {
      			label.setValue("<font size = \"3\" color=\"black\">" + df.format(competitorPrice));	
      			icon = new Icon(VaadinIcons.SCALE);
      			icon.setBlueColor();
      			addComponent(label);
      			addComponent(icon);
      			addComponent(link);
      			position.setCurrentPositionToEqual();
      		}
      	}
    }

	public Position getPosition() {
		return position;
	}

	
	@Override
	public String toString() {
		/**
		 * One way to solve this: Create a render for a double
		 * Another option, could be putting some extra columns renders as numbers, we can hide them to the user in the browser.
		 */
		//return competitorPrice.toString().replace(".", ",") + ";"+ position.getCurrentPosition();	
		return competitorPrice.toString().replace(".", ",");
		//return df.format(competitorPrice) + ";"+ position.getCurrentPosition();
	}

	
}
