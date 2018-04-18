package com.dataprice.ui.dashboard.charts;

import java.util.ArrayList;
import java.util.List;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.DonutChartConfig;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Tooltips.PositionMode;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.dashboard.DemoUtils;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;

@org.springframework.stereotype.Component
public class DonutChartProductsPerRetail implements UIComponentBuilder{

	
	private class DonutChart extends VerticalLayout {
	
		
		public Component init() {
			
	        DonutChartConfig config = new DonutChartConfig();
	        config
	            .data()
	                .labels("Red", "Green", "Yellow", "Grey", "Dark Grey")
	                .addDataset(new PieDataset().label("Dataset 1"))
	                .and();

	        config.
	            options()
	                .rotation(Math.PI)
	                .circumference(Math.PI)
	                .responsive(true)
	                .title()
	                    .display(true)
	                    .text("Total de productos por Retail")
	                    .and()
	                .animation()
	                    .animateScale(false)
	                    .animateRotate(true)
	                    .and()
	               .done();

	        String[] colors = new String[] {"#F7464A", "#46BFBD", "#FDB45C", "#949FB1", "#4D5360"};

	        List<String> labels = config.data().getLabels();
	        for (Dataset<?, ?> ds : config.data().getDatasets()) {
	            PieDataset lds = (PieDataset) ds;
	            lds.backgroundColor(colors);
	            List<Double> data = new ArrayList<>();
	            for (int i = 0; i < labels.size(); i++) {
	                data.add((double) (Math.round(Math.random() * 100)));
	            }
	            lds.dataAsList(data);
	        }

	        ChartJs chart = new ChartJs(config);
	        chart.setJsLoggingEnabled(true);
	        chart.addClickListener((a,b) -> DemoUtils.notification(a, b, config.data().getDatasets().get(a)));
	        chart.setWidth("100%");
			return chart;
		}
		

	    public DonutChart load() {
		    // TODO Auto-generated method stub
		return this;
	    }
   

	}
	
	@Override
	public Component createComponent() {
		return new DonutChart().load().init();
	}

}