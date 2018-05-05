package com.dataprice.ui.feed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.FeedSettings;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ReportSettings;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.reports.ReportsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.reports.exporter.CsvExport;
import com.dataprice.ui.reports.exporter.DefaultGridHolder;
import com.dataprice.ui.reports.exporter.ExcelExport;
import com.dataprice.ui.reports.exporter.TableHolder;
import com.dataprice.ui.reports.gridutil.cell.CellFilterChangedListener;
import com.dataprice.ui.reports.gridutil.cell.GridCellFilter;
import com.vaadin.annotations.Push;
import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

@Push
@UIScope
@org.springframework.stereotype.Component
public class ExportFeedLayoutFactory {
	
	private List<String> competitors;
	private List<Product> products;
	private Settings settings;
	private ProgressBar csvProgressBar;
	private Button csvExportButton;
	
	

	
	private class ExportFeedLayout extends VerticalLayout  {
		
		private FeedSettings feedSettings;
		private Grid<Product> productsTable;

		public ExportFeedLayout(FeedSettings feedSettings) {
			this.feedSettings = feedSettings;
		}

		

		public ExportFeedLayout init() {
			
			
			csvProgressBar = new ProgressBar();
			csvProgressBar.setCaption("Procesando...");
			csvProgressBar.setIndeterminate(true);
			csvProgressBar.setVisible(false);
			
			
			productsTable = new Grid<>(Product.class);						  
			productsTable.removeAllColumns();
				
			 if (settings.getKeyType().equals("sku")) {
				productsTable.addColumn(p -> p.getSku()).setCaption("Pid").setId("Mysku");
			 }else {
				productsTable.addColumn(p -> p.getUpc()).setCaption("Pid").setId("Myupc");
			 }
			
			 productsTable.addColumn(p -> p.getName()).setCaption("Name").setId("Myname");
				
			 productsTable.addColumn(p -> p.getDescription()).setCaption("Description").setId("Mydescription");
			 
			 productsTable.addColumn(p -> "").setCaption("Short_desc").setId("Myshort");

			 productsTable.addColumn(p -> p.getProductUrl()).setCaption("Url").setId("Myurl");

			 productsTable.addColumn(p -> p.getCategory()).setCaption("Category").setId("Mycategory");

			 productsTable.addColumn(p -> "").setCaption("Tags").setId("Mytags");
			
			 productsTable.addColumn(p -> p.getBrand()).setCaption("Brand").setId("Mybrand");
			
			 productsTable.addColumn(p -> p.getPrice()).setCaption("Price").setId("Myprice");
			
			 productsTable.addColumn(p -> "").setCaption("Shipping").setId("Myshipping");
			
			 productsTable.addColumn(p -> "").setCaption("Shipping_coment").setId("Myshippingcomment");
			
			 productsTable.addColumn(p -> p.getImageUrl()).setCaption("Image").setId("Myimage");
			 
			 productsTable.setWidth("100%");
			 productsTable.setHeight("500px"); //500 antes
			 productsTable.setItems(products);
				
			 
			 
			 csvExportButton =  new Button("Descargar en CSV");
			 
			 
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
		        
		        
			
			return this;
		}

		public Component layout() {
			VerticalLayout v1 = new VerticalLayout(csvExportButton,csvProgressBar,productsTable);
			v1.setSizeFull();
			v1.setMargin(false);
			
			
			return v1;
		}

		
		
		/**
		public ExportFeedLayout footer() {
			
			 final FooterRow footerRow = productsTable.appendFooterRow();
			 if (settings.getKeyType().equals("sku")) {
			        footerRow.getCell("Mysku")
	                .setHtml("Total:");
			 }else {
			        footerRow.getCell("Myupc")
	                .setHtml("Total:");
			}

			 final FooterCell footerCell = footerRow.join("Myname");
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
		
		public ExportFeedLayout header() {
			
			 HeaderRow fistHeaderRow = productsTable.prependHeaderRow();
		        if (settings.getKeyType().equals("sku")) {
		        	  fistHeaderRow.join("Mysku", "Myname");
				        fistHeaderRow.getCell("Mysku")
				                .setHtml("");
				}else {
					  fistHeaderRow.join("Mysku", "Myname");
				        fistHeaderRow.getCell("Myupc")
				                .setHtml("");
				}
		        
		      
		        HeaderCell join = fistHeaderRow.join("Mydescription");
		        HorizontalLayout buttonLayout = new HorizontalLayout();
		        buttonLayout.setSpacing(true);
		        join.setComponent(buttonLayout);
		     
	        
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
		        
		        buttonLayout.addComponent(csvExportButton);
		        buttonLayout.addComponent(csvProgressBar);


		        
			return this;	
		}
		*/
		
		
		
       public ExportFeedLayout load() {
			
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			java.util.Date lastUpdate = java.sql.Date.valueOf(feedSettings.getLastUpdate());
			competitors = reportsService.getCompetitorsList(feedSettings.getSeller());
			Set<String> competitorsSet = new HashSet<String>(competitors);
			if (settings.getKeyType().equals("sku")) {
				products = reportsService.getProductsForPriceMatrixBySku(feedSettings.getSeller(), lastUpdate,competitorsSet);
			}else {
				products = reportsService.getProductsForPriceMatrixByUpc(settings.getMainSeller(), lastUpdate,competitorsSet);
			}
			return this;
		}

		
       
       
		
	}
	
	@Autowired
	private VaadinHybridMenuUI vaadinHybridMenuUI;
	
	@Autowired 
	private ReportsService reportsService;				
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	
	
	public Component createComponent(FeedSettings feedSettings) {
		return new ExportFeedLayout(feedSettings).load().init().layout();
	}
	

}
