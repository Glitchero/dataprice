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
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.products.Icon;
import com.dataprice.ui.reports.exporter.CsvExport;
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
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
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
import com.vaadin.ui.ProgressBar;
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
public class RawMatrixReportLayoutFactory {

	private List<Product> products;
	private Grid<Product> productsTable;
	private Settings settings;
	private GridCellFilter filter;
	private ProgressBar excelProgressBar;
	private ProgressBar csvProgressBar;
	
	//private DecimalFormat df = new DecimalFormat("#0.00");
	private DecimalFormat df = new DecimalFormat("####,###,###.00");
	
	
	private Icon upArrow;
	private Icon downArrow;
	private Icon equalArrow;
	
	
	private class MatrixReportLayout extends VerticalLayout  {

		private ReportSettings reportSettings;
		
		public MatrixReportLayout(ReportSettings reportSettings) {
			this.reportSettings = reportSettings;
		}

		public MatrixReportLayout init() {
			excelProgressBar = new ProgressBar();
			excelProgressBar.setCaption("Procesando...");
			excelProgressBar.setIndeterminate(true);
			excelProgressBar.setVisible(false);
			
			csvProgressBar = new ProgressBar();
			csvProgressBar.setCaption("Procesando...");
			csvProgressBar.setIndeterminate(true);
			csvProgressBar.setVisible(false);
			
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
				MyLink  productLink =  new MyLink(p.getName());
				productLink.setCaption(p.getName());
				ExternalResource externalResourceLink = new ExternalResource(p.getProductUrl());
				productLink.setResource(externalResourceLink);
				productLink.setTargetName("_blank");								
			    return productLink;
			}).setCaption("Nombre").setId("Myname").setRenderer(new ComponentRenderer());	
			
	        productsTable.addColumn(p -> p.getBrand()).setCaption("Brand").setId("Mybrand");
			
			productsTable.addColumn(p -> p.getCategory()).setCaption("Category").setId("Mycategory");
					
			productsTable.addColumn(p -> p.getPrice()).setCaption("Mi precio").setId("Myprice").setWidth(150).setRenderer(new NumberRenderer(df));			
			
			for (String seller : reportSettings.getCompetitors()) {  //Competition
				 
				 productsTable.addColumn(p -> {
					 List<Product> products = null;
					 if (settings.getKeyType().equals("sku")) {
						 products =showAllProductsService.getProductsFromSellerNameAndSku(seller, p.getSku());
						}else {
						 products =showAllProductsService.getProductsFromSellerNameAndUpc(seller, p.getUpc());
						}
					 Double rivalPrice = null;
			        if (products.size()!=0) {
			        	rivalPrice = products.get(0).getPrice();
			        }
				    return rivalPrice;
				}).setCaption(seller).setId(seller).setWidth(150).setRenderer(new NumberRenderer(df));
			  
			}	
						
