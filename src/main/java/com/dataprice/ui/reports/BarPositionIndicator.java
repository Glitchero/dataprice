package com.dataprice.ui.reports;

import java.text.DecimalFormat;

import com.dataprice.ui.products.Icon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;

public class BarPositionIndicator extends HorizontalLayout implements Indicator{
	
	private DecimalFormat df = new DecimalFormat("#0.0");
	private Label label;
	private ProgressBar progressBar;
	private Position position;
	private Double percentage;

	public BarPositionIndicator(Double mainPrice, Double competitorPrice) {
		  
		label = new Label();
		label.setContentMode(ContentMode.HTML);
	    progressBar = new ProgressBar();
	    position = new Position();
	    setSizeFull();
	    
	    Double percentage = 0.0;
        	
        	if (mainPrice<competitorPrice) {
        		percentage = ((competitorPrice-mainPrice)/mainPrice)*100.0; 
        		this.percentage = percentage;
	        	label.setValue("<font size = \"2\" color=\"black\">" + "-" + df.format(percentage) + "%");	
	        	progressBar.setValue((float)((percentage/100.0) + (percentage/100.0)*2));
	        	progressBar.setStyleName("myprogress");
	        	addComponent(progressBar);
	        	addComponent(label);
	        	setComponentAlignment(progressBar, Alignment.BOTTOM_LEFT);
	        	setComponentAlignment(label, Alignment.TOP_RIGHT);
	        	position.setCurrentPositionToCheaper();
        	}else {
        		if (mainPrice>competitorPrice) {
        		   percentage = ((mainPrice-competitorPrice)/competitorPrice)*100.0; 
        		   this.percentage = percentage;
	        	   label.setValue("<font size = \"2\" color=\"black\">" + "+" +df.format(percentage) + "%");
	        	   progressBar.setValue((float)((percentage/100.0) + (percentage/100.0)*2));
	        	   progressBar.setStyleName("myprogressbar");
	        	   addComponent(progressBar);
	        	   addComponent(label);
	        	   setComponentAlignment(progressBar, Alignment.BOTTOM_LEFT);
		           setComponentAlignment(label, Alignment.TOP_RIGHT);
		           position.setCurrentPositionToExpensive();
        		}else {
        			this.percentage = 0.0;
		        	   label.setValue("<font size = \"2\" color=\"black\">" + "+0,0%");
		        	   progressBar.setValue((float)(0.01));			
		        	   addComponent(progressBar);
		        	   addComponent(label);
		        	   setComponentAlignment(progressBar, Alignment.BOTTOM_LEFT);
			           setComponentAlignment(label, Alignment.TOP_RIGHT);
			           position.setCurrentPositionToEqual();
        		}
        	}
	}
	
	
	public Position getPosition() {
		return position;
	}

	@Override
	public String toString() {
		//return percentage.toString() + ";"+ position.getCurrentPosition();	
		return df.format(percentage) + "%";
	}

	
}
