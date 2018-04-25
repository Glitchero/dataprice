package com.dataprice.ui.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.ui.dashboard.charts.DonutChartProductsPerRetail;
import com.dataprice.ui.dashboard.charts.LineChartProductsPerDay;
import com.dataprice.ui.dashboard.charts.PieChartBotsPerRetail;
import com.dataprice.ui.dashboard.charts.PieChartProductsPerRetail;
import com.dataprice.ui.settings.AddSettingsMainLayoutFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@SpringView
public class DashboardLayoutFactory extends VerticalLayout implements View{

	/**
	 * Some ideas for the future dashboard, but first i need to finish the first report (at least).
	 * Header
	 * 1.- Total of products
	 * 2.- Total of tasks
	 * 3.- My products
	 * 4.- Checked percentage
	 * 6.- Matched percentage
	 * 7.- Canceled bots
	 * 8.- Finalized bots
	 * 9.- Pending bots
	 * 10.-Total products matched
	 * 11.-Competence following.
	 * 
	 * It would be nice to add a ranking board for me and my competence, fist place sell the lower and so on !!.
	 * 
	 * I need two more pie-charts:
	 * 
	 * 1.- (Depends on sku or upc) How i am positionated against my competition. equal, higher than, less than , could be average or counting competition.
	 * 2.- Number of matched products by seller in Sku or Upc. This also show my competence.
	 */
	
	@Autowired
	private HeaderLayoutFactory headerLayoutFactory;
	
	@Autowired
	private PieChartBotsPerRetail pieChartBotsPerRetail;
	
	@Autowired
	private DonutChartProductsPerRetail donutChartProductsPerRetail;
	
	@Autowired
	private LineChartProductsPerDay lineChartProductsPerDay;
	
	@Autowired
	private PieChartProductsPerRetail pieChartProductsPerRetail;
	
	private void addLayout() {
		Component header = headerLayoutFactory.createComponent();
		
		Component pieChart = pieChartBotsPerRetail.createComponent();
		
		Component lineChart = lineChartProductsPerDay.createComponent();
		
		Component donutChart = donutChartProductsPerRetail.createComponent();
		
		Component pieChart2 = pieChartProductsPerRetail.createComponent();
		
		    HorizontalSplitPanel splitContentCode = new HorizontalSplitPanel();        
	        splitContentCode.setFirstComponent(pieChart);
	        splitContentCode.setSecondComponent(pieChart2);
	        splitContentCode.setSplitPosition(50);
	        splitContentCode.setWidth("100%");

	     //   VerticalLayout v2 = new VerticalLayout(lineChart); 
	      //  v2.setComponentAlignment(lineChart, Alignment.BOTTOM_CENTER);
	       // v2.setSizeFull();
	        //v2.setMargin(false);
	        
	        HorizontalSplitPanel splitContentCode2 = new HorizontalSplitPanel();        
	        splitContentCode2.setFirstComponent(splitContentCode);
	        splitContentCode2.setSecondComponent(lineChart);
	        splitContentCode2.setSplitPosition(50);
	        splitContentCode2.setWidth("100%");
	        
	     

	        addComponent(header);
		    addComponent(splitContentCode2);
		
		
		
	}
	
	@PostConstruct
	void init() {

		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
	
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}
	
}
