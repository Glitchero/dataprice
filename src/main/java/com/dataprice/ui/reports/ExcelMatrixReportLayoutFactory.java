package com.dataprice.ui.reports;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.reports.exporter.DefaultGridHolder;
import com.dataprice.ui.reports.exporter.ExcelExport;
import com.dataprice.ui.reports.exporter.TableHolder;
import com.dataprice.ui.view.MemberPage.PayCheck;
import com.vaadin.annotations.Push;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.NumberRenderer;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ExcelMatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	
	//Excel export
	private DataProvider<Product, ?> dataProvider;
	private DecimalFormat df = new DecimalFormat("#0.0000");
    private SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy");

	
	private class ExcelMatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public ExcelMatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public ExcelMatrixReportLayout init() {
			productsTable = new Grid<>(Product.class);
			
			productsTable.setDataProvider(dataProvider);
			  
			productsTable.removeAllColumns();
			
			if (settings.getKeyType().equals("sku")) {
		    	productsTable.addColumn(p -> p.getSku()).setCaption("SKU").setId("Mysku");
			}else {
			   productsTable.addColumn(p -> p.getUpc()).setCaption("UPC").setId("Myupc");
			}
			productsTable.addColumn(p -> p.getName()).setCaption("Nombre").setId("Myname");	
			productsTable.addColumn(p -> p.getUpdateDay()).setCaption("Actualizado").setId("Update").setRenderer(new DateRenderer(sdf));	
			productsTable.addColumn(p -> p.getPrice()).setCaption("Mi precio").setId("Myprice").setRenderer(new NumberRenderer(df));
			
			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
					 productsTable.addColumn(p -> {
						 List<Product> products = null;
						 if (settings.getKeyType().equals("sku")) {
							 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
							}else {
							 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
							}
				    //    if (products.size()!=0) {
				    //    }
					    return products.get(0).getPrice();
					}).setCaption(seller).setId(seller).setRenderer(new NumberRenderer(df));
				  
				}
			
			
			productsTable.addColumn(p -> p.getBrand()).setCaption("Brand").setId("Mybrand");

			productsTable.setSizeFull();
			
			return this;
		}

		public Component layout() {

			// put the Grid in the TableHolder after the grid is fully baked
	        final TableHolder tableHolder = new DefaultGridHolder(productsTable);

	        // create the layout with the main export options
	        final HorizontalLayout mainOptions = new HorizontalLayout();

	        mainOptions.setSizeFull();
	        
	     //   final Label headerLabel = new Label("<b><font size=\"4\">Export options: </font></b>",ContentMode.HTML);
	        final Label verticalSpacer = new Label();
	        verticalSpacer.setHeight("10px");
	     //   final Label endSpacer = new Label();
	     //   endSpacer.setHeight("10px");
	        final TextField reportTitleField = new TextField("Report Title", "Demo Report");
	        reportTitleField.setWidth("100%");
	      
	        final TextField sheetNameField = new TextField("Sheet Name", "Grid Export");
	        sheetNameField.setWidth("100%");

	        final TextField exportFileNameField = new TextField("Export Filename", "Grid-Export.xls");
	        exportFileNameField.setWidth("100%");


	      //  mainOptions.addComponent(headerLabel);
	      //  mainOptions.addComponent(verticalSpacer);
	        mainOptions.addComponent(reportTitleField);
	        mainOptions.addComponent(sheetNameField);
	        mainOptions.addComponent(exportFileNameField);
	  

	        // create the export buttons
	        final Resource export = FontAwesome.FILE_EXCEL_O;
	        final Button regularExportButton = new Button("Exportar");
	        regularExportButton.setIcon(export);
	       //regularExportButton.setWidth("100%");

	        regularExportButton.addClickListener(new Button.ClickListener() {
	            private static final long serialVersionUID = -73954695086117200L;
	            private ExcelExport excelExport;

	            @Override
	            public void buttonClick(final ClickEvent event) {
	                if (!"".equals(sheetNameField.getValue().toString())) {
	                        excelExport = new ExcelExport(tableHolder, sheetNameField.getValue().toString());
	                } else {
	                        excelExport = new ExcelExport(tableHolder);
	                }
	                if (!"".equals(reportTitleField.getValue().toString())) {
	                    excelExport.setReportTitle(reportTitleField.getValue().toString());
	                }
	                if (!"".equals(exportFileNameField.getValue().toString())) {
	                    excelExport.setExportFileName(exportFileNameField.getValue().toString());
	                }
	                excelExport.setDisplayTotals(false);
	                excelExport.setRowHeaders(false);
	                excelExport.setExcelFormatOfProperty("date", "mm/dd/yyyy");
	                excelExport.setDoubleDataFormat("#0.00");
	                excelExport.export();
	            }
	        });

	        mainOptions.addComponent(regularExportButton);
	        mainOptions.setComponentAlignment(regularExportButton, Alignment.BOTTOM_LEFT);
	      //mainOptions.addComponent(endSpacer);
	        

	        VerticalLayout v1 = new VerticalLayout(new Label("<b><font size=\"4\">Export options: </font></b>",ContentMode.HTML),mainOptions);
	        v1.setMargin(false);
	        v1.setSizeFull();
	        
	        VerticalLayout v2 = new VerticalLayout(new Label("<b><font size=\"3\">Vista precia de los datos a exportar: </font></b>",ContentMode.HTML),productsTable);
	        v2.setMargin(false);
	        v2.setSizeFull();

	        // add to window
	        final VerticalLayout gridAndOptions = new VerticalLayout(v1,verticalSpacer,v2);
	        gridAndOptions.setSpacing(true);
	        gridAndOptions.setMargin(false);
	        gridAndOptions.setSizeFull();
	        

	        return gridAndOptions;
		}

		
		
		
		
		public ExcelMatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			java.util.Date lastUpdate = java.sql.Date.valueOf(reportSettings.getLastUpdate());
		//	java.util.Date endDate = java.sql.Date.valueOf(reportSettings.getEndDate());
			
			if (settings.getKeyType().equals("sku")) {
				products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), reportSettings.getCategories(),lastUpdate,reportSettings.getCompetitors());
			}else {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), reportSettings.getCategories(),lastUpdate,reportSettings.getCompetitors());
			}
			dataProvider = DataProvider.ofCollection(products);
			System.out.println("TOtal de productos: " + products.size());
			return this;
		}
		
	}

	@Autowired
	private ShowAllProductsService showAllProductsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired 
	private ReportsService reportsService;
	
	public Component createComponent(ReportSettings reportSettings) {
		return new ExcelMatrixReportLayout(reportSettings).load().init().layout();
	}

	
}
