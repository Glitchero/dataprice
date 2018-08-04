package com.dataprice.ui.reports;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.DefaultScale;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.dashboard.DemoUtils;
import com.dataprice.ui.products.Icon;
import com.dataprice.ui.reports.ProductsComparator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class PriceDistributionLayoutFactory {
	
	
	private class StackedChart extends VerticalLayout {

		private ReportSettings reportSettings;
		
		public StackedChart(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}
		
		
		private List<String> competitorsUsed;
		private List<String> competitorsUsedWithTotal;
		private List<Product> products;
		private List<Double> cheaperList;
		private List<Double> expensiveList;
		private List<Double> equalList;
		List<Double> valueList;
		
		private Settings settings;
		
		public Component init() {
			 BarChartConfig config = new BarChartConfig();
		        config.data()
		            .labelsAsList(competitorsUsedWithTotal)
		            .addDataset(new BarDataset().label("Soy más barato").backgroundColor(DemoUtils.RGB_GREEN))
		            .addDataset(new BarDataset().label("Mismos precios").backgroundColor(DemoUtils.RGB_BLUE))
		            .addDataset(new BarDataset().label("Soy más caro").backgroundColor(DemoUtils.RGB_RED))
		            .and()
		        .options()
		            .responsive(true)
		            .title()
		                .display(true)
		                .text("Distribución de los Precios de la Competencia" + " (" + competitorsUsedWithTotal.size() +" competidores)")
		                .and()
		            .tooltips()
		                .mode(InteractionMode.INDEX)
		                .intersect(false)
		                .and()
		            .scales()
		            .add(Axis.X, new DefaultScale()
		                    .stacked(true).display(true)) //Estaba en false
		            .add(Axis.Y, new DefaultScale()
		                    .stacked(true))
		            .and()
		            .done();
		        
		        // add random data for demo
		        List<String> labels = config.data().getLabels();
		        int con = 0;
		        
		        //Here i can use some flags: ban = 1, ban = 2 y ban = 3.
		        System.out.println("Hola paso aqui");
		        for (Dataset<?, ?> ds : config.data().getDatasets()) {
		            BarDataset lds = (BarDataset) ds;
		            List<Double> data = new ArrayList<>();
		            for (int i = 0; i < labels.size(); i++) {		            	
		            	data.add(valueList.get(con));
		            	con++;
		            }
		            lds.dataAsList(data);
		        }

		        ChartJs chart = new ChartJs(config);
		        chart.addClickListener((a, b) -> {
		            BarDataset dataset = (BarDataset) config.data().getDatasets().get(a);
		            DemoUtils.notification(a, b, dataset);
		        });
		        chart.setJsLoggingEnabled(true);
		        chart.setWidth("100%");
		        return chart;
		}	
		
		
		/**
		 * This method can be reduced!! Check later
		 * @return
		 */
		public StackedChart load() {
			Integer cheaper = 0;
			Integer equal = 0;
			Integer expensive = 0;
			
			User user = userServiceImpl.getUserByUsername("admin");
			settings = user.getSettings();
			
			LocalDate today = LocalDate.now();
		    LocalDate yesterday = today.minus(Period.ofDays(settings.getLastUpdateInDays()));
			java.util.Date lastUpdate = java.sql.Date.valueOf(yesterday);
			System.out.println("Ultima actualización" + lastUpdate);
			competitorsUsedWithTotal = new ArrayList<String>();
			cheaperList = new LinkedList<Double>();
			expensiveList = new LinkedList<Double>();
			equalList = new LinkedList<Double>();
			
			valueList = new LinkedList<Double>();
		
		//	competitorsUsed = dashboardService.getCompetitorsBySku(settings.getMainSeller());
		//	Set<String> competitorsUsedSet = new HashSet<String>(competitorsUsed);
			
		//	Set<String> competitorsUsedSet = reportSettings.getCompetitors();
			if (reportSettings.getCompetitors().size()!=0) {	
				products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), lastUpdate,reportSettings.getCompetitors());  //Change function for string.

			if (products.size()!=0) {
			     for (String competitorUsed : reportSettings.getCompetitors()) {
					
					for (Product p: products) {
						List<Product> productCompetition = reportsService.getProductsFromSellerNameAndSku(competitorUsed, p.getSku(),lastUpdate);
					   
				        if (productCompetition.size()!=0) {
				        	
				        	//Order product list
				        	ProductsComparator comparator = new ProductsComparator();
				        	Collections.sort(productCompetition, comparator);
				        	
				        	if (p.getPrice()<productCompetition.get(0).getPrice()) {
				        		cheaper++;	
				        	}else {
				        		if (p.getPrice()>productCompetition.get(0).getPrice()) {
				        			expensive++;
				        		}else {
				        			equal++;
				        		}
				        	}
				        }

					}
						
					
					
					Integer total = cheaper+expensive+equal;
					if (total!=0) {

					    competitorsUsedWithTotal.add(competitorUsed + " (" + total + " productos)");
					
					    Locale currentLocale = Locale.getDefault();
					    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
					    otherSymbols.setDecimalSeparator('.'); 
					    NumberFormat df = new DecimalFormat("#.##",otherSymbols);
					
				    	cheaperList.add(Double.valueOf(df.format(cheaper/(double) total * 100)));				
					    expensiveList.add(Double.valueOf(df.format(expensive/(double) total * 100)));				
					    equalList.add(Double.valueOf(df.format(equal/(double) total * 100)));

					    cheaper = 0;
					    expensive = 0;
					    equal = 0;
					    total = 0;
					}
				}
				
				
				valueList.addAll(cheaperList);
				valueList.addAll(equalList);
				valueList.addAll(expensiveList);
				}	
				}
			
					
			return this;
		}
		

	}
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	@Autowired 
	private ReportsService reportsService;
	
	@Autowired
    private DashboardService dashboardService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	
	public Component createComponent(ReportSettings reportSettings) {
		return new StackedChart(reportSettings).load().init();
	}

}
