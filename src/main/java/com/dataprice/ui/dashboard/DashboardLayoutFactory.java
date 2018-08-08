package com.dataprice.ui.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.ui.dashboard.charts.DonutChartProductsPerRetail;
import com.dataprice.ui.dashboard.charts.LineChartProductsPerDay;
import com.dataprice.ui.dashboard.charts.PieChartBotsPerRetail;
import com.dataprice.ui.dashboard.charts.PieChartGlobalPricingDistribution;
import com.dataprice.ui.dashboard.charts.PieChartProductsPerRetail;
import com.dataprice.ui.dashboard.charts.StackedChartDistributionByCompetition;
import com.dataprice.ui.settings.AddSettingsMainLayoutFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

@SpringView
public class DashboardLayoutFactory extends VerticalLayout implements View{

	/**
	 * 1.-Global pie chart plot. --->Important
	 * 2.-Bar chart distribution for each competitor. --->Important
	 * 3.-Show price strategies. --->Important
	 * 4.-Maybe show competitors matching distribution. 
	 */
	

	@Autowired
	private PieChartGlobalPricingDistribution pieChartGlobalPricingDistribution;
	
	@Autowired
	private PieChartBotsPerRetail PieChartBotsPerRetail;

	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired 
	private HeaderLayoutFactory headerLayoutFactory;
	
	@Autowired
	private StackedChartDistributionByCompetition stackedChartDistributionByCompetition;
	
	private void addLayout() {
		
		User user = userServiceImpl.getUserByUsername("admin");
		Settings settings = user.getSettings();
	
		Label mainTittle = new Label("<b><font size=\"5\">" + "Dashboard" + "</font></b>" ,ContentMode.HTML);
		Label subTittle = new Label("<font size=\"2\">Este dashboard muestra información sobre el plan que ha contratado con Dataprice. </font>",ContentMode.HTML);
	
		Component pieChartBotsPerRetail = PieChartBotsPerRetail.createComponent();
		
		Component barChartDistributionByRetail = stackedChartDistributionByCompetition.createComponent();

		Button button = new Button("Revisa Nuestros Tutoriales");
		button.setWidth("100%");
		button.setStyleName(ValoTheme.BUTTON_PRIMARY);
		
		VerticalLayout vl = new VerticalLayout(button,pieChartBotsPerRetail);
		    HorizontalSplitPanel splitContentCode = new HorizontalSplitPanel();        
	        splitContentCode.setFirstComponent(headerLayoutFactory.createComponent());
	        splitContentCode.setSecondComponent(vl);
	        splitContentCode.setSplitPosition(50);
	        splitContentCode.setWidth("100%");

	     //   VerticalLayout v2 = new VerticalLayout(lineChart); 
	      //  v2.setComponentAlignment(lineChart, Alignment.BOTTOM_CENTER);
	       // v2.setSizeFull();
	        //v2.setMargin(false);
	        
	        /**
	        HorizontalSplitPanel splitContentCode2 = new HorizontalSplitPanel();        
	        splitContentCode2.setFirstComponent(splitContentCode);
	        splitContentCode2.setSecondComponent(lineChart);
	        splitContentCode2.setSplitPosition(50);
	        splitContentCode2.setWidth("100%");
	        
	     

	        addComponent(header);
		    addComponent(splitContentCode2);
		    addComponent(pieChartNew);
		*/
	        addComponent(mainTittle);
	        addComponent(subTittle);
	        addComponent(splitContentCode);
		
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
