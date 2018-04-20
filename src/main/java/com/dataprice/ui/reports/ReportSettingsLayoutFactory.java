package com.dataprice.ui.reports;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.vaadin.annotations.Push;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
	
	private DateField startDate;
	
	private DateField endDate;
	
	private ComboBox typeOfReport;
	
	private Button generateReport;

	private Label separator1;

	
	private class ReportSettingsLayout extends VerticalLayout implements Button.ClickListener {

		private GenerateReportListener generateReportListener;

		public ReportSettingsLayout(GenerateReportListener generateReportListener) {
			this.generateReportListener = generateReportListener;
		}



		public ReportSettingsLayout init() {

			separator1 = new Label("<hr />",ContentMode.HTML);	
			separator1.setWidth("100%");

			mainTittle = new Label("<b><font size=\"5\">Seleccione los parámetros de su reporte: </font></b>",ContentMode.HTML);	
			
			
			categoriesSelect = new TwinColSelect<>("Selecciona tus categorías:");
			categoriesSelect.setItems(categories);
			categoriesSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			categoriesSelect.setHeight("170px");
			
			competitorsSelect = new TwinColSelect<>("Selecciona tu competencia:");
			competitorsSelect.setItems(competitors);
			competitorsSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			competitorsSelect.setHeight("170px");
			
			//Name and Price are the minimum
			fieldsSelect = new TwinColSelect<>("Selecciona los campos a agregar:");
			fieldsSelect.setItems("Key(UPC or SKU)","Category","Descripción", "Marca","Url", "ImageUrl","Fecha de Actualizacíon");
			fieldsSelect.addSelectionListener(event -> addComponent(new Label("Selected: " + event.getNewSelection())));
			fieldsSelect.setHeight("170px");
			
			startDate = new DateField("Apartir de:");
			startDate.setValue(LocalDate.now());
			startDate.setWidth("100%");
			
			endDate = new DateField("Hasta:");
			endDate.setValue(LocalDate.now());
			endDate.setWidth("100%");
			
			typeOfReport = new ComboBox("Seleccione el tipo de reporte:");
			typeOfReport.setItems("Matriz de Precios","Matriz de Descuentos");
			typeOfReport.setWidth("100%");
			
			generateReport = new Button("Generar Reporte");
			generateReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			generateReport.addClickListener(this);
			generateReport.setWidth("100%");
			
			
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
			HorizontalLayout h3 = new HorizontalLayout(startDate,endDate);
			h3.setWidth("100%");
			h3.setMargin(false);
			
			HorizontalLayout h2 = new HorizontalLayout(h3,typeOfReport,generateReport);
			h2.setComponentAlignment(generateReport, Alignment.BOTTOM_CENTER);
			h2.setWidth("70%");
			h2.setMargin(false);
			
			HorizontalLayout h1 = new HorizontalLayout(categoriesSelect,competitorsSelect,fieldsSelect);
			h1.setSizeFull();
			h1.setMargin(false);
		//	h1.setComponentAlignment(v1, Alignment.TOP_CENTER);
			
			VerticalLayout v2 = new VerticalLayout(mainTittle,h1,h2,separator1);
			v2.setComponentAlignment(h1, Alignment.MIDDLE_CENTER);
			v2.setSizeFull();
			v2.setMargin(false);
			
			return v2;
		}


		@Override
		public void buttonClick(ClickEvent event) {
			ReportSettings reportSettings = new ReportSettings(categoriesSelect.getValue(), competitorsSelect.getValue(), fieldsSelect.getValue(), startDate.getValue(),endDate.getValue());
			generateReportListener.generateReport(typeOfReport.getValue().toString(), reportSettings);
		}

	
	}
		
	@Autowired 
	private ReportsService reportsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	
	public Component createComponent(GenerateReportListener generateReportListener) {
		return new ReportSettingsLayout(generateReportListener).load().init().layout();
	}





	

}
