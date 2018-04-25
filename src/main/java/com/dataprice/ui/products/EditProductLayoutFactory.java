package com.dataprice.ui.products;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.searchproduct.SearchProductService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.utils.NotificationsMessages;
import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.annotations.Push;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

@Push
@UIScope
@org.springframework.stereotype.Component
public class EditProductLayoutFactory {

   private Settings settings;
	
   //Product details
   private TextField textFieldName;	
   private TextField textFieldId;
   private TextField textFieldRetail;
   private TextField textFieldPrice;
   private TextField textFieldBrand;
   private TextField textFieldUpdateDate;
   private TextArea description;
   private Image productImage;
   private Link  productLink;
   
   private TextField textFieldKey;
   private CheckBox productChecked;
   //Buttons 
   private Button editButton;
   
   private Product product;
 
   private Grid<Product> topProductsTable;  ///IMPORTANT!!!! I also have to update this table when edit is cliked!.

   private class EditProductLayout extends VerticalLayout implements Button.ClickListener{

		private ProductSaveListener productSaveListener;
		private Binder<Product> binder;
		 
		public EditProductLayout(ProductSaveListener productSaveListener) {
			this.productSaveListener = productSaveListener;
		}
		
		public EditProductLayout init() {
			
			textFieldName = new TextField("Nombre:");
			textFieldName.setWidth("100%");
			textFieldName.setEnabled(false);
			
			textFieldId = new TextField("Id:");
			textFieldId.setWidth("100%");
			textFieldId.setEnabled(false);
			
			textFieldRetail = new TextField("Retail:");
			textFieldRetail.setWidth("100%");
			textFieldRetail.setEnabled(false);
			
			textFieldPrice = new TextField("Price:");
			textFieldPrice.setWidth("100%");
			textFieldPrice.setEnabled(false);
			
			textFieldBrand = new TextField("Marca:");
			textFieldBrand.setWidth("100%");
			textFieldBrand.setEnabled(false);
			
			textFieldUpdateDate = new TextField("Actualizado:");
			textFieldUpdateDate.setWidth("100%");
			textFieldUpdateDate.setEnabled(false);
			
			//Key condition
			if (settings.getKeyType().equals("sku")) {
				textFieldKey = new TextField("SKU:");
			}else {
				textFieldKey = new TextField("UPC:");
            } 
	
			textFieldKey.setWidth("100%");
	
			productChecked = new CheckBox("Marcar producto como revisado.");
			productChecked.setValue(true);
			
		   	binder = new Binder<>(Product.class);		   	
		
			editButton = new Button("Actualizar la base de datos");
			editButton.setIcon(VaadinIcons.EDIT);
			editButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			editButton.addClickListener(this);
			editButton.setWidth("100%");
					
			productImage = new Image();
			productImage.setWidth("100%");
			productImage.setHeight("100%");
	   
			description = new TextArea();
			description.setWidth("100%");
			description.setHeight("100%");
			description.setEnabled(false);
			
			productLink =  new Link();
			productLink.setCaption("Go to website.");
		
			topProductsTable = new Grid<>(Product.class);
			
			topProductsTable.removeAllColumns();
			
			topProductsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(100,Unit.PIXELS);
			    image.setHeight(100,Unit.PIXELS);

			    return image;
			}).setCaption("Imagen");
			
			topProductsTable.addColumn(p ->
		      p.getSeller()).setCaption("Competencia");
			
			topProductsTable.addColumn(p ->
		      p.getName()).setCaption("Nombre");
			
			//Key condition
			if (settings.getKeyType().equals("sku")) {
				topProductsTable.addColumn(p ->
			      p.getSku()).setCaption("SKU");
			}else {
				topProductsTable.addColumn(p ->
			      p.getUpc()).setCaption("UPC");
			} 
	
			topProductsTable.addColumn(p ->
		      p.getPrice()).setCaption("Precio");
				
			topProductsTable.addColumn(p ->
		      "<a target=\"_blank\" href='" + p.getProductUrl() + "' target='_top'>check me!</a>",
		      new HtmlRenderer()).setCaption("Links");
			
			topProductsTable.setSelectionMode(SelectionMode.MULTI);
			
