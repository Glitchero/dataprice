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
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.DefaultScale;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.dashboard.DemoUtils;
import com.dataprice.ui.products.Icon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class StackedChartDistributionByCompetition implements UIComponentBuilder{

	
	private class StackedChart extends VerticalLayout {

		
		private List<String> competitorsUsed;
		private List<String> competitorsUsedWithTotal;
		private List<Product> products;
		private List<Integer> distribution;
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
		                    .stacked(true))
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
		            //  data.add((double) (Math.random() > 0.5 ? -1 : 1) * Math.round(Math.random() * 100));
		            //	System.out.println(lds.toString());
		            	data.add(valueList.get(con));
		           // 	System.out.println(distribution.get(con) + " - " + con);
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
		        chart.setSizeFull();
		        return chart;
		}	
		
		
		public StackedChart load() {
			Integer cheaper = 0;
			Integer equal = 0;
			Integer expensive = 0;
			
			LocalDate today = LocalDate.now();
		    LocalDate yesterday = today.minus(Period.ofDays(1));
			java.util.Date lastUpdate = java.sql.Date.valueOf(yesterday);
			
			competitorsUsedWithTotal = new ArrayList<String>();
			distribution = new ArrayList<Integer>();
			cheaperList = new LinkedList<Double>();
			expensiveList = new LinkedList<Double>();
			equalList = new LinkedList<Double>();
			
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			
			if (settings.getKeyType().equals("sku")) {

				competitorsUsed = dashboardService.getCompetitorsBySku(settings.getMainSeller());
				Set<String> competitorsUsedSet = new HashSet<String>(competitorsUsed);
						
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
				    //	Integer total = dashboardService.getTotalOfProductsFromCompetitorBySku(settings.getMainSeller(),competitorUsed);
						
					System.out.println("competitorUsed: " + competitorUsed);
					
					Integer total = cheaper+expensive+equal;
					competitorsUsedWithTotal.add(competitorUsed + " (" + total + " productos)");
					/**
					System.out.println("Cheaper: " + cheaper);
					System.out.println("expensive: " + expensive);
					System.out.println("equal: " + equal);
					
					
					Double per_cheaper = cheaper/(double) total;
					Double per_expensive = expensive/(double) total;
					
					int a = (int) Math.round(per_cheaper * 100);
					*/
					
				//	DecimalFormat df=new DecimalFormat("#.##");
					
					Locale currentLocale = Locale.getDefault();
					DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
					otherSymbols.setDecimalSeparator('.'); 
					NumberFormat df = new DecimalFormat("#.##",otherSymbols);
					
					cheaperList.add(Double.valueOf(df.format(cheaper/(double) total * 100)));
					System.out.println("% Cheaper: " + Math.round(cheaper/(double) total * 100) );
				
					expensiveList.add(Double.valueOf(df.format(expensive/(double) total * 100)));
					System.out.println("% expensive: " + Math.round(expensive/(double) total * 100));

					equalList.add(Double.valueOf(df.format(equal/(double) total * 100)));
					System.out.println("% equal: " + Math.round(equal/(double) total * 100));
				
					
					System.out.println("Total: " + (total));
				
				
					cheaper = 0;
					expensive = 0;
					equal = 0;
					total = 0;
				}
				
				valueList = new LinkedList<Double>();
				valueList.addAll(cheaperList);
				valueList.addAll(equalList);
				valueList.addAll(expensiveList);
				
			}else {
				competitorsUsed = dashboardService.getCompetitorsByUpc(settings.getMainSeller());
				for (String competitorUsed : competitorsUsed) {
					Integer total = dashboardService.getTotalOfProductsFromCompetitorByUpc(settings.getMainSeller(),competitorUsed);
					competitorsUsedWithTotal.add(competitorUsed + " (" + total + " productos)");
					
					//Here i have to bring the sku they share main seller and the competitorused.
					//Traverse the sku list 
				}
			}
			
			////////////////////////////
			for (int i=0;i<5; i++) {
				distribution.add(25);
			}
			for (int i=5;i<20; i++) {
				distribution.add(50);
			}
			///////////////////////////
			
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
		return new StackedChart().load().init();
	}

}
