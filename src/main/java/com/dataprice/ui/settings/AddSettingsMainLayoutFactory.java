package com.dataprice.ui.settings;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.hibernate.search.annotations.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.crawlers.Arome;
import com.dataprice.model.crawlers.Catalogue;
import com.dataprice.model.crawlers.Sanborns;
import com.dataprice.model.crawlers.Sears;
import com.dataprice.model.crawlers.utils.CrawlInfo;
import com.dataprice.model.entity.Country;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Retail;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.addcountryservice.AddCountryService;
import com.dataprice.service.addproductservice.AddProductService;
import com.dataprice.service.addretailservice.AddRetailService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifysettings.ModifySettingsService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.products.ComboBoxWithButton;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.InlineDateTimeField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class AddSettingsMainLayoutFactory implements UIComponentBuilder {
	

	 private List<String> sellers;
	 private Settings settings;	
	 
	private class AddSettingsMainLayout extends VerticalLayout implements Button.ClickListener, Upload.Receiver{
		
		private Label mainTittle;	
		private ComboBox sellersComboBox;
			
		private Slider slider;
		private Label vertvalue;	
		private Button saveButton;
		private Label subTittle;
		private Binder<Settings> binder;
		private Button loadRetailsButton;
		private Button loadCatalogueButton;
		private TextArea stopWords;
		
		private RadioButtonGroup<Integer> daysGroup;
		
		 public AddSettingsMainLayout init() {

			mainTittle = new Label("<b><font size=\"5\">Ajustes del Sistema </font></b>",ContentMode.HTML);	

			subTittle = new Label("<font size=\"2\">Selecciona los ajustes del sistema deseados. </font>",ContentMode.HTML);	
			
			sellersComboBox = new ComboBox("Vendedores disponibles:");
			sellersComboBox.setWidth("50%");
			sellersComboBox.setItems(sellers);		
     
			daysGroup = new RadioButtonGroup<>("Seleccione la última actualización (en días): ");
			daysGroup.setItems(1, 2,3,4,5,6,7);
			daysGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
			
	
			slider =  new Slider(1, 100);
			slider.setWidth("50%");
			slider.setCaption("Número máximo de productos similares mostrados: ");
			// Shows the value of the vertical slider
			vertvalue = new Label();

		
			slider.addValueChangeListener(event -> {
			 // float value = event.getValue().floatValue();
				double value = event.getValue();
				vertvalue.setValue(String.valueOf(value));
				/**
			    if (value==slider.getMax()) {
			    	Notification.show("Warning","Show all available related products",Type.WARNING_MESSAGE);
			    }else {
			    	vertvalue.setValue(String.valueOf(value));
			    }
			    */
			});
			
			stopWords = new TextArea("Palabras excluidas en búsqueda de similares:");
			stopWords.setPlaceholder("Escribe las palabras separadas por comma(,)");
			stopWords.setWidth("50%");
			stopWords.setHeight("100%");
			stopWords.setEnabled(true);			
			
			
			saveButton = new Button("Gurdar Ajustes");
			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			saveButton.addClickListener(this);
			saveButton.setWidth("100%");			
			
			loadRetailsButton = new Button("Cargar Ajustes Predefinidos");
			loadRetailsButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			loadRetailsButton.addClickListener(this);
			loadRetailsButton.setWidth("100%");
						
			loadCatalogueButton = new Button("Load Catalogue");
			loadCatalogueButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			loadCatalogueButton.addClickListener(this);
			loadCatalogueButton.setWidth("100%");
			
			binder = new Binder<>(Settings.class);
			return this;
			    
		    }
		 
		 
		 private Object openSubWindow(String string) {
			// TODO Auto-generated method stub
			return null;
		}


		public AddSettingsMainLayout bind() {
			 
				binder.forField(sellersComboBox)
				  .bind("mainSeller");
				
				binder.forField(slider)
				  .bind("numRetrieved");
				
				binder.forField(daysGroup)
				  .bind("lastUpdateInDays");
				
				binder.forField(stopWords)
				  .bind("stopWords");
				
				binder.readBean(settings);
			
				return this;
			}	
		 
		
		 public Component layout() {	
			 		 
			 
		    	setMargin(true);
		    
		    	/**
		    	InlineDateTimeField date=new InlineDateTimeField("Correr los bots diariamente a las:");
		    	date.setValue(LocalDateTime.now());
		    	date.setLocale(Locale.US);
		    	date.setResolution(DateTimeResolution.MINUTE);
		    	date.setStyleName("mytheme");
		   		*/    	
		    	HorizontalLayout hbuttons= new HorizontalLayout(saveButton,loadRetailsButton);//,loadCatalogueButton);
		    	hbuttons.setWidth("40%");
		    	
		    	FormLayout form = new FormLayout(sellersComboBox,daysGroup,slider,stopWords);//,f);//,selector);
		    	form.setWidth("100%");
		    	
		    	VerticalLayout vl = new VerticalLayout(mainTittle,subTittle,form,hbuttons);
		    	vl.setMargin(false);
		    	vl.setSizeFull();
				return vl;

		    }

		 
		 @Override
			public void buttonClick(ClickEvent event) {
			     if (event.getSource()==saveButton) {
	                	 edit();
			     } else  {
			    	 if (event.getSource()==loadRetailsButton) {
			    		 loadRetails();
			         } else  {
			    	     loadCatalogue();
			         }	     
			     }
			}
		 
		 private void loadCatalogue() {
				Task task = new Task();
				task.setSeed("https://www.sanborns.com.mx/categoria/130101/ella/");
				
				task.setTaskName("DAMA");
				
				Sanborns crawler = new Sanborns();
				List<CrawlInfo> productsInfo = crawler.getUrlsFromTask(task);
			
				/**
				System.out.println("Tamaño total de productos base descargados: " + productsInfo.size());
				int con = 1;
				for (CrawlInfo crawlInfo : productsInfo) {
					System.out.println("------------------------      " + con );
					List<Product> products = crawler.parseProductsFromUrl(crawlInfo, task);
					for (int i = 0; i<products.size();i++){	 
						 System.out.println(products.get(i));
		  			     addProductService.saveProduct(products.get(i));
			      	}
					con++;
				}
				*/
				
				for (CrawlInfo crawlInfo : productsInfo) {
				    Product p = crawler.parseProductFromURL(crawlInfo, task);
					System.out.println(p);
					addProductService.saveProduct(p);
				}
				
			
		}


		private void loadRetails() {
			 
			/**
			Country country = new Country();
			country.setCountryId(1);
			country.setCountryName("México");
			country.setCurrency("Peso MXN");
			country.setNickname("MX");
			addCountryService.saveCountry(country);	
			
			Retail retail1 = new Retail();
			retail1.setRetailId(1);
			retail1.setRetailName("Walmart(MarketPlace)");
			retail1.setCrawlerName("Walmart");
			retail1.setCountry(country);		
			addRetailService.saveRetail(retail1);
			
			Retail retail2 = new Retail();
			retail2.setRetailId(2);
			retail2.setRetailName("Liverpool");
			retail2.setCrawlerName("Liverpool");
			retail2.setCountry(country);		
			addRetailService.saveRetail(retail2);
			
			Retail retail3 = new Retail();
			retail3.setRetailId(3);
			retail3.setRetailName("Coppel");
			retail3.setCrawlerName("Coppel");
			retail3.setCountry(country);		
			addRetailService.saveRetail(retail3);
			
			Retail retail4 = new Retail();
			retail4.setRetailId(4);
			retail4.setRetailName("Chedraui");
			retail4.setCrawlerName("Chedraui");
			retail4.setCountry(country);		
			addRetailService.saveRetail(retail4);
			
			Retail retail5 = new Retail();
			retail5.setRetailId(5);
			retail5.setRetailName("Walmart(Super)");
			retail5.setCrawlerName("SuperWalmart");
			retail5.setCountry(country);		
			addRetailService.saveRetail(retail5);
			
			Retail retail6 = new Retail();
			retail6.setRetailId(6);
			retail6.setRetailName("Mercado Libre");
			retail6.setCrawlerName("MercadoLibre");
			retail6.setCountry(country);		
			addRetailService.saveRetail(retail6);
			
			
			Retail retail7 = new Retail();
			retail7.setRetailId(7);
			retail7.setRetailName("SuplementosFitness");
			retail7.setCrawlerName("SuplementosFitness");
			retail7.setCountry(country);		
			addRetailService.saveRetail(retail7);
			
			
			Retail retail8 = new Retail();
			retail8.setRetailId(8);
			retail8.setRetailName("NutritionDepot");
			retail8.setCrawlerName("NutritionDepot");
			retail8.setCountry(country);		
			addRetailService.saveRetail(retail8);
			
			
			Retail retail9 = new Retail();
			retail9.setRetailId(9);
			retail9.setRetailName("Arome");
			retail9.setCrawlerName("Arome");
			retail9.setCountry(country);		
			addRetailService.saveRetail(retail9);
			
			
			
			Retail retail10 = new Retail();
			retail10.setRetailId(10);
			retail10.setRetailName("Sanborns");
			retail10.setCrawlerName("Sanborns");
			retail10.setCountry(country);		
			addRetailService.saveRetail(retail10);
			
			
			Retail retail11 = new Retail();
			retail11.setRetailId(11);
			retail11.setRetailName("Amazon");
			retail11.setCrawlerName("Amazon");
			retail11.setCountry(country);		
			addRetailService.saveRetail(retail11);
			
			
			Retail retail12 = new Retail();
			retail12.setRetailId(12);
			retail12.setRetailName("ExpoPerfumes");
			retail12.setCrawlerName("ExpoPerfumes");
			retail12.setCountry(country);		
			addRetailService.saveRetail(retail12);	
			
			Retail retail13 = new Retail();
			retail13.setRetailId(13);
			retail13.setRetailName("Osom");
			retail13.setCrawlerName("Osom");
			retail13.setCountry(country);		
			addRetailService.saveRetail(retail13);
			
			
			Retail retail14 = new Retail();
			retail14.setRetailId(14);
			retail14.setRetailName("Sears");
			retail14.setCrawlerName("Sears");
			retail14.setCountry(country);		
			addRetailService.saveRetail(retail14);
			
			
			Retail retail15 = new Retail();
			retail15.setRetailId(15);
			retail15.setRetailName("Soriana");
			retail15.setCrawlerName("Soriana");
			retail15.setCountry(country);		
			addRetailService.saveRetail(retail15);
			
			
			Retail retail16 = new Retail();
			retail16.setRetailId(16);
			retail16.setRetailName("PerfumesOnline");
			retail16.setCrawlerName("PerfumesOnline");
			retail16.setCountry(country);		
			addRetailService.saveRetail(retail16);
			
			Retail retail17 = new Retail();
			retail17.setRetailId(17);
			retail17.setRetailName("PerfumesMexico");
			retail17.setCrawlerName("PerfumesMexico");
			retail17.setCountry(country);		
			addRetailService.saveRetail(retail17);
			
			Notification.show("RETAILS","Loaded of Retails Complete",Type.WARNING_MESSAGE);
			*/
			
			daysGroup.setValue(1);
			slider.setValue(10.0);
			stopWords.setValue("");
			
		}


		private void edit() {
			 
				try {
					binder.writeBean(settings);
				} catch (ValidationException e) {
					Notification.show("ERROR","Los ajustes no se guardaron",Type.ERROR_MESSAGE);
					return;
				}
				modifySettingsService.modifySettings(settings);
				Notification.show("GUARDAR","Ajustes guardados satisfactoriamente",Type.WARNING_MESSAGE);
			
			}
		
		 
		 private void clearField() {
				
		 }


		public AddSettingsMainLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			sellers = showAllProductsService.getSellersList();
			if (sellers.size()==0) {
				settings.setMainSeller(null); //In there are no sellers set to null!!
			}
			
			return this;
		}


		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			// TODO Auto-generated method stub
			return null;
		}

			 
		 
	}
	
	@Autowired
	private AddProductService addProductService;
	
	
	@Autowired 
	private AddCountryService addCountryService;
	
	@Autowired 
	private AddRetailService addRetailService;
	
	@Autowired 
	private ModifySettingsService modifySettingsService;
	
	@Autowired 
	private UserServiceImpl userServiceImpl;
	
	@Autowired 
	private ShowAllProductsService showAllProductsService;
	
	@Override
	public Component createComponent() {
		return new AddSettingsMainLayout().load().init().bind().layout();
	}
	
	
}
