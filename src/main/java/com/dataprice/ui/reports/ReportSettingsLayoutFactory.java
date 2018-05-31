package com.dataprice.ui.reports;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.dashboard.DashboardService;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.vaadin.annotations.Push;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ReportSettingsLayoutFactory {
	
	private Label mainTittle;	
	
	private Label subTittle;

	private List<String> competitors;
	
	private Settings settings;
	
	private TwinColSelect<String> competitorsSelect;
			
	private ComboBox typeOfReport;
	
	private Button generateReport;
	
	private ReportSettings reportSettings;
	
	private class ReportSettingsLayout extends VerticalLayout implements Button.ClickListener {

		private GenerateReportListener generateReportListener;
		private ExportToExcelListener exportToExcelListener;
		private Binder<ReportSettings> binder;
		
		public ReportSettingsLayout(GenerateReportListener generateReportListener, ExportToExcelListener exportToExcelListener) {
			this.generateReportListener = generateReportListener;
			this.exportToExcelListener =  exportToExcelListener;
		}



		public ReportSettingsLayout init() {

			binder = new Binder<>(ReportSettings.class);
			
			reportSettings = new ReportSettings(settings.getLastUpdateInDays()); //1 day last update!!!
			
			mainTittle = new Label("<b><font size=\"5\">Creador de Reportes </font></b>",ContentMode.HTML);	
			subTittle = new Label("<font size=\"2\">Crea reportes con los parámetros deseados y visualízalos en el navegador o en Excel. </font>",ContentMode.HTML);	
			
			competitorsSelect = new TwinColSelect<>("Selecciona tu competencia:");
			competitorsSelect.setItems(competitors);
			competitorsSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			
			typeOfReport = new ComboBox("Seleccione el tipo de reporte:");
			typeOfReport.setItems("Matriz de Precios con Indicadores","Matriz de Precios en Porcentajes","Matriz de Precios");
			typeOfReport.setWidth("50%");
			
			generateReport = new Button("Generar Reporte");
			generateReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			generateReport.addClickListener(this);
			generateReport.setWidth("100%");
			
			return this;
		}
		


		public ReportSettingsLayout load() {
			User user = userServiceImpl.getUserByUsername("admin");
			settings = user.getSettings();
						
			//competitors = reportsService.getCompetitorsList(settings.getMainSeller());
			
			if (settings.getKeyType().equals("sku")) {
				competitors = dashboardService.getCompetitorsBySku(settings.getMainSeller());
			}else {
				competitors = dashboardService.getCompetitorsByUpc(settings.getMainSeller());
			}
				
				
			return this;
		}
	

		
		public Component layout() {
	
			HorizontalLayout h4 = new HorizontalLayout(generateReport);
			h4.setWidth("20%");
			h4.setMargin(false);
			
			FormLayout f1 = new FormLayout(competitorsSelect,typeOfReport);
			
			
			VerticalLayout v2 = new VerticalLayout(mainTittle,subTittle,f1,h4);
			v2.setMargin(false);
			v2.setSizeFull();

			return v2;
		}

		public ReportSettingsLayout bind() {
			
			binder.forField(competitorsSelect)
			  .asRequired("competitors are required")
			  .bind("competitors");
			
			binder.forField(typeOfReport)
			  .asRequired("Type of report is required")
			  .bind("typeOfReport");
			
			binder.readBean(reportSettings);
			
			return this;
		}

		@Override
		public void buttonClick(ClickEvent event) {			
			generateReport();			
		}


		private void generateReport() {
			try {
				binder.writeBean(reportSettings);
			} catch (ValidationException e) {
				Notification.show("ERROR","Error en los ajustes del reporte",Type.ERROR_MESSAGE);
				return;
			}			
			generateReportListener.generateReport(reportSettings);
		}

	
	}
	
    @Autowired
    private DashboardService dashboardService;
		
	@Autowired 
	private ReportsService reportsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	public Component createComponent(GenerateReportListener generateReportListener,ExportToExcelListener exportToExcelListener) {
		return new ReportSettingsLayout(generateReportListener,exportToExcelListener).load().init().bind().layout();
	}





	

}
