package com.dataprice.ui.reports;


import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.ui.Notification.Type;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.products.Icon;
import com.dataprice.ui.reports.exporter.DefaultGridHolder;
import com.dataprice.ui.reports.exporter.ExcelExport;
import com.dataprice.ui.reports.exporter.TableHolder;
import com.dataprice.ui.reports.gridutil.GridUtil;
import com.dataprice.ui.reports.gridutil.cell.CellFilterChangedListener;
import com.dataprice.ui.reports.gridutil.cell.GridCellFilter;
import com.vaadin.annotations.Push;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;


@Push
@UIScope
@org.springframework.stereotype.Component
public class MatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	private GridCellFilter filter;
	
	private DecimalFormat df = new DecimalFormat("#0.00");

	private Icon upArrow;
	private Icon downArrow;
	private Icon equalArrow;
	
	
	private class MatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public MatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public MatrixReportLayout init() {
			
			upArrow = new Icon(VaadinIcons.ARROW_UP);
			upArrow.setRedColor();
			
			downArrow = new Icon(VaadinIcons.ARROW_DOWN);
			
			equalArrow = new Icon(VaadinIcons.SCALE);
			equalArrow.setBlueColor();
			
			
			productsTable = new Grid<>(Product.class);						  
			productsTable.removeAllColumns();

				
			if (settings.getKeyType().equals("sku")) {
				productsTable.addColumn(p -> p.getSku()).setCaption("Sku").setId("Mysku");
			}else {
				productsTable.addColumn(p -> p.getUpc()).setCaption("Upc").setId("Myupc");
			}
			
			productsTable.addColumn(p -> {
				Link  productLink =  new Link();
				productLink.setCaption(p.getName());
				ExternalResource externalResourceLink = new ExternalResource(p.getProductUrl());
				productLink.setResource(externalResourceLink);
				productLink.setTargetName("_blank");								
			    return productLink;
			}).setCaption("Nombre").setId("Myname").setRenderer(new ComponentRenderer());	
			
	        productsTable.addColumn(p -> p.getBrand()).setCaption("Brand").setId("Mybrand");
			
			productsTable.addColumn(p -> p.getCategory()).setCaption("Category").setId("Mycategory");
				
		//	productsTable.addColumn(p -> p.getPrice()).setCaption("Mi precio").setId("Myprice").setWidth(150);
		
			productsTable.addColumn(p -> p.getPrice()).setCaption("Mi precio").setId("Myprice").setRenderer(new NumberRenderer(df));
			
			
			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
				 productsTable.addComponentColumn(p -> {
					 List<Product> products = null;
					 if (settings.getKeyType().equals("sku")) {
						 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
						}else {
						 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
						}
				
					PositionIndicator positionIndicator = null;
			        if (products.size()!=0) {
			        	positionIndicator = new PositionIndicator(p.getPrice(),products.get(0).getPrice());
			        }
				    return positionIndicator;
				}).setCaption(seller).setId(seller);
				 
			}		
			
			
			productsTable.setWidth("100%");
			productsTable.setHeight("520px"); //500 antes
			productsTable.setItems(products);
			
		  
		    
			return this;
		}

		
		public MatrixReportLayout filter() {
			
			filter = new GridCellFilter(productsTable);
			filter.setLinkFilter("Myname", true, false);
			if (settings.getKeyType().equals("sku")) {
					filter.setTextFilter("Mysku", true, true);
			}else {
					filter.setTextFilter("Myupc", true, true);
			}
			filter.setTextFilter("Mybrand", true, true);
			filter.setTextFilter("Mycategory", true, true);				
			filter.setNumberFilter("Myprice", Double.class,"invalid input", "inferior", "superior");		
					
			for (String seller : reportSettings.getCompetitors()) {
					filter.setIndicatorFilter(seller);
			}
			
			return this;			
		}
		
		public MatrixReportLayout footer() {
			 final FooterRow footerRow = productsTable.appendFooterRow();
			 if (settings.getKeyType().equals("sku")) {
			        footerRow.getCell("Mysku")
	                .setHtml("Total:");
			 }else {
			        footerRow.getCell("Myupc")
	                .setHtml("Total:");
			}

			 final FooterCell footerCell = footerRow.join("Myname", "Mybrand", "Mycategory", "Myprice");
			 // inital total count
			 footerCell.setHtml("<b>" + products.size() + "</b>");
			 // filter change count recalculate
			 productsTable.getDataProvider().addDataProviderListener(event -> {
			 List<Product> data = event.getSource()
			      .fetch(new Query<>()).collect(Collectors.toList());
			       footerCell.setHtml("<b>" + data.size() + "</b>");
			  });
			return this;			
		}
		
		
		public MatrixReportLayout header() {
			
			 HeaderRow fistHeaderRow = productsTable.prependHeaderRow();
		        if (settings.getKeyType().equals("sku")) {
		        	  fistHeaderRow.join("Mysku", "Myname", "Mybrand");
				        fistHeaderRow.getCell("Mysku")
				                .setHtml("");
				}else {
					  fistHeaderRow.join("Mysku", "Myname", "Mybrand");
				        fistHeaderRow.getCell("Myupc")
				                .setHtml("");
				}
		        
		      
		        HeaderCell join = fistHeaderRow.join("Mycategory", "Myprice");
		        HorizontalLayout buttonLayout = new HorizontalLayout();
		        buttonLayout.setSpacing(true);
		        join.setComponent(buttonLayout);
		        Button clearAllFilters = new Button("Limpiar Filtros", event -> filter.clearAllFilters());
		        clearAllFilters.setIcon(VaadinIcons.CLOSE);
		        clearAllFilters.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		        buttonLayout.addComponent(clearAllFilters);

		        Button changeVisibility = new Button("Exportar a Excel");
		        changeVisibility.addClickListener(new Button.ClickListener() {
		        	 private static final long serialVersionUID = -73954695086117200L;		        	  
		        	 final TableHolder tableHolder = new DefaultGridHolder(productsTable);
			         private ExcelExport excelExport;
		            @Override
		            public void buttonClick(final ClickEvent event) {
		               //Do the exporting stuff
		               System.out.println("Hola me llamo Rene");
	
	                excelExport = new ExcelExport(tableHolder);
	                excelExport.setDisplayTotals(false);
	                excelExport.setRowHeaders(false);
	             // excelExport.setExcelFormatOfProperty("date", "mm/dd/yyyy");
	             // excelExport.setDoubleDataFormat("#0.00");
	                excelExport.export();
	                
	                
		            }
		        });
		        changeVisibility.setIcon(VaadinIcons.CLOUD_DOWNLOAD);
		        changeVisibility.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		        buttonLayout.addComponent(changeVisibility);


		        // listener's on filter
		      
		        filter.addCellFilterChangedListener(new CellFilterChangedListener() {

		            @Override
		            public void changedFilter(final GridCellFilter cellFilter) {
		                Notification.show("Filtro ejecutado" , Type.TRAY_NOTIFICATION);
		            }
		        });
		        
			return this;			
		}
		
		
		public Component layout() {
			
			HorizontalLayout f1 = new HorizontalLayout(upArrow, new Label("Soy más caro"));
			f1.setMargin(false);
			
			HorizontalLayout f2 = new HorizontalLayout(equalArrow, new Label("Tengo mismo precio"));
			f1.setMargin(false);
			
			HorizontalLayout f3 = new HorizontalLayout(downArrow, new Label("Soy más barato"));
			f1.setMargin(false);
			
			HorizontalLayout h1 = new HorizontalLayout(f1,f2,f3);
			h1.setWidth("50%");
			h1.setMargin(false);
			
			VerticalLayout v1 = new VerticalLayout(h1,productsTable);
			v1.setComponentAlignment(h1, Alignment.MIDDLE_RIGHT);
			v1.setSizeFull();
			v1.setMargin(false);
			
	        return v1;
		}

		public MatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			java.util.Date lastUpdate = java.sql.Date.valueOf(reportSettings.getLastUpdate());
			
			if (settings.getKeyType().equals("sku")) {
				products = reportsService.getProductsForPriceMatrixBySku(settings.getMainSeller(), lastUpdate,reportSettings.getCompetitors());
			}else {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), lastUpdate,reportSettings.getCompetitors());
			}
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
		return new MatrixReportLayout(reportSettings).load().init().filter().footer().header().layout();
	}

	
}
