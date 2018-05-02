package com.dataprice.ui.reports;

import com.dataprice.ui.products.Icon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;


public class PositionIndicator extends HorizontalLayout implements Indicator{

	
	private Label label;
    private Icon icon;
    private Position position;
    private Double competitorPrice;
    
    public PositionIndicator(Double mainPrice, Double competitorPrice) {
    	this.competitorPrice = competitorPrice;
    	label = new Label();
      	label.setContentMode(ContentMode.HTML);
      	position = new Position();
      	
      	
      	if (mainPrice<competitorPrice) {
	        	label.setValue("<font size = \"3\" color=\"black\">" + competitorPrice.toString());	
	        	icon = new Icon(VaadinIcons.ARROW_DOWN);
	        	addComponent(label);
	        	addComponent(icon);
	        	position.setCurrentPositionToCheaper();
      	}else {
      		if (mainPrice>competitorPrice) {
	        	label.setValue("<font size = \"3\" color=\"black\">" + competitorPrice.toString());
	        	icon = new Icon(VaadinIcons.ARROW_UP);
	        	icon.setRedColor();
	        	addComponent(label);
	        	addComponent(icon);
	        	position.setCurrentPositionToExpensive();
      		}else {
      			label.setValue("<font size = \"3\" color=\"black\">" + competitorPrice.toString());	
      			icon = new Icon(VaadinIcons.SCALE);
      			icon.setBlueColor();
      			addComponent(label);
      			addComponent(icon);
      			position.setCurrentPositionToEqual();
      		}
      	}
    }

	public Position getPosition() {
		return position;
	}

	
	@Override
	public String toString() {
		return competitorPrice.toString().replace(".", ",");	
	}

	
}
