package com.dataprice.ui.reports;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.ReportSettings;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class ReportsMainLayout extends VerticalLayout implements View,GenerateReportListener{
	
	@Autowired
	private ReportSettingsLayoutFactory reportSettingsLayoutFactory;
	
	@Autowired
	private MatrixReportLayoutFactory matrixReportLayoutFactory;
	
	@PostConstruct
	void init() {
		
		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
		
        
		
		
	}

	private void addLayout() {
		Component reportTab = reportSettingsLayoutFactory.createComponent(this);
		addComponent(reportTab);
		setComponentAlignment(reportTab, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	@Override
	public void generateReport(ReportSettings reportSettings) {
		if (reportSettings.getTypeOfReport().equals("Matriz de Precios")) {
			Component priceMatrixReport = matrixReportLayoutFactory.createComponent(reportSettings);
			addComponent(priceMatrixReport);
		}else {
			//Do nothing for the moment
		}
		
	}

	
}