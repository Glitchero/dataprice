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
public class ReportsMainLayout extends VerticalLayout implements View,GenerateReportListener,ExportToExcelListener{
	
	@Autowired
	private ReportSettingsLayoutFactory reportSettingsLayoutFactory;
	
	@Autowired
	private MatrixReportLayoutFactory matrixReportLayoutFactory;
	
	@Autowired
	private ExcelMatrixReportLayoutFactory excelMatrixReportLayoutFactory;
	
	@Autowired
	private PerMatrixReportLayoutFactory perMatrixReportLayoutFactory;
	
	@PostConstruct
	void init() {
		
		setSizeFull();
		setMargin(false);
		removeAllComponents();
		addLayout();
		
        
		
		
	}

	private void addLayout() {
		Component reportTab = reportSettingsLayoutFactory.createComponent(this,this);
		addComponent(reportTab);
		setComponentAlignment(reportTab, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	@Override
	public void generateReport(ReportSettings reportSettings) {
		removeAllComponents();
		if (reportSettings.getTypeOfReport().equals("Matriz de Precios en Unidades")) {
			Component priceMatrixReport = matrixReportLayoutFactory.createComponent(reportSettings);
			addComponent(priceMatrixReport);
		}else {
			Component priceMatrixReport = perMatrixReportLayoutFactory.createComponent(reportSettings);
			addComponent(priceMatrixReport);
		}
		
	}

	@Override
	public void exportToExcel(ReportSettings reportSettings) {
		removeAllComponents();
		if (reportSettings.getTypeOfReport().equals("Matriz de Precios en Unidades")) {
			Component excelPriceMatrixReport = excelMatrixReportLayoutFactory.createComponent(reportSettings);
			addComponent(excelPriceMatrixReport);
			
		}else {
			//Do nothing for the moment
		}
		
	}

	
}