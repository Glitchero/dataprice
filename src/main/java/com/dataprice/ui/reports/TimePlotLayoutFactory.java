package com.dataprice.ui.reports;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.TimeLineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.options.scale.TimeScale;
import com.byteowls.vaadin.chartjs.options.scale.TimeScaleOptions;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.dashboard.DemoUtils;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class TimePlotLayoutFactory {
	
	
	private class TimePlot extends VerticalLayout {
		
        private Product product; //From the price Matrix
        private Set<String> competitors;
        private List<Product> products;
        private String mainSeller;
		
		public TimePlot(Product product,Set<String> competitors, String mainSeller) {
			this.product = product;
			this.competitors = competitors;
			this.mainSeller = mainSeller;
		}
		
		
		
		public Component init() {
			
			LocalDateTime t = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
			Calendar calendar = Calendar.getInstance(); // this would default to now
			//LocalDate tday = LocalDate.now();
			
	        LineChartConfig config = new LineChartConfig();
	        
	        //First we create a set of participants   
	         //Set<String> participants = new HashSet<String>(competitors);
	         //participants.add(mainSeller);
	        
	        //Later, we addDatasets dynamically
	        for (Product product : products) {
	        	config.data().addDataset(new TimeLineDataset().label(product.getSeller()).fill(false));
	        }
	        
	        //Configuration for handling date only
	        config.data()
	        //  .addDataset(new TimeLineDataset().label(mainSeller).fill(false))  //Add the mainSeller dataset
	        //  .addDataset(new TimeLineDataset().label("My Second dataset").fill(false))
	            .and()
	        .options()
	            .responsive(true)
	            .title()
	            .display(true)
	            .text(product.getName()) //Tittle of the plot
	            .and()
	        .tooltips()
	            .mode(InteractionMode.INDEX)
	            .intersect(false)
	            .and()
	        .hover()
	            .mode(InteractionMode.NEAREST)
	            .intersect(true)
	            .and()
	        .scales()
	        .add(Axis.X, new TimeScale()
	        .time()
	          .min(t.minusDays(30)) //antes 9
	          .max(t)
	          .stepSize(7) //antes 2
	          .unit(TimeScaleOptions.Unit.DAY)
	          .displayFormats()
	            .hour("DD.MM") // german date/time format
	            .and()
	        .and()
	      )
	        .add(Axis.Y, new LinearScale()
	                .display(true)
	                .scaleLabel()
	                    .display(true)
	                    .labelString("Precio")
	                    .and()
	                .ticks()
	                    .suggestedMin(0)
	                    .suggestedMax(250)
	                    .and()
	                .position(Position.RIGHT))
	        .and()
	        .done();

	        int con = 0; //Counter to identify datasets
	        for (Product product : products) {
	        	Dataset<?, ?> ds = config.data().getDatasetAtIndex(con);
	        	TimeLineDataset lds = (TimeLineDataset) ds;
	            lds.borderColor(ColorUtils.randomColor(.4));
	            lds.backgroundColor(ColorUtils.randomColor(.1));
	            lds.pointBorderColor(ColorUtils.randomColor(.7));
	            lds.pointBackgroundColor(ColorUtils.randomColor(.5));
	            lds.pointBorderWidth(1);
	            for (int i = 0; i < 31; i++) { 
	            	java.util.Date date = java.sql.Date.valueOf(t.minusDays(i).toLocalDate());
	            	calendar.setTime(date);
	            	Double price = reportsService.getHistoricalPriceFromKey(product.getProductKey(), calendar);
	            	if (price!=null)
	            		lds.addData(t.minusDays(i), price);    
	            }
	            con++;
	        }  
	        
	        ChartJs chart = new ChartJs(config);
	        chart.setJsLoggingEnabled(true);
	        chart.addClickListener((a,b) ->
	            DemoUtils.notification(a, b, config.data().getDatasets().get(a)));
	        chart.setWidth("100%");
			return chart;
		}
		
		public TimePlot load() {
			
			products = showAllProductsService.getAllPrductsForTimePlot(product.getSku(),competitors,mainSeller); //instead of mainSeller could be product.getSeller
			System.out.println("Tamaño de productos donde coincide SKU: " + products.size());
			
			
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
	
	//Participants = me + competitors i selected previously!!
	public Component createComponent(Product product,Set<String> competitors, String mainSeller) {
		return new TimePlot(product,competitors,mainSeller).load().init();
	}
	

}