			productsTable.setWidth("100%");
			productsTable.setHeight("500px"); //500 antes
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
				filter.setNumberFilter(seller, Double.class,"invalid input", "inferior", "superior");		
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
		        	  fistHeaderRow.join("Mysku", "Myname");
				        fistHeaderRow.getCell("Mysku")
				                .setHtml("");
				}else {
					  fistHeaderRow.join("Myupc", "Myname");
				        fistHeaderRow.getCell("Myupc")
				                .setHtml("");
				}
		        
		      
		        HeaderCell join = fistHeaderRow.join("Mybrand","Mycategory","Myprice");
		        HorizontalLayout buttonLayout = new HorizontalLayout();
		        buttonLayout.setSpacing(true);
		        join.setComponent(buttonLayout);
		        Button clearAllFilters = new Button("Limpiar Filtros", event -> filter.clearAllFilters());
		        clearAllFilters.setIcon(VaadinIcons.CLOSE);
		        clearAllFilters.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		        buttonLayout.addComponent(clearAllFilters);

		        //Excel exporting section!!
		        Button excelExportButton = new Button("Exportar a Excel");
		        excelExportButton.addClickListener(new Button.ClickListener() {
		        	
		        	
		        	 private static final long serialVersionUID = -73954695086117200L;		        	  
		        	 final TableHolder tableHolder = new DefaultGridHolder(productsTable);
			         private ExcelExport excelExport;
		         
			         
		            @Override
		            public void buttonClick(final ClickEvent event) {
		               //Do the exporting stuff
		            	excelExportInNewThread();
		            /**
	                excelExport = new ExcelExport(tableHolder);
	                excelExport.setDisplayTotals(false);
	                excelExport.setRowHeaders(false);
	             // excelExport.setExcelFormatOfProperty("date", "mm/dd/yyyy");
	             // excelExport.setDoubleDataFormat("#0.00");
	                excelExport.export(); 
	                */        
		            }
		            
					private void excelExportInNewThread() {
						   new Thread(() -> {
					        	vaadinHybridMenuUI.access(() -> {
					        		excelProgressBar.setVisible(true);
					        		excelExportButton.setVisible(false);
					            });
					        						        		     
				                excelExport = new ExcelExport(tableHolder);
				                excelExport.setReportTitle("Reporte de Precios");
				                excelExport.setExportFileName("Excel-Report.xls");
				                excelExport.setDisplayTotals(false);
				                excelExport.setRowHeaders(false);							           				                
				                excelExport.export(); 
				               
					        	vaadinHybridMenuUI.access(() -> {
					        		excelProgressBar.setVisible(false);
					        		excelExportButton.setVisible(true);
					            });
					        }).start();	
					}
					
					
		        });
		        
		        final Resource excelExportIcon = FontAwesome.FILE_EXCEL_O;
		        excelExportButton.setIcon(excelExportIcon);
		        excelExportButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		        
		        //CSV exporting section!!
		        Button csvExportButton = new Button("Exportar a CSV");
		        csvExportButton.addClickListener(new Button.ClickListener() {
		        	
		        	
		        	 private static final long serialVersionUID = -73954695086117200L;		        	  
		        	 final TableHolder tableHolder = new DefaultGridHolder(productsTable);
			         private CsvExport csvExport;
		         
			         
		            @Override
		            public void buttonClick(final ClickEvent event) {
		               //Do the exporting stuff
		            	csvExportInNewThread(); 
		            }
		            
					private void csvExportInNewThread() {
						   new Thread(() -> {
					        	vaadinHybridMenuUI.access(() -> {
					        		csvProgressBar.setVisible(true);
					        		csvExportButton.setVisible(false);
					            });
					        	
					        	csvExport = new CsvExport(tableHolder);
					        	csvExport.setExportFileName("CSV-Report.csv");
					        	csvExport.setDisplayTotals(false);
				                csvExport.setRowHeaders(false);
				                csvExport.export(); 
				                
					        	vaadinHybridMenuUI.access(() -> {
					        		csvProgressBar.setVisible(false);
					        		csvExportButton.setVisible(true);
					            });
					        }).start();	
					}
					
					
		        });
		        
		        final Resource csvExportIcon = FontAwesome.FILE_TEXT_O;
		        csvExportButton.setIcon(csvExportIcon);
		        csvExportButton.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		       
		        //Add buttons to Layout
		    
		        buttonLayout.addComponent(excelExportButton);
		        buttonLayout.addComponent(excelProgressBar);
		        
		        buttonLayout.addComponent(csvExportButton);
		        buttonLayout.addComponent(csvProgressBar);

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
			
			HorizontalLayout f2 = new HorizontalLayout(equalArrow, new Label("Mismo precio"));
			f1.setMargin(false);
			
			HorizontalLayout f3 = new HorizontalLayout(downArrow, new Label("Soy más barato"));
			f1.setMargin(false);
			
			HorizontalLayout h1 = new HorizontalLayout(f1,f2,f3);
			h1.setWidth("35%");
			h1.setMargin(false);
			
			VerticalLayout v1 = new VerticalLayout(h1,productsTable);
			v1.setComponentAlignment(h1, Alignment.MIDDLE_RIGHT);
			v1.setSizeFull();
			v1.setMargin(false);
			
	        return v1;
		}

		public MatrixReportLayout load() {
			User user = userServiceImpl.getUserByUsername("admin");
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
	private VaadinHybridMenuUI vaadinHybridMenuUI;

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
