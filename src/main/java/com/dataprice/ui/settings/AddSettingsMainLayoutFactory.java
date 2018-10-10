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
import com.dataprice.service.showallcountries.ShowAllCountriesService;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.products.ComboBoxWithButton;
import com.dataprice.ui.tasks.ShowAllTasksLayoutFactory;
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
	 private List<String> countries;
	 
	 private Settings settings;	
	 
	private class AddSettingsMainLayout extends VerticalLayout implements Button.ClickListener, Upload.Receiver{
		
		private Label mainTittle;	
		private ComboBox sellersComboBox;
		private ComboBox countriesComboBox;
		
		
		private Slider slider;
		private Label vertvalue;	
		private Button saveButton;
		private Label subTittle;
		private Binder<Settings> binder;
		private Button loadRetailsButton;
		private Button loadCatalogueButton;
		private TextArea stopWords;
		
		private RadioButtonGroup<Integer> daysGroup;
		private RadioButtonGroup<Integer> coresGroup;
		
		 public AddSettingsMainLayout init() {

			mainTittle = new Label("<b><font size=\"5\">Ajustes del Sistema </font></b>",ContentMode.HTML);	

			subTittle = new Label("<font size=\"2\">Selecciona los ajustes del sistema deseados. </font>",ContentMode.HTML);	
			
			sellersComboBox = new ComboBox("Vendedores disponibles:");
			sellersComboBox.setWidth("50%");
			sellersComboBox.setItems(sellers);
			
			
			countriesComboBox = new ComboBox("Paises disponibles:");
			countriesComboBox.setWidth("50%");
			countriesComboBox.setItems(countries);		
			
			
     
			daysGroup = new RadioButtonGroup<>("Seleccione la última actualización (en días): ");
			daysGroup.setItems(1, 2,3,4,5,6,7);
			daysGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
			
	
			coresGroup = new RadioButtonGroup<>("Seleccione en número de cores: ");
			coresGroup.setItems(1, 2,3,4,5,6,7);
			coresGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
			
			
			
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
				
				
				binder.forField(countriesComboBox)
				  .bind("countrySelected");
				
				
				binder.forField(slider)
				  .bind("numRetrieved");
				
				binder.forField(daysGroup)
				  .bind("lastUpdateInDays");
				
				binder.forField(coresGroup)
				  .bind("cores");
				
				
				
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
		    	HorizontalLayout hbuttons= new HorizontalLayout(saveButton);//,loadCatalogueButton);
		    	hbuttons.setWidth("40%");
		    	
		    	FormLayout form = new FormLayout(sellersComboBox,countriesComboBox,daysGroup,coresGroup,slider,stopWords);//,f);//,selector);
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
			     } 
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
			
			countries = showAllCountriesService.getAllCountriesNames();
			if (countries.size()==0) {
				settings.setCountrySelected(null); //In there are no sellers set to null!!
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
	private ShowAllCountriesService showAllCountriesService;
	
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
