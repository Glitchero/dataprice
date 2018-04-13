package com.dataprice.ui.settings;

import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifysettings.ModifySettingsService;
import com.dataprice.service.removegender.RemoveGenderService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.products.ComboBoxWithButton;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
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
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class AddSettingsMainLayoutFactory implements UIComponentBuilder {
	

	 private List<String> sellers;
	 private Settings settings;	
	 
	private class AddSettingsMainLayout extends VerticalLayout implements Button.ClickListener, Upload.Receiver{
		
		private Label mainTittle;	
		private Label dataTittle;	
		private ComboBoxWithButton sellersComboBoxWithUpload;
		private RadioButtonGroup<String> keyGroup;	
		private Slider slider;
		private Label vertvalue;	
		private TextField postalCode;
		private Button saveButton;
		private Button defaultSetingsButton; 
		private Label separator1;
		private Binder<Settings> binder;
	 
		
		 public AddSettingsMainLayout init() {

		
			separator1 = new Label("<hr />",ContentMode.HTML);	
			separator1.setWidth("100%");
			
		   
			mainTittle = new Label("<b><font size=\"5\">List of settings: </font></b>",ContentMode.HTML);	
			
          
			dataTittle = new Label("<b>Elija la fuente de datos: </b>",ContentMode.HTML);
		
			sellersComboBoxWithUpload = new ComboBoxWithButton("Vendedores disponibles: ", VaadinIcons.CLOUD_UPLOAD,
	                onClick -> openSubWindow("Upload"));
			sellersComboBoxWithUpload.setWidth("50%");
			sellersComboBoxWithUpload.getComboBox().setItems(sellers);
		
		
 
			keyGroup = new RadioButtonGroup<>("Seleccione el tipo de clave: ");
			keyGroup.setItems("Modelo/sku", "Código de barras");
			keyGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
			
     
	
			slider =  new Slider(1, 100);
			slider.setWidth("50%");
			slider.setCaption("Número máximo de productos similares mostrados: ");
			// Shows the value of the vertical slider
			vertvalue = new Label();


			slider.addValueChangeListener(event -> {
			    float value = event.getValue().floatValue();
			    vertvalue.setValue(String.valueOf(value));
			});
			
			
			

			postalCode = new TextField("Código postal: ");
			postalCode.setWidth("50%");
			
	
			
			saveButton = new Button("Save Settings");
			saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			saveButton.addClickListener(this);
			saveButton.setWidth("100%");
			
			defaultSetingsButton = new Button("Restore default values");
			defaultSetingsButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			defaultSetingsButton.addClickListener(this);
			defaultSetingsButton.setWidth("100%");
			
			binder = new Binder<>(Settings.class);
			return this;
			    
		    }
		 
		 
		 private Object openSubWindow(String string) {
			// TODO Auto-generated method stub
			return null;
		}


		public AddSettingsMainLayout bind() {
			 
				binder.forField(sellersComboBoxWithUpload.getComboBox())
				  .bind("mainSeller");
				
				binder.forField(slider)
				  .bind("numRetrieved");
				
				binder.forField(keyGroup)
				  .bind("keyType");
				
				binder.readBean(settings);
			
				return this;
			}	
		 
		 
		 public Component layout() {		
		    	setMargin(true);
				

		    	
		    	HorizontalLayout hbuttons= new HorizontalLayout(saveButton,defaultSetingsButton);
		    	hbuttons.setWidth("40%");
		  
		    	
		    	FormLayout form = new FormLayout(sellersComboBoxWithUpload,keyGroup,slider,postalCode);
		    	form.setWidth("100%");
		    	
		    	VerticalLayout vl = new VerticalLayout(mainTittle,separator1,form,hbuttons);
		    	vl.setMargin(false);
		    	vl.setSizeFull();
				return vl;

		    }

		 
		 @Override
			public void buttonClick(ClickEvent event) {
	                	 edit();
			}
		 
		 private void edit() {
			 
				try {
					binder.writeBean(settings);
				} catch (ValidationException e) {
					Notification.show("ERROR","Gender is not saved",Type.ERROR_MESSAGE);
					return;
				}
				modifySettingsService.modifySettings(settings);
				Notification.show("SAVE","Settings is saved",Type.WARNING_MESSAGE);
			
			}
		
		 
		 private void clearField() {
				
		 }


		public AddSettingsMainLayout load() {
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			sellers = showAllProductsService.getSellersList();
			
			return this;
		}


		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			// TODO Auto-generated method stub
			return null;
		}

			 
		 
	}
	
	
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
