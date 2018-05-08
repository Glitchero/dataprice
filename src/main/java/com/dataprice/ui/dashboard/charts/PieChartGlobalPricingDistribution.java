package com.dataprice.ui.dashboard.charts;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.dashboard.DemoUtils;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;


@org.springframework.stereotype.Component
public class PieChartGlobalPricingDistribution implements UIComponentBuilder{

	
private class PieChart extends VerticalLayout {	
	
	private List<String> retailersUsed;
	private List<String> competitorsUsed;
	private List<Product> products;
	private Settings settings;
	private Integer cheaper = 0;
	private Integer expensive = 0;
	private Integer equal = 0;
	
	
		public Component init() {
			
		

	        PieChartConfig config2 = new PieChartConfig();
	        config2
	            .data()
	            .labels("Soy más barato", "Mismo precio", "Soy más caro")
	            .addDataset(new PieDataset().label("Distribución Global de Precios" + " (" + (cheaper + expensive + equal) + " productos en común)"))
	            .and();

	        config2.
	            options()
	                .responsive(true)
	                .title()
	                    .display(true)
	                    .text("Distribución Global de Precios" + " (" + (cheaper + expensive + equal) + " productos en común)")
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
	         //   for (int i = 0; i < labels2.size(); i++) {
	         //   	data.add(dashboardService.getNumOfTasksByRetail(retailersUsed.get(i)).doubleValue());
	         //       colors.add(ColorUtils.randomColor(0.7));
	         //   }
	            
	            //Cheaper
		        data.add((double) cheaper);
		        colors.add(DemoUtils.RGB_GREEN);
	            
		        //Equal
		        data.add((double) equal);
		        colors.add(DemoUtils.RGB_BLUE);
		        
		        //Expensive
		        data.add((double) expensive);
		        colors.add(DemoUtils.RGB_RED);
		        
	            
	            lds.backgroundColor(colors.toArray(new String[colors.size()]));
	            lds.dataAsList(data);
	        }

	        ChartJs chart2 = new ChartJs(config2);
	        chart2.setJsLoggingEnabled(true);
	        chart2.setWidth("100%");
	        
			return chart2;
		}
		

	    public PieChart load() {
	    	User user = userServiceImpl.getUserByUsername("admin");
			settings = user.getSettings();
			
	    	LocalDate today = LocalDate.now();
		    LocalDate yesterday = today.minus(Period.ofDays(settings.getLastUpdateInDays()));
			java.util.Date lastUpdate = java.sql.Date.valueOf(yesterday);
			
          
	       
			
			  if (settings.getKeyType().equals("sku")) {

				  competitorsUsed = dashboardService.getCompetitorsBySku(settings.getMainSeller());
				  Set<String> competitorsUsedSet = new HashSet<String>(competitorsUsed);
				  
				 if (competitorsUsedSet.size()!=0) {
				  products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), lastUpdate,competitorsUsedSet);  //Change function for string.

				  for (String competitorUsed : competitorsUsed) {
					
					for (Product p: products) {
						List<Product> productCompetition = showAllProductsService.getProductsFromSellerNameAndSku(competitorUsed, p.getSku());
					   
				        if (productCompetition.size()!=0) {
				                
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
				}
			  }	
			}else {
				
				competitorsUsed = dashboardService.getCompetitorsByUpc(settings.getMainSeller());
				Set<String> competitorsUsedSet = new HashSet<String>(competitorsUsed);
				if (competitorsUsedSet.size()!=0) {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), lastUpdate,competitorsUsedSet);  //Change function for string.

				for (String competitorUsed : competitorsUsed) {
					
					for (Product p: products) {
						List<Product> productCompetition = showAllProductsService.getProductsFromSellerNameAndUpc(competitorUsed, p.getUpc());
					   
				        if (productCompetition.size()!=0) {
				                
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
				}				
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
    
	@Override
	public Component createComponent() {
		return new PieChart().load().init();
	}

}

