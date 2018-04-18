package com.dataprice.ui.dashboard.charts;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Tooltips.PositionMode;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.dashboard.DemoUtils;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;

@org.springframework.stereotype.Component
public class LineChartProductsPerDay implements UIComponentBuilder{

	
	private class LineChart extends VerticalLayout {
	
		
		public Component init() {
			
		//	("01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17")
	        LineChartConfig config = new LineChartConfig();
	        config.data()
	       //     .labels("January", "February", "March", "April", "May", "June", "July")
	        .labels("01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17", "01/01/17","01/01/17","01/01/17")
	            .addDataset(new LineDataset().label("Dataset")
	                .borderColor(DemoUtils.RGB_RED)
	                .backgroundColor(DemoUtils.RGB_RED)
	                .data(10d, 30d, 46d, 2d, 8d, 50d, 0d)
	                .fill(false))
	            .and()
	            .options()
	                .responsive(true)
	                .title()
	                    .display(true)
	                    .text("Total de productos descargados en el último mes")
	                .and()
	                    .tooltips()
	                        .position(PositionMode.NEAREST)
	                        .mode(InteractionMode.INDEX)
	                        .intersect(false)
	                        .yPadding(10)
	                        .xPadding(10)
	                        .caretSize(8)
	                        .caretPadding(10)
	                        .backgroundColor("rgba(72, 241, 12, 1)")
	                        .titleFontColor(DemoUtils.RGB_BLACK)
	                        .bodyFontColor(DemoUtils.RGB_BLACK)
	                        .borderColor("rgba(0,0,0,1)")
	                        .borderWidth(4)
	                    .and()
	            .done();

	        ChartJs chart = new ChartJs(config);
	        chart.addStyleName("chart-container");
	        chart.setJsLoggingEnabled(true);
	        chart.setWidth("100%");
	        
			return chart;
		}
		

	    public LineChart load() {
		    // TODO Auto-generated method stub
		return this;
	    }
   

	}
	
	@Override
	public Component createComponent() {
		return new LineChart().load().init();
	}

}