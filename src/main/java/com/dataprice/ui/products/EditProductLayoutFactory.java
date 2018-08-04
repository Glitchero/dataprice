package com.dataprice.ui.products;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.crawlers.utils.Configuration;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.ProductEquivalences;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.addproductequivservice.AddProductEquivService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.removeproductequivalency.RemoveProductEquivalencyService;
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
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.components.grid.SingleSelectionModel;
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
   private TextField textFieldTaskName;
   
   private TextField textFieldBrand;
   private TextField textFieldCategory;
   
   private TextField textFieldUpdateDate;
   private TextArea description;
   private Image productImage;
   private Link  productLink;
   
   private TextField textFieldKey;
   private CheckBox productChecked;

   //Buttons 
   private Button editButton;
   
   //Buttons 
   private Button uneditButton;
   
   
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
			
			textFieldId = new TextField("Clave:");
			textFieldId.setWidth("100%");
			textFieldId.setEnabled(false);
			
			textFieldRetail = new TextField("Retail:");
			textFieldRetail.setWidth("100%");
			textFieldRetail.setEnabled(false);
			
			textFieldPrice = new TextField("Precio:");
			textFieldPrice.setWidth("100%");
			textFieldPrice.setEnabled(false);
			
			
			textFieldUpdateDate = new TextField("Actualizado:");
			textFieldUpdateDate.setWidth("100%");
			textFieldUpdateDate.setEnabled(false);
			
			textFieldTaskName = new TextField("Nombre del Bot:");
			textFieldTaskName.setWidth("100%");
			textFieldTaskName.setEnabled(false);
			
			//Key condition
			if (settings.getKeyType().equals("sku")) {
				textFieldKey = new TextField("SKU:");
			}else {
				textFieldKey = new TextField("UPC:");
            } 
	
			textFieldKey.setWidth("100%");
	
			textFieldBrand = new TextField("Marca:");
			textFieldBrand.setWidth("100%");
	
			textFieldCategory = new TextField("Categoría:");
			textFieldCategory.setWidth("100%");
			
			productChecked = new CheckBox("Marcar producto como revisado.");
			productChecked.setValue(true);
			
		   	binder = new Binder<>(Product.class);		   	
		
			editButton = new Button("Emparejar");
			editButton.setIcon(VaadinIcons.EDIT);
			editButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			editButton.addClickListener(this);
			editButton.setWidth("40%");
					
			uneditButton = new Button("Desemparejar");
			uneditButton.setIcon(VaadinIcons.EDIT);
			uneditButton.setStyleName(ValoTheme.BUTTON_DANGER);
			uneditButton.addClickListener(this);
			uneditButton.setWidth("40%");
							
			productImage = new Image();
			productImage.setWidth("100%");
			productImage.setHeight("100%");
	   
			description = new TextArea();
			description.setWidth("100%");
			description.setHeight("100%");
			description.setEnabled(false);
			
			productLink =  new Link();
			productLink.setCaption("Ver en sitio web");
		
			topProductsTable = new Grid<>(Product.class);
			
			topProductsTable.removeAllColumns();
			
			topProductsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(100,Unit.PIXELS);
			    image.setHeight(100,Unit.PIXELS);

			    return image;
			}).setId("Image").setCaption("Imagen");
			
			topProductsTable.addColumn(p ->
		      p.getSeller()).setId("Competence").setCaption("Competencia");
			
			topProductsTable.addColumn(p ->
		      p.getName()).setId("RetrievedName").setCaption("Nombre");
			
			/**
			//Key condition
			if (settings.getKeyType().equals("sku")) {
				topProductsTable.addColumn(p ->
			      p.getSku()).setCaption("SKU");
			}else {
				topProductsTable.addColumn(p ->
			      p.getUpc()).setCaption("UPC");
			} 
	        */
			
			
			topProductsTable.addColumn(p ->
		      p.getPrice()).setId("Price").setCaption("Precio");
				
			topProductsTable.addColumn(p ->
		      "<a target=\"_blank\" href='" + p.getProductUrl() + "' target='_top'>ver link!</a>",
		      new HtmlRenderer()).setId("Links").setCaption("Links");
			
			
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

		 private void editClearField() {
			 
		 //  Set<Product> productsSelectedMemory = topProductsTable.getSelectedItems();
		 //  topProductsTable.setSelectionMode(SelectionMode.SINGLE);
		 //  for (Product p : productsSelectedMemory) {
		 //	   topProductsTable.select(p);
		 //  } 
			 editData(product);	  
		   
		 }
		 
		 
		 private void uneditClearField() {

		 //((MultiSelectionModel) topProductsTable.getSelectionModel()).setUserSelectionAllowed(true);
			 editData(product);	
		 }
		 
		 
			
		public EditProductLayout load() {
			product = null;

			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			return this;
		}


		public Component layout() {
						
			FormLayout formLayout2 = new FormLayout(textFieldId,textFieldRetail,textFieldPrice,textFieldTaskName,textFieldUpdateDate);
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
			
			
			VerticalLayout v3 = new VerticalLayout(topProductsTable);
			v3.setSizeFull();
			v3.setMargin(new MarginInfo(true, false, false, false));
					
			
		//	FormLayout formLayoutKey = new FormLayout(textFieldKey);
		//	formLayoutKey.setWidth("100%");
		//	formLayoutKey.setMargin(false);
			
			/**
			HorizontalLayout hKey = new HorizontalLayout(textFieldKey,textFieldBrand,textFieldCategory,productChecked);	
			hKey.setComponentAlignment(productChecked, Alignment.BOTTOM_CENTER);
			hKey.setWidth("70%");
			hKey.setMargin(new MarginInfo(true, false, false, false));
			*/
			
			HorizontalLayout h1 = new HorizontalLayout(editButton,uneditButton);
			h1.setComponentAlignment(editButton, Alignment.TOP_RIGHT);
			h1.setMargin(false);
			h1.setWidth("100%");
			
			VerticalLayout vl = new VerticalLayout(formLayout,hl,v3,h1);
			vl.setComponentAlignment(h1, Alignment.TOP_CENTER);
			vl.setWidth("100%");
			vl.setMargin(false);
			return vl;
		}
			
		
		@Override
		public void buttonClick(ClickEvent event) {
			 if (event.getSource()==editButton)	{
				 edit();
             }else {
            	 unedit();
             }
		     
        }	
				
		private void unedit() {
			
			if (product==null){
				Notification.show("ERROR","Favor de seleccionar un producto",Type.ERROR_MESSAGE);
				return;
			}
			
			if (product.isChecked()==false){
				Notification.show("ERROR","El producto ya está desemparejado",Type.ERROR_MESSAGE);
				return;
			}
			
			/**
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
			*/
			
			//In case we edit or match
			
			product.setChecked(false);
            product.setSku("");
            product.setUpc("");
            
            
			modifyProductService.modifyProduct(product);
			
			ProductEquivalences pe = new ProductEquivalences();
			pe.setProductKey(product.getProductKey());
			pe.setSku(product.getSku());
			pe.setUpc(product.getUpc());
			pe.setBrand(product.getBrand());
			pe.setCategory(product.getCategory());
			if (product.isChecked()) {
				addProductEquivService.saveEquivalency(pe);
			}else {
				removeProductEquivalencyService.removeEquivalency(pe);
			}
			
		
			//Propagate the profile
			if (topProductsTable.getSelectedItems().size()!=0) {
				for (Product p : topProductsTable.getSelectedItems()) {
					p.setSku(product.getSku());
					p.setUpc(product.getUpc());
					p.setBrand(product.getBrand());
					p.setCategory(product.getCategory());
					p.setChecked(product.isChecked());
					
					modifyProductService.modifyProduct(p);
					
					ProductEquivalences pe2 = new ProductEquivalences();
					pe2.setProductKey(p.getProductKey());
					pe2.setSku(p.getSku());
					pe2.setUpc(p.getUpc());
					pe2.setBrand(p.getBrand());
					pe2.setCategory(p.getCategory());
					if (product.isChecked()) {
						addProductEquivService.saveEquivalency(pe2);
					}else {
						removeProductEquivalencyService.removeEquivalency(pe2);
					}
					
				}
			}
			
			
			productSaveListener.productSaved();
		//	editData(product); //In order to refreh the topProductsTable
			uneditClearField();
			Notification.show("DESEMPAREJADO","El producto fue desemparejado con éxito",Type.WARNING_MESSAGE);
		}
		
		
		
		private void edit() {
	
			if (product==null){
				Notification.show("ERROR","Favor de seleccionar un producto",Type.ERROR_MESSAGE);
				return;
			}
			
			if (product.isChecked()==true){
				Notification.show("ERROR","El producto ya está emparejado",Type.ERROR_MESSAGE);
				return;
			}
			
			//At least one selected in top table
			if (topProductsTable.getSelectedItems().size()==0) {
				Notification.show("ERROR","Debe seleccionar al menos un producto",Type.ERROR_MESSAGE);
				return;
			}
			
			/**
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
			*/
			
			//In case we edit or match
			
			product.setChecked(true);
            product.setSku(product.getProductId());
            product.setUpc(product.getProductId());
            
            
			modifyProductService.modifyProduct(product);
			
			ProductEquivalences pe = new ProductEquivalences();
			pe.setProductKey(product.getProductKey());
			pe.setSku(product.getSku());
			pe.setUpc(product.getUpc());
			pe.setBrand(product.getBrand());
			pe.setCategory(product.getCategory());
			if (product.isChecked()) {
				addProductEquivService.saveEquivalency(pe);
			}else {
				removeProductEquivalencyService.removeEquivalency(pe);
			}
			
		
			//Propagate the profile
			if (topProductsTable.getSelectedItems().size()!=0) {
				for (Product p : topProductsTable.getSelectedItems()) {
					p.setSku(product.getSku());
					p.setUpc(product.getUpc());
					p.setBrand(product.getBrand());
					p.setCategory(product.getCategory());
					p.setChecked(product.isChecked());
					
					modifyProductService.modifyProduct(p);
					
					ProductEquivalences pe2 = new ProductEquivalences();
					pe2.setProductKey(p.getProductKey());
					pe2.setSku(p.getSku());
					pe2.setUpc(p.getUpc());
					pe2.setBrand(p.getBrand());
					pe2.setCategory(p.getCategory());
					if (product.isChecked()) {
						addProductEquivService.saveEquivalency(pe2);
					}else {
						removeProductEquivalencyService.removeEquivalency(pe2);
					}
					
				}
			}
			
			productSaveListener.productSaved();
		//	editData(product); //In order to refreh the topProductsTable
			editClearField();
			Notification.show("EMPAREJADO","El producto fue emparejado con éxito",Type.WARNING_MESSAGE);
			
		}


		public EditProductLayout bind() {
			binder.forField(textFieldKey)
			//  .asRequired("Key must be set")
			  .bind(settings.getKeyType()); //sku or upc from product

			binder.forField(textFieldBrand)
			//  .asRequired("Key must be set")
			  .bind("brand"); //sku or upc from product
			
			binder.forField(textFieldCategory)
			//  .asRequired("Key must be set")
			  .bind("category"); //sku or upc from product

			binder.forField(productChecked)
			  .bind("checked");
			
			
			binder.readBean(product);
			
			return this;
		}
		
				
	}
   
   
    @Autowired 
    private RemoveProductEquivalencyService removeProductEquivalencyService;
   
    @Autowired 
    private AddProductEquivService addProductEquivService;
   
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
    		return new EditProductLayout(productSaveListener).load().init().layout();
    }
    	
    	
	public void editData(Object item) {
		product = (Product) item;
		
		/**
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
		*/
		
		
		  //Only show those products selected
		if (product.isChecked()==true) {
			
		//	((MultiSelectionModel) topProductsTable.getSelectionModel()).setUserSelectionAllowed(false);
		//	topProductsTable.setItems(retrieveFilteredAndMatchedList);
			
		//Work it with Sql query	
			
			List<Product> retrieveList = showAllProductsService.getMatchedProducts(product.getSeller(), product.getSku());
            ((MultiSelectionModel) topProductsTable.getSelectionModel()).setUserSelectionAllowed(false);
			topProductsTable.setItems(retrieveList);
			
			for (Product p : retrieveList) {
				   topProductsTable.select(p);
			}
			
		} else {
			
			
			
			
			//Set topProductsTable
			//	String searchQuery = product.getName() + product.getBrand() + product.getSku();
				String searchQuery = product.getName();
				
			//	searchQuery = searchQuery.replace(product.getBrand(), " ");
				
				//Small trick to transform the comma separated stop words to a set in capital letters
				List<String> stopWordsList = Arrays.asList(settings.getStopWords().split(","));
				stopWordsList.replaceAll(String::toUpperCase);
				Set<String> stopWordsSet= new HashSet<String>(stopWordsList);
			
				for (String s:stopWordsSet) {
					System.out.println(s);
				}
				
				searchQuery = preprocessAndStopWords(searchQuery, stopWordsSet);
				System.out.println("El search query es: " + searchQuery);
				//Get all sellers except for the main seller.
				List <String> wantedSellers = showAllProductsService.getSellersListExceptForSeller(settings.getMainSeller());
				List<Product> retrieveList = searchProductService.search(searchQuery,wantedSellers);
				
				
				
						
				if (retrieveList.size()!=0) { //Do this only if products related are found
				   boolean remove = retrieveList.remove(product);
				   String sellerSelected = product.getSeller();
				   List<Product> retrieveFilteredList = new LinkedList<Product>();
				   List<Product> retrieveFilteredAndMatchedList = new LinkedList<Product>();
				   int con = 0;
				
				   for (Product p : retrieveList) {
				   	if (p.isChecked()==false) { //Consider only those available for checked
					    retrieveFilteredList.add(p);
					    //Store those with same key!!   
					    if (settings.getKeyType().equals("sku")) {
							if (p.getSku().equals(product.getSku()) && !p.getSku().equals("")) { //Not consider those with blanks spaces as key
						    	retrieveFilteredAndMatchedList.add(p);
						    }
						}else {
							if (p.getUpc().equals(product.getUpc()) && !p.getUpc().equals("")) { 
						    	retrieveFilteredAndMatchedList.add(p);
						    }	
						} 
					    
					    
					}
					   con++;
					   if (con>settings.getNumRetrieved() && settings.getNumRetrieved()!=100) {
						  System.out.println("con" + con);
					      break;	
					  }
				   }
			
				   ((MultiSelectionModel) topProductsTable.getSelectionModel()).setUserSelectionAllowed(true);
					topProductsTable.setItems(retrieveFilteredList);
				
				   //Select all with coincidences
				   for (Product p : retrieveFilteredAndMatchedList) {
					   topProductsTable.select(p);
				   }
				
				} 
			
			
			
			
			
			
		}
		
		
		
		//Disable sorting for all columns, in fact the order is very importance, it has to only ordered by search relevance
		
		topProductsTable.getColumn("Image").setSortable(false);
		topProductsTable.getColumn("Competence").setSortable(false);
		topProductsTable.getColumn("RetrievedName").setSortable(false);
		topProductsTable.getColumn("Price").setSortable(false);
		topProductsTable.getColumn("Links").setSortable(false);
		
		//Set product name
		textFieldName.setValue(product.getName());
		
		//Set product pid
		textFieldId.setValue(product.getProductId());
		
		//Set product precio
		textFieldPrice.setValue(product.getPrice().toString());
				
		//Set product precio
		if (product.getTask()!=null)
		    textFieldRetail.setValue(product.getTask().getRetail().getRetailName());
		
		//Set product TaskName
		if (product.getTask()!=null)
		    textFieldTaskName.setValue(product.getTask().getTaskName());
		
		//Set product Brand
		textFieldBrand.setValue(product.getBrand());
		
		//Set product category
		textFieldCategory.setValue(product.getCategory());
		
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
	
	
	
	private String preprocessAndStopWords(String descripcion,Set<String> stopwordsset) {
	  //  descripcion = descripcion.replaceAll("[^A-Za-z0-9]"," ").trim();
	      descripcion = descripcion.trim().replaceAll(" +", " ");
	  //  descripcion = descripcion.trim().replaceAll(brand, "");
	    String[] words = descripcion.split(" ");
        ArrayList<String> wordsList = new ArrayList<String>();
       
        for(String word : words)
        {
            String wordCompare = word.toUpperCase();
            if(!stopwordsset.contains(wordCompare))
            {
                wordsList.add(word);
            }
        }
        String output = "";
        for (String str : wordsList){
           output +=str + " ";
        }
	return output.trim();
   }
	
		
}