package com.dataprice.ui.dashboard.charts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;


@org.springframework.stereotype.Component
public class PieChartBotsPerRetail implements UIComponentBuilder{

	
private class PieChart extends VerticalLayout {	
	
	private List<String> retailersUsed;
	
		public Component init() {
			
		

	        PieChartConfig config2 = new PieChartConfig();
	        config2
	            .data()
	                .labelsAsList(retailersUsed)
	                .addDataset(new PieDataset().label("Total de Bots por Retailer"))
	                .and();

	        config2.
	            options()
	                .responsive(true)
	                .title()
	                    .display(true)
	                    .text("Total de bots por Retail")
	                    .and()
	                .animation()
	                    //.animateScale(true)
	                    .animateRotate(true)
	                    .and()
	               .done();

	        List<String> labels2 = config2.data().getLabels();
	        for (Dataset<?, ?> ds : config2.data().getDatasets()) {
	            PieDataset lds = (PieDataset) ds;
	            List<Double> data = new ArrayList<>();
	            List<String> colors = new ArrayList<>();
	            for (int i = 0; i < labels2.size(); i++) {
	              //  data.add((double) (Math.round(Math.random() * 100)));
	            	data.add(dashboardService.getNumOfTasksByRetail(retailersUsed.get(i)).doubleValue()); //Same size for all labels
	                colors.add(ColorUtils.randomColor(0.7));
	            }
	            lds.backgroundColor(colors.toArray(new String[colors.size()]));
	            lds.dataAsList(data);
	        }

	        ChartJs chart2 = new ChartJs(config2);
	        chart2.setJsLoggingEnabled(true);
	        chart2.setWidth("100%");
	        
			return chart2;
		}
		

	    public PieChart load() {
	    	retailersUsed = dashboardService.getRetailersUsed();
		    return this;
	    }

	 
	}

     @Autowired
     private DashboardService dashboardService;
	
	@Override
	public Component createComponent() {
		return new PieChart().load().init();
	}

}

