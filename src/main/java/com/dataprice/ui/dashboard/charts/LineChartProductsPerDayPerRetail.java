package com.dataprice.ui.dashboard.charts;


import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class LineChartProductsPerDayPerRetail implements UIComponentBuilder{

	
	private class LineChart extends VerticalLayout {
	
		
		public LineChart init() {
			// TODO Auto-generated method stub
			return this;
		}
		

	    public LineChart load() {
		    // TODO Auto-generated method stub
		return this;
	    }


	    public Component layout() {
		    // TODO Auto-generated method stub
		return null;
	    }
	    

	}
	
	@Override
	public Component createComponent() {
		return new LineChart().load().init().layout();
	}

}
