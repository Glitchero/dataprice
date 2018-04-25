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

	private List<String> competitors;
	
	private List<String> categories;
	
	private Settings settings;
	
	private TwinColSelect<String> categoriesSelect;

	private TwinColSelect<String> competitorsSelect;
	
	private TwinColSelect<String> fieldsSelect;
	
	private DateField lastDate;
		
	private ComboBox typeOfReport;
	
	private Button generateReport;
	
	private Button exportReport;

	private Label separator1;

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
			
			reportSettings = new ReportSettings();
					
			separator1 = new Label("<hr />",ContentMode.HTML);	
			separator1.setWidth("100%");

			mainTittle = new Label("<b><font size=\"5\">Seleccione los parámetros de su reporte: </font></b>",ContentMode.HTML);	
			
			
			categoriesSelect = new TwinColSelect<>("Selecciona tus categorías:");
			categoriesSelect.setItems(categories);
			categoriesSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			categoriesSelect.setHeight("165px"); //170 antes
			
			competitorsSelect = new TwinColSelect<>("Selecciona tu competencia:");
			competitorsSelect.setItems(competitors);
			competitorsSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			competitorsSelect.setHeight("165px");
			
			//Name and Price are the minimum
			fieldsSelect = new TwinColSelect<>("Selecciona los campos a agregar:");
			fieldsSelect.setItems("Key(UPC or SKU)","Category","Descripción", "Marca","Url", "ImageUrl","Fecha de Actualizacíon");
			fieldsSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			fieldsSelect.setHeight("170px");
			
			lastDate = new DateField("Seleccione la fecha de última actualización:");
			//startDate.setValue(LocalDate.now());
			lastDate.setWidth("100%");
			
					
			//System.out.println("Tiempo: " + LocalDate.now());
			//reportSettings.setStartDate(LocalDate.now());
			//reportSettings.setEndDate(LocalDate.now());
			
			typeOfReport = new ComboBox("Seleccione el tipo de reporte:");
			typeOfReport.setItems("Matriz de Precios en Unidades","Matriz de Precios en Porcentajes");
			typeOfReport.setWidth("100%");
			
			generateReport = new Button("Generar Reporte");
			generateReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			generateReport.addClickListener(this);
			generateReport.setWidth("100%");
			
			exportReport = new Button("Exportar a Excel");
			exportReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			exportReport.addClickListener(this);
			exportReport.setWidth("100%");
			
			
			return this;
		}
		


		public ReportSettingsLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
						
			categories = reportsService.getCategoryListForSeller(settings.getMainSeller());
			competitors = reportsService.getCompetitorsList(settings.getMainSeller());
			return this;
		}
	

		
		public Component layout() {
			HorizontalLayout h3 = new HorizontalLayout(lastDate);
			h3.setWidth("50%");
			h3.setMargin(false);
			
			HorizontalLayout h4 = new HorizontalLayout(generateReport,exportReport);
			h4.setWidth("100%");
			h4.setMargin(false);
			
			
			
			VerticalLayout h2 = new VerticalLayout(h3,typeOfReport,h4);
		//	h2.setComponentAlignment(generateReport, Alignment.BOTTOM_CENTER);
			h2.setWidth("100%");
			h2.setMargin(false);
			
			HorizontalLayout h1 = new HorizontalLayout(categoriesSelect,competitorsSelect,h2);
			h1.setSizeFull();
			h1.setMargin(false);
		//	h1.setComponentAlignment(v1, Alignment.TOP_CENTER);
			
			VerticalLayout v2 = new VerticalLayout(h1,separator1);
		//	VerticalLayout v2 = new VerticalLayout(h1);
			v2.setComponentAlignment(h1, Alignment.MIDDLE_CENTER);
			v2.setSizeFull();
			v2.setMargin(false);
			
			return v2;
		}

		public ReportSettingsLayout bind() {
			binder.forField(categoriesSelect)
			  .asRequired("categories are required")
			  .bind("categories");
			
			binder.forField(competitorsSelect)
			  .asRequired("competitors are required")
			  .bind("competitors");
			
			binder.forField(lastDate)
			  .asRequired("last date is required")
			  .bind("lastUpdate");

			binder.forField(typeOfReport)
			  .asRequired("Type of report is required")
			  .bind("typeOfReport");
			
			binder.readBean(reportSettings);
			
			return this;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			
			if (event.getSource()==generateReport) {
				generateReport();
				
			}else {
				exportToExcel();
			}
			
			
		}



		private void exportToExcel() {
			try {
				binder.writeBean(reportSettings);
			} catch (ValidationException e) {
				Notification.show("ERROR","Error in reports Settings",Type.ERROR_MESSAGE);
				return;
			}			
			exportToExcelListener.exportToExcel(reportSettings);
			
		}


		private void generateReport() {
			try {
				binder.writeBean(reportSettings);
			} catch (ValidationException e) {
				Notification.show("ERROR","Error in reports Settings",Type.ERROR_MESSAGE);
				return;
			}			
			generateReportListener.generateReport(reportSettings);
		}

	
	}
		
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