			/**
			topProductsTable.removeColumn("imageUrl");
			topProductsTable.removeColumn("productUrl");
			topProductsTable.removeColumn("productKey");
			topProductsTable.removeColumn("brand");
			topProductsTable.removeColumn("task");
			topProductsTable.removeColumn("name");
			topProductsTable.removeColumn("upc");
			topProductsTable.removeColumn("sku");
			topProductsTable.removeColumn("price");
			topProductsTable.removeColumn("productId");
			topProductsTable.removeColumn("description");
			*/
			
			topProductsTable.setBodyRowHeight(100);
			topProductsTable.setVisible(true);
			topProductsTable.setWidth("100%");

			
			return this;
			
		}

		 private void clearField() {

			textFieldKey.setValue("");
			
		 }
			
		public EditProductLayout load() {
			product = null;

			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			return this;
		}


		public Component layout() {
						
			FormLayout formLayout2 = new FormLayout(textFieldId,textFieldRetail,textFieldPrice,textFieldBrand,textFieldUpdateDate);
			formLayout2.setWidth("100%");
			formLayout2.setMargin(false);
			
			VerticalLayout vlform2 = new VerticalLayout(formLayout2);
			vlform2.setWidth("100%");
			vlform2.setExpandRatio(formLayout2, 1);
			vlform2.setMargin(false); //era true

			
			Label descr = new Label("descrip");
			descr.setValue("Descripción:");
			descr.setWidth("100%");
			descr.setHeight("10px");
			VerticalLayout vlform4 = new VerticalLayout(descr,description);
			vlform4.setWidth("90%");
			vlform4.setHeight("100%");
			vlform4.setMargin(false);
			vlform4.setExpandRatio(description, 1);

			
			Label img = new Label("img");
			img.setValue("Imagen:");
			img.setWidth("100%");
			//img.setHeight("10px");
			HorizontalLayout himg = new HorizontalLayout(img,productLink);
			himg.setWidth("100%");
			himg.setHeight("10px");
			himg.setSpacing(true);
			himg.setComponentAlignment(img, Alignment.TOP_LEFT);
			productLink.setWidth("100%");
			himg.setComponentAlignment(productLink, Alignment.TOP_RIGHT);
			VerticalLayout vlform5 = new VerticalLayout(himg,productImage);
			vlform5.setWidth("90%"); //era 90
			vlform5.setHeight("100%");
			vlform5.setMargin(false);
			vlform5.setExpandRatio(productImage, 1);
			
			
			HorizontalLayout hl = new HorizontalLayout(vlform2,vlform4,vlform5);
			hl.setMargin(false);
			hl.setSpacing(true);
			hl.setWidth("100%");
			hl.setComponentAlignment(vlform4, Alignment.TOP_RIGHT);
			hl.setComponentAlignment(vlform5, Alignment.TOP_RIGHT);
			

			FormLayout formLayout = new FormLayout(textFieldName);
			formLayout.setWidth("100%");
			formLayout.setMargin(false);
			
			editButton.setWidth("100%");
			
			VerticalLayout v3 = new VerticalLayout(topProductsTable);
			v3.setSizeFull();
			v3.setMargin(new MarginInfo(true, false, false, false));
					
			
			FormLayout formLayoutKey = new FormLayout(textFieldKey);
			formLayoutKey.setWidth("100%");
			formLayoutKey.setMargin(false);
			
			
			HorizontalLayout hKey = new HorizontalLayout(formLayoutKey,productChecked);	
			hKey.setComponentAlignment(productChecked, Alignment.BOTTOM_CENTER);
			hKey.setWidth("50%");
			hKey.setMargin(new MarginInfo(true, false, false, false));
									
			HorizontalLayout h1 = new HorizontalLayout(editButton);
			h1.setMargin(false);
			h1.setWidth("25%");
			
			VerticalLayout vl = new VerticalLayout(formLayout,hl,v3,hKey,h1);
			vl.setComponentAlignment(h1, Alignment.TOP_LEFT);
			vl.setWidth("100%");
			vl.setMargin(false);
			return vl;
		}
			
		
		@Override
		public void buttonClick(ClickEvent event) {
		     edit();
        }	
				
		private void edit() {
	
			if (product==null){
				Notification.show("ERROR","Please select a product first",Type.ERROR_MESSAGE);
				return;
			}
										
			BinderValidationStatus<Product> status = binder.validate();

			if (status.hasErrors()) {
				Notification.show(NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_TITLE.getString(),NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_DESCRIPTION.getString(),Type.ERROR_MESSAGE);
			   return;
			}
			
			try {
				binder.writeBean(product);
			} catch (ValidationException e) {
				Notification.show(NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_TITLE.getString(),NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_DESCRIPTION.getString(),Type.ERROR_MESSAGE);
				return;
			}
			
			modifyProductService.modifyProduct(product);
			
			
			//Propagate the profile
			if (topProductsTable.getSelectedItems().size()!=0) {
				for (Product p : topProductsTable.getSelectedItems()) {
					p.setSku(product.getSku());
					p.setUpc(product.getUpc());
					modifyProductService.modifyProduct(p);
				}
			}
			
			productSaveListener.productSaved();
		//	editData(product); //In order to refreh the topProductsTable
			clearField();
			Notification.show("EDIT","Product profile editted",Type.WARNING_MESSAGE);
			
		}


		public EditProductLayout bind() {
			binder.forField(textFieldKey)
			//  .asRequired("Key must be set")
			  .bind(settings.getKeyType()); //sku or upc from product

			binder.forField(productChecked)
			  .bind("checked");
			
			
			binder.readBean(product);
			
			return this;
		}
		
				
	}
   
	@Autowired 
	private UserServiceImpl userServiceImpl;
   
    @Autowired
    private VaadinHybridMenuUI vaadinHybridMenuUI;

    @Autowired
    private ShowAllProductsService showAllProductsService;
   
    
    @Autowired
    private ModifyProductService modifyProductService;
   
   
	@Autowired
	private SearchProductService searchProductService;

   
   public Component createComponent(ProductSaveListener productSaveListener) {
    		return new EditProductLayout(productSaveListener).load().init().bind().layout();
    }
    	
    	
	public void editData(Object item) {
		product = (Product) item;
		
		//Key condition		
		if (settings.getKeyType().equals("sku")) {
			if (product.getSku()!=null) {
				textFieldKey.setValue(product.getSku());
			}else {
				textFieldKey.setValue("");
			}
		}else {
			if (product.getUpc()!=null) {
				textFieldKey.setValue(product.getUpc());
			}else {
				textFieldKey.setValue("");
			}
		} 

		//Set topProductsTable
		String searchQuery = product.getName() + product.getBrand() + product.getSku();
		
		List<Product> retrieveList = searchProductService.search(searchQuery);
		
		if (retrieveList.size()!=0) { //Do this only if products related are found
		boolean remove = retrieveList.remove(product);
		String sellerSelected = product.getSeller();
		List<Product> retrieveFilteredList = new LinkedList<Product>();
		List<Product> retrieveFilteredAndMatchedList = new LinkedList<Product>();
		int con = 0;
		
		for (Product p : retrieveList) {
			if (!p.getSeller().equals(sellerSelected)) { //From different store
			    retrieveFilteredList.add(p);
			    if (p.getUpc().equals(product.getUpc())) { //Store those with same key!!
			    	retrieveFilteredAndMatchedList.add(p);
			    }
			}
			con++;
			if (con>settings.getNumRetrieved()) {
			    break;	
			}
		}
	
		topProductsTable.setItems(retrieveFilteredList);
		
		//Select all with coincidences
		for (Product p : retrieveFilteredAndMatchedList) {
			topProductsTable.select(p);
		}
		
		}   
		   
		//Set product name
		textFieldName.setValue(product.getName());
		
		//Set product pid
		textFieldId.setValue(product.getProductId());
		
		//Set product precio
		textFieldPrice.setValue(product.getPrice().toString());
				
		//Set product precio
		textFieldRetail.setValue(product.getTask().getRetail().getRetailName());
		
		//Set product taskname
		textFieldBrand.setValue(product.getBrand());
		
		//Set product date
		textFieldUpdateDate.setValue(product.getUpdateDay().toString());
				
		//Set product image
		ExternalResource externalResource = new ExternalResource(product.getImageUrl());
		productImage.setSource(externalResource);
		
		//Set description
		
		description.setValue(product.getDescription());
		
		//Set product link
		
		ExternalResource externalResourceLink = new ExternalResource(product.getProductUrl());
		productLink.setResource(externalResourceLink);
		productLink.setTargetName("_blank");
		
		//Always put value as true.		
		productChecked.setValue(true);

	}
	
	
		
}