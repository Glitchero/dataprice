package com.dataprice.ui.view;


/**
 * Some ideas for the dashboard.
 * Basically, we have four variables:
 * 1.-Total of tasks
 * 2.-Total of products
 * 3.-Retails
 * 4.-Status
 * 5(Optional).-Date and time, number of tasks downloaded yesterdar, maybe a line plot
 * 6(Optional).-Showing velocity or number of active cores!.
 * 7.-Showing downloading tasks or task at the moment.
 */
import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.dataprice.model.entity.Product;
import com.dataprice.service.productstatistics.ProductStatisticsService;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

@SpringView
public class HomePage extends VerticalLayout implements View {

	
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	@Autowired
	private ProductStatisticsService productStatisticsService;
	
	@PostConstruct
	void init() {
		//Statistics
		Integer	numberOfProducts = productStatisticsService.getNumOfProducts(); 
		Integer numberOfProductsWithoutProfile = productStatisticsService.getNumOfProductsWithoutPid();
		
		
		setSizeFull();
		setMargin(false);
		
		Label title = new Label();
		title.setCaption("Home");
		title.setValue("Fist componenteee");
		
		
		Label title2 = new Label(
			    "Total de Productos: \n" +
			    "<center><b><font size=\"7\">" + numberOfProducts + "</font></b></center>",
			    ContentMode.HTML);
		
      //  Icon icon = new Icon(VaadinIcons.DOWNLOAD_ALT);
      //  icon.setSize(60);
        
	    HorizontalLayout vl = new HorizontalLayout(title2);
	  //  vl.setComponentAlignment(icon, Alignment.TOP_RIGHT);
		vl.setMargin(false);
		vl.setSizeFull();
		
		Label title3 = new Label(
			    "Por perfilar: \n" +
			    "<center><b><font size=\"7\">" + numberOfProductsWithoutProfile + "</font></b></center>",
			    ContentMode.HTML);
		
		Label title4 = new Label();
		title4.setCaption("Home");
		title4.setValue("Cuarto component");
		
		Label title5 = new Label();
		title5.setCaption("Home");
		title5.setValue("Quinto component");
		
		Label title6 = new Label();
		title6.setCaption("Home");
		title6.setValue("Sexto component");
		
		Label title7 = new Label();
		title7.setCaption("Home");
		title7.setValue("Septimo component");
		
		
		Label title8 = new Label();
		title8.setCaption("Home");
		title8.setValue("extra comp");
		
		
		List<Product> products = showAllProductsService.getAllProducts();
		Grid<Product> productsTable = new Grid<>(Product.class);
		
		productsTable.setColumnOrder("productId","retail","name", "price", "imageUrl","productUrl","pid","gender","category","subcategory","brand");
		productsTable.removeColumn("task");
		
		productsTable.setItems(products);
		productsTable.setSizeFull();

		



        
        /////////////////////////////////////////////////////////////
        
        BarChartConfig config = new BarChartConfig();
        config
            .data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("rgba(151,187,205,0.5)").borderColor("white").borderWidth(2))
                .addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)"))
                .and();
        
        config.
            options()
                .responsive(true)
                .title()
                    .display(true)
                    .position(Position.LEFT)
                    .text("Chart.js Combo Bar Line Chart")
                    .and()
               .done();
        
        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
            }
            
            if (ds instanceof BarDataset) {
                BarDataset bds = (BarDataset) ds;
                bds.dataAsList(data);    
            }
                
            if (ds instanceof LineDataset) {
                LineDataset lds = (LineDataset) ds;
                lds.dataAsList(data);    
            }
        }
        
        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.setWidth("100%");
        chart.setHeight("100%"); //if this is activated. the chart takes half of the screen ,, why??
        /////////////////////////////////////////////////////////////
        
        PieChartConfig config2 = new PieChartConfig();
        config2
            .data()
                .labels("Red", "Green", "Yellow", "Grey", "Dark Grey")
                .addDataset(new PieDataset().label("Dataset 1"))
                .and();

        config2.
            options()
                .responsive(true)
                .title()
                    .display(true)
                    .text("Chart.js Single Pie Chart")
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
            	data.add(50.0); //Same size for all labels
                colors.add(ColorUtils.randomColor(0.7));
            }
            lds.backgroundColor(colors.toArray(new String[colors.size()]));
            lds.dataAsList(data);
        }

        ChartJs chart2 = new ChartJs(config2);
        chart2.setJsLoggingEnabled(true);
        chart2.setWidth("100%");
   
        
        //////////////////////////////////////////////////////////////
        
        LineChartConfig lineConfig = new LineChartConfig();
        lineConfig.data()
            .labels("January", "February", "March", "April", "May", "June", "July")
            .addDataset(new LineDataset().label("My First dataset").fill(false))
            .addDataset(new LineDataset().label("My Second dataset").fill(false))
            .addDataset(new LineDataset().label("Hidden dataset").hidden(true))
            .and()
        .options()
            .responsive(true)
            .title()
            .display(true)
            .text("Chart.js Line Chart")
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
        .add(Axis.X, new CategoryScale()
                .display(true)
                .scaleLabel()
                    .display(true)
                    .labelString("Month")
                    .and()
                .position(Position.TOP))
        .add(Axis.Y, new LinearScale()
                .display(true)
                .scaleLabel()
                    .display(true)
                    .labelString("Value")
                    .and()
                .ticks()
                    .suggestedMin(-10)
                    .suggestedMax(250)
                    .and()
                .position(Position.RIGHT))
        .and()
        .done();

        // add random data for demo
        List<String> labels3 = lineConfig.data().getLabels();
        for (Dataset<?, ?> ds : lineConfig.data().getDatasets()) {
            LineDataset lds = (LineDataset) ds;
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels3.size(); i++) {
                data.add((double) Math.round(Math.random() * 100));
            }
            lds.dataAsList(data);
            lds.borderColor(ColorUtils.randomColor(0.3));
            lds.backgroundColor(ColorUtils.randomColor(0.5));
        }

        ChartJs chart3 = new ChartJs(lineConfig);
        chart3.setJsLoggingEnabled(true);
        chart3.setWidth("100%");
        
        
        //////////////////////////////////////////////////////////////
        
        setMargin(false);
       /** 
        VerticalSplitPanel v1 = new VerticalSplitPanel(); 
        v1.setFirstComponent(chart);
        v1.setSecondComponent(chart2);
        v1.setSplitPosition(50);
        v1.setSizeFull();
      */
        
        HorizontalSplitPanel h1 = new HorizontalSplitPanel();   
        h1.setFirstComponent(chart);
        h1.setSecondComponent(chart2);
        h1.setSplitPosition(50);
        h1.setSizeFull();
        
        VerticalSplitPanel v2 = new VerticalSplitPanel(); 
        v2.setFirstComponent(h1);
        v2.setSecondComponent(chart3);
        v2.setSplitPosition(50);
        v2.setSizeFull();
       
        
       
        
        HorizontalSplitPanel splitContentCode = new HorizontalSplitPanel();        
        splitContentCode.setFirstComponent(v2);
        splitContentCode.setSecondComponent(productsTable);
        splitContentCode.setSplitPosition(50);
        splitContentCode.setWidth("100%");
        splitContentCode.setHeight("650px");
     
        HorizontalLayout hl = new HorizontalLayout(vl,title3,title4,title5,title6,title7);
        hl.setHeight("150px");
        hl.setWidth("100%");
        hl.setMargin(false);
        addComponent(hl);
        addComponent(splitContentCode);
        
        
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
}
