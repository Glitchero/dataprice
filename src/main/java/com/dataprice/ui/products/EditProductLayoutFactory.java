package com.dataprice.ui.products;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dataprice.model.entity.Brand;
import com.dataprice.model.entity.Category;
import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.Subcategory;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.User;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.searchproduct.SearchProductService;
import com.dataprice.service.security.UserServiceImpl;
import com.dataprice.service.showallbrands.ShowAllBrandsService;
import com.dataprice.service.showallcategories.ShowAllCategoriesService;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showallsubcategories.ShowAllSubcategoriesService;
import com.dataprice.ui.VaadinHybridMenuUI;
import com.dataprice.ui.classification.BrandLayoutFactory;
import com.dataprice.ui.classification.CategoryLayoutFactory;
import com.dataprice.ui.classification.GenderLayoutFactory;
import com.dataprice.ui.classification.SubcategoryLayoutFactory;
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
   private TextField textFieldTaskName;
   private TextField textFieldAddedDate;
   
   private TextField textFieldPid;
   private ComboBoxWithButton GenderComboBoxWithButton;
   private ComboBoxWithButton CategoryComboBoxWithButton;
   private ComboBoxWithButton SubcategoryComboBoxWithButton;
   private ComboBoxWithButton BrandComboBoxWithButton;
   
   private Window subWindow;
   
   private TextArea description;
   private Image productImage;
   private Link  productLink;
   
   private Button editButton;
   private Button cancelButton;
   
   private Product product;
 
   private boolean isSaveOperationValidForSubcategory;
   private boolean isSaveOperationValidForBrand;
   private Grid<Product> topProductsTable;  ///IMPORTANT!!!! I also have to update this table when edit is cliked!.
   
   private Button addExtraDataButton;
   
   private Subcategory currentSubcategory = null;
   private Category currentCategory = null;
   private Gender currentGender = null;
   private Brand currentBrand = null;
   
   
   
   private class EditProductLayout extends VerticalLayout implements Button.ClickListener,ValueChangeListener, CloseListener{

		 
		
		private ProductSaveListener productSaveListener;
		private Binder<Product> binder;
		 
		public EditProductLayout(ProductSaveListener productSaveListener) {
			this.productSaveListener = productSaveListener;
		}
		
		public EditProductLayout init() {
			
			textFieldName = new TextField("Nombre");
			textFieldName.setWidth("100%");
			textFieldName.setEnabled(false);
			
			textFieldId = new TextField("Id");
			textFieldId.setWidth("100%");
			textFieldId.setEnabled(false);
			
			textFieldRetail = new TextField("Retail");
			textFieldRetail.setWidth("100%");
			textFieldRetail.setEnabled(false);
			
			textFieldPrice = new TextField("Price");
			textFieldPrice.setWidth("100%");
			textFieldPrice.setEnabled(false);
			
			textFieldTaskName = new TextField("Task Name");
			textFieldTaskName.setWidth("100%");
			textFieldTaskName.setEnabled(false);
			
			textFieldAddedDate = new TextField("Added date");
			textFieldAddedDate.setWidth("100%");
			textFieldAddedDate.setEnabled(false);
			
			textFieldPid = new TextField("Modelo");
			textFieldPid.setWidth("100%");
			
			addExtraDataButton = new Button("Agregar datos adicionales (opcional)");
			addExtraDataButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			addExtraDataButton.addClickListener(this);
			
			GenderComboBoxWithButton = new ComboBoxWithButton("Gender", VaadinIcons.PLUS_CIRCLE,
	                onClick -> openSubWindow("Gender"));
			GenderComboBoxWithButton.setWidth("90%");
			GenderComboBoxWithButton.getComboBox().addValueChangeListener(this);
			GenderComboBoxWithButton.setVisible(false);
			
			
			CategoryComboBoxWithButton = new ComboBoxWithButton("Category", VaadinIcons.PLUS_CIRCLE,
	                onClick -> openSubWindow("Category"));
			CategoryComboBoxWithButton.setWidth("90%");
			CategoryComboBoxWithButton.getComboBox().addValueChangeListener(this);
			CategoryComboBoxWithButton.setVisible(false);
			
			
			SubcategoryComboBoxWithButton = new ComboBoxWithButton("Subcategory", VaadinIcons.PLUS_CIRCLE,
	                onClick -> openSubWindow("Subcategory"));
			SubcategoryComboBoxWithButton.setWidth("90%");
			SubcategoryComboBoxWithButton.getComboBox().addValueChangeListener(this);
			SubcategoryComboBoxWithButton.setVisible(false);
			
			
			BrandComboBoxWithButton = new ComboBoxWithButton("Brand", VaadinIcons.PLUS_CIRCLE,
	                onClick -> openSubWindow("Brand"));  //Do not change 
			BrandComboBoxWithButton.setWidth("90%");
			BrandComboBoxWithButton.getComboBox().addValueChangeListener(this);
			BrandComboBoxWithButton.setVisible(false);
			
			
					
		   	binder = new Binder<>(Product.class);
		   	
		
			editButton = new Button("Editar");
			editButton.setIcon(VaadinIcons.EDIT);
			editButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			editButton.addClickListener(this);
			editButton.setWidth("100%");
			
			cancelButton = new Button("Limpiar");
			cancelButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			cancelButton.setIcon(VaadinIcons.ERASER);
			cancelButton.addClickListener(this);
			cancelButton.setWidth("100%");
			
			
			subWindow = new Window("Product profile manager");
			subWindow.setHeight("550px");
			subWindow.setWidth("800px");
			subWindow.setModal(true);
			subWindow.addCloseListener(this);
			
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
			
			topProductsTable.addColumn(p ->
		      p.getName()).setCaption("Nombre");
			
			topProductsTable.addColumn(p ->
		      p.getPid()).setCaption("Pid");
			
			topProductsTable.addColumn(p ->
		      p.getPrice()).setCaption("Precio");
			
			topProductsTable.addComponentColumn(probe -> {
			    Image image = new Image("", new ExternalResource(probe.getImageUrl()));
			    image.setWidth(100,Unit.PIXELS);
			    image.setHeight(100,Unit.PIXELS);

			    return image;
			}).setCaption("Imagen");
				
			topProductsTable.addColumn(p ->
		      "<a target=\"_blank\" href='" + p.getProductUrl() + "' target='_top'>product link</a>",
		      new HtmlRenderer()).setCaption("Link");
			
			topProductsTable.setSelectionMode(SelectionMode.MULTI);
			
			topProductsTable.removeColumn("imageUrl");
			topProductsTable.removeColumn("productUrl");
			topProductsTable.removeColumn("productKey");
			topProductsTable.removeColumn("category");
			topProductsTable.removeColumn("subcategory");
			topProductsTable.removeColumn("brand");
			topProductsTable.removeColumn("gender");
			topProductsTable.removeColumn("task");
			topProductsTable.removeColumn("name");
			topProductsTable.removeColumn("pid");
			topProductsTable.removeColumn("price");
			topProductsTable.removeColumn("productId");
			topProductsTable.removeColumn("description");
			
			topProductsTable.setBodyRowHeight(100);
			topProductsTable.setVisible(true);
			topProductsTable.setWidth("100%");

			
			return this;
			
		}

		 private void clearField() {
			textFieldPid.setValue("");
			GenderComboBoxWithButton.getComboBox().setValue(null);
			CategoryComboBoxWithButton.getComboBox().setValue(null);
			SubcategoryComboBoxWithButton.getComboBox().setValue(null);
			BrandComboBoxWithButton.getComboBox().setValue(null);
			
			/**
			textFieldName.setValue("");
			textFieldId.setValue("");
			textFieldPrice.setValue("");
			textFieldRetail.setValue("");
			textFieldTaskName.setValue("");
			textFieldAddedDate.setValue("");
			productImage.setSource(null);
			description.setValue("");
			productLink.setResource(null);
			productLink.setTargetName("_blank");
	
			topProductsTable.setItems(new LinkedList<Product>());  //Remove all items.
		   */
		 }
		 
		private boolean isSaveOperationValidForCategory() {
			return showAllCategoriesService.getAllCategories().size() !=0;
		}
		
		private boolean isSaveOperationValidForGender() {
			return showAllGendersService.getAllGenders().size() !=0;
		}
		
		public EditProductLayout load() {
			List<Gender> genderList = showAllGendersService.getAllGenders();
			GenderComboBoxWithButton.getComboBox().setItems(genderList);
			List<Category> categoryList = showAllCategoriesService.getAllCategories();
			CategoryComboBoxWithButton.getComboBox().setItems(categoryList);
			
			product = null;
			
			User user = userServiceImpl.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			settings = user.getSettings();
			
			return this;
		}


		public Component layout() {
				

			
			FormLayout formLayout2 = new FormLayout(textFieldId,textFieldRetail,textFieldPrice,textFieldTaskName,textFieldAddedDate);
			formLayout2.setWidth("100%");
			formLayout2.setMargin(false);
			
			VerticalLayout vlform2 = new VerticalLayout(formLayout2);
			vlform2.setWidth("100%");
			vlform2.setExpandRatio(formLayout2, 1);
			vlform2.setMargin(false); //era true

		
			GenderComboBoxWithButton.getComboBox().setRequiredIndicatorVisible(false);
			CategoryComboBoxWithButton.getComboBox().setRequiredIndicatorVisible(false);
			SubcategoryComboBoxWithButton.getComboBox().setRequiredIndicatorVisible(false);
			BrandComboBoxWithButton.getComboBox().setRequiredIndicatorVisible(false);
			
			/**
			FormLayout formLayout3 = new FormLayout(textFieldPid,GenderComboBoxWithButton,CategoryComboBoxWithButton,SubcategoryComboBoxWithButton,BrandComboBoxWithButton);	
			formLayout3.setWidth("100%");
			formLayout3.setMargin(false);
			VerticalLayout vlform3 = new VerticalLayout(formLayout3);
			vlform3.setWidth("100%");
			vlform3.setExpandRatio(formLayout3, 1);
			vlform3.setMargin(false); //era true
			*/
			
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
			cancelButton.setWidth("100%");

			
			VerticalLayout v3 = new VerticalLayout(topProductsTable);
			v3.setSizeFull();
			v3.setMargin(new MarginInfo(true, false, false, false));
		
			
			
			
			HorizontalLayout hKey = new HorizontalLayout(textFieldPid,addExtraDataButton);	
			hKey.setComponentAlignment(addExtraDataButton, Alignment.BOTTOM_LEFT);
			hKey.setWidth("50%");
			hKey.setMargin(new MarginInfo(true, false, false, false));
			
			HorizontalLayout hProfile = new HorizontalLayout(GenderComboBoxWithButton,CategoryComboBoxWithButton,SubcategoryComboBoxWithButton,BrandComboBoxWithButton);	
			hProfile.setWidth("100%");
			hProfile.setMargin(new MarginInfo(false, false, false, false));
			
			HorizontalLayout h1 = new HorizontalLayout(editButton,cancelButton);
			h1.setMargin(false);
			h1.setWidth("25%");
			
			VerticalLayout vl = new VerticalLayout(formLayout,hl,v3,hKey,hProfile,h1);
			vl.setComponentAlignment(h1, Alignment.TOP_LEFT);
			vl.setWidth("100%");
			vl.setMargin(false);
			return vl;
		}
			
		
		@Override
		public void buttonClick(ClickEvent event) {
			 if (event.getSource()==editButton)	{
            	 edit();
             }else {
            	 if (event.getSource()==cancelButton) {
            		 cancel();
            	 }else {
            		 if (event.getSource()==addExtraDataButton) {
            			 showExtraData();
            		 }else {
            			 
            		 }
            		 
            	 }
             }	
		}


		private void showExtraData() {
			if (addExtraDataButton.getCaption().equals("Agregar datos adicionales (opcional)")) {
				GenderComboBoxWithButton.setVisible(true);
				CategoryComboBoxWithButton.setVisible(true);
				SubcategoryComboBoxWithButton.setVisible(true);
				BrandComboBoxWithButton.setVisible(true);
				addExtraDataButton.setCaption("Ocultar datos adicionales");
			}else {
				GenderComboBoxWithButton.setVisible(false);
				CategoryComboBoxWithButton.setVisible(false);
				SubcategoryComboBoxWithButton.setVisible(false);
				BrandComboBoxWithButton.setVisible(false);
				addExtraDataButton.setCaption("Agregar datos adicionales (opcional)");
			}

		}

		private void openSubWindow(String profile) {
			
			Component component = null;
			switch(profile) {
			   case "Gender" :
				   component = genderLayoutFactory.createComponent();
			      break; 
			   
			   case "Category" :
				   component = categoryLayoutFactory.createComponent();
			      break; 
			      
			   case "Subcategory" :
				   component = subcategoryLayoutFactory.createComponent();				   
				  break; 
				      
			   case "Brand" :
				   component = brandLayoutFactory.createComponent();
				  break; 
			   
			   default : 
			      System.out.println("Opps profile name dosen't exist, please check class editproductlayout");
			}
			
		
			VerticalLayout subContent = new VerticalLayout();
			subWindow.setContent(subContent);
			subContent.addComponent(component);
			subWindow.center();
			vaadinHybridMenuUI.addWindow(subWindow);
		}

		private void cancel() {

			clearField();
			
		}

		private void edit() {
			
			
			if (product==null){
				Notification.show("ERROR","Please select a product first",Type.ERROR_MESSAGE);
				return;
			}
			
		/**
			
			if(!isSaveOperationValidForGender()) {
				Notification.show("ERROR","Must have at least one gender",Type.ERROR_MESSAGE);
				return;
			}
			
			if(!isSaveOperationValidForCategory()) {
				Notification.show("ERROR","Must have at least one category",Type.ERROR_MESSAGE);
				return;
			}
			
			if(!isSaveOperationValidForSubcategory) {
				Notification.show("ERROR","Must have at least one subcategory",Type.ERROR_MESSAGE);
				return;
			}
			
			if(!isSaveOperationValidForBrand) {
				Notification.show("ERROR","Must have at least one Brand",Type.ERROR_MESSAGE);
				return;
			}
			
		*/
						
			BinderValidationStatus<Product> status = binder.validate();

			if (status.hasErrors()) {
				Notification.show(NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_TITLE.getString(),NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_DESCRIPTION.getString(),Type.ERROR_MESSAGE);
			   return;
			}
			
			if(!isEditOperationValid()) {
				Notification.show("ERROR","Same products must have one profile",Type.ERROR_MESSAGE);
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
					p.setPid(product.getPid());
					p.setBrand(product.getBrand());
					p.setCategory(product.getCategory());
					p.setSubcategory(product.getSubcategory());
					p.setGender(product.getGender());
					
					modifyProductService.modifyProduct(p);
				}
			}
			
			productSaveListener.productSaved();
			cancel();
			Notification.show("EDIT","Product profile editted",Type.WARNING_MESSAGE);
			
		}


		public EditProductLayout bind() {
			binder.forField(textFieldPid)
			  .asRequired("pid must be set")
			  .bind("pid");
			
			
			binder.forField(GenderComboBoxWithButton.getComboBox())
				 // .asRequired("genders must be set")
				  .bind("gender");
			
			
			binder.forField(CategoryComboBoxWithButton.getComboBox())
			 // .asRequired("category must be set")
			  .bind("category");
			
			binder.forField(SubcategoryComboBoxWithButton.getComboBox())
		//	  .asRequired("subcategory must be set")
			  .bind("subcategory");
			
			binder.forField(BrandComboBoxWithButton.getComboBox())
		//	  .asRequired("brand must be set")
			  .bind("brand");
			
			binder.readBean(product);
			
			return this;
		}
		
		private boolean isEditOperationValid() {
			
			List<Product> productList = showAllProductsService.getAllProductsFromPid(textFieldPid.getValue());
			boolean validation = true;
			boolean isSameProduct = false;
			if (productList.size()==1) {
				if (productList.get(0).getProductKey().equals(product.getProductKey())){
					isSameProduct = true;
				}else {
					isSameProduct = false;
				}
			}
			
			if (!isSameProduct) {
		    	Gender genderValue = (Gender) GenderComboBoxWithButton.getComboBox().getValue();
			    Category categoryValue = (Category) CategoryComboBoxWithButton.getComboBox().getValue();
			    Subcategory subcategoryValue = (Subcategory) SubcategoryComboBoxWithButton.getComboBox().getValue();
			    Brand brandValue = (Brand) BrandComboBoxWithButton.getComboBox().getValue();
			
			    for(Product product : productList) {
				
				     if (product.getGender()!=null) {
				           if (!product.getGender().getGenderName().equals(genderValue.getGenderName())) {
				    	       validation = false;
				           }
				     }
				
				     if (product.getCategory()!=null) {
				          if (!product.getCategory().getCategoryName().equals(categoryValue.getCategoryName())) {
					          validation = false;
				          }
				     }     
				     if (product.getSubcategory()!=null) {
					      if (!product.getSubcategory().getSubcategoryName().equals(subcategoryValue.getSubcategoryName())) {
						      validation = false;
					      }         
				     }   
				     
				     if (product.getBrand()!=null) {
					      if (!product.getBrand().getBrandName().equals(brandValue.getBrandName())) {
						      validation = false;
					      }         
				     }  
				     
			     }
			}
			return validation;
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			

			 if (event.getSource()==GenderComboBoxWithButton.getComboBox())	{
				 currentGender = (Gender) GenderComboBoxWithButton.getComboBox().getValue();
             }else {
            	 if (event.getSource()==CategoryComboBoxWithButton.getComboBox()) {
            		 categoryComboBoxChange();
            	 }else {
            		 if (event.getSource()==SubcategoryComboBoxWithButton.getComboBox()) {
        				 currentSubcategory = (Subcategory) SubcategoryComboBoxWithButton.getComboBox().getValue();	
            		 }else {
        				 currentBrand = (Brand) BrandComboBoxWithButton.getComboBox().getValue();	
            		 }	 
            	 }
             }			
		}

		
		
		private void categoryComboBoxChange() {
			
			SubcategoryComboBoxWithButton.getComboBox().setValue(null);
			BrandComboBoxWithButton.getComboBox().setValue(null);
		
			Category categoryValue = (Category) CategoryComboBoxWithButton.getComboBox().getValue();	
			currentCategory = categoryValue;

			if (categoryValue!=null) {
				List<Subcategory> subcategoryList = showAllSubcategoriesService.getAllSubcategoriesForCategory(categoryValue.getCategoryId());
				if (subcategoryList.size() !=0) {
					isSaveOperationValidForSubcategory = true;
				}else {
					isSaveOperationValidForSubcategory = false;
				}
				SubcategoryComboBoxWithButton.getComboBox().setItems(subcategoryList);
				
				
				List<Brand> brandList = showAllBrandsService.getAllBrandsForCategory(categoryValue.getCategoryId());
				if (brandList.size() !=0) {
					isSaveOperationValidForBrand = true;
				}else {
					isSaveOperationValidForBrand = false;
				}
				BrandComboBoxWithButton.getComboBox().setItems(brandList);	
			}
		}

		
		@Override
		public void windowClose(CloseEvent e) {
			
			List<Gender> genderList = showAllGendersService.getAllGenders();
			GenderComboBoxWithButton.getComboBox().setItems(genderList);
			List<Category> categoryList = showAllCategoriesService.getAllCategories();
			CategoryComboBoxWithButton.getComboBox().setItems(categoryList);
			
			//We need to repeat the same as when we change a value in the category combobox.
			
			if (currentCategory!=null) {
				List<Subcategory> subcategoryList = showAllSubcategoriesService.getAllSubcategoriesForCategory(currentCategory.getCategoryId());
				if (subcategoryList.size() !=0) {
					isSaveOperationValidForSubcategory = true;
				}else {
					isSaveOperationValidForSubcategory = false;
				}
				SubcategoryComboBoxWithButton.getComboBox().setItems(subcategoryList);
				
				
				List<Brand> brandList = showAllBrandsService.getAllBrandsForCategory(currentCategory.getCategoryId());
				if (brandList.size() !=0) {
					isSaveOperationValidForBrand = true;
				}else {
					isSaveOperationValidForBrand = false;
				}
				BrandComboBoxWithButton.getComboBox().setItems(brandList);
				
			}
			
			// Set profile
			if (currentCategory!=null) {
				CategoryComboBoxWithButton.getComboBox().setValue(currentCategory);
			}
			
			if (currentGender!=null) {
				GenderComboBoxWithButton.getComboBox().setValue(currentGender);
			}
			
			if (currentSubcategory!=null) {
				SubcategoryComboBoxWithButton.getComboBox().setValue(currentSubcategory);
			}
			
			if (currentBrand!=null) {
				BrandComboBoxWithButton.getComboBox().setValue(currentBrand);
			}
			
			productSaveListener.productSaved();// Need to refresh main table!!
		}
		
		
		
	}
   
	@Autowired 
	private UserServiceImpl userServiceImpl;
   
    @Autowired
    private VaadinHybridMenuUI vaadinHybridMenuUI;

    @Autowired
    private ShowAllProductsService showAllProductsService;
   
    @Autowired
    private ShowAllGendersService showAllGendersService;
   
    @Autowired
    private ModifyProductService modifyProductService;
   
    @Autowired
    private ShowAllCategoriesService showAllCategoriesService;
   
    @Autowired
    private ShowAllSubcategoriesService showAllSubcategoriesService;
   
    @Autowired
    private ShowAllBrandsService showAllBrandsService;
   
	@Autowired
	private SearchProductService searchProductService;
	
	@Autowired
	private GenderLayoutFactory genderLayoutFactory;
	
	@Autowired
	private CategoryLayoutFactory categoryLayoutFactory;
	
	@Autowired
	private SubcategoryLayoutFactory subcategoryLayoutFactory;
	
	@Autowired
	private BrandLayoutFactory brandLayoutFactory;
	
   
   public Component createComponent(ProductSaveListener productSaveListener) {
    		return new EditProductLayout(productSaveListener).init().load().bind().layout();
    }
    	
    	
	public void editData(Object item) {
		product = (Product) item;
		
		//Set Pid
		if (product.getPid()!=null) {
			textFieldPid.setValue(product.getPid());
		}else {
			textFieldPid.setValue("");
		}
		//Set Gender
		if (product.getGender()!=null) {
			GenderComboBoxWithButton.getComboBox().setValue(product.getGender());
		}else {
			GenderComboBoxWithButton.getComboBox().setValue(null);
		}
		//Set Category
		if (product.getCategory()!=null) {
			CategoryComboBoxWithButton.getComboBox().setValue(product.getCategory());
		}else {
			CategoryComboBoxWithButton.getComboBox().setValue(null);
		}
		//Set Subcategory
		if (product.getSubcategory()!=null) {
			SubcategoryComboBoxWithButton.getComboBox().setValue(product.getSubcategory());
		}else {
			SubcategoryComboBoxWithButton.getComboBox().setValue(null);
		}
		
		//Set Brand
		if (product.getBrand()!=null) {
			BrandComboBoxWithButton.getComboBox().setValue(product.getBrand());
		}else {
			BrandComboBoxWithButton.getComboBox().setValue(null);
		}
		
		GenderComboBoxWithButton.setVisible(false);
		CategoryComboBoxWithButton.setVisible(false);
		SubcategoryComboBoxWithButton.setVisible(false);
		BrandComboBoxWithButton.setVisible(false);
		
		
		//Set topProductsTable
		List<Product> retrieveList = searchProductService.search(product.getName());
		
		if (retrieveList.size()!=0) {
		boolean remove = retrieveList.remove(product);
		String sellerSelected = product.getSeller();
		//System.out.println("Retail selected: " + retailSelected);
		List<Product> retrieveFilteredList = new LinkedList<Product>();
		//System.out.println("Tamaño: " + retrieveFilteredList.size());
		int con = 0;
		for (Product p : retrieveList) {
			if (!p.getSeller().equals(sellerSelected)) {
				//System.out.println("Retail for: " + p.getRetail());
			    retrieveFilteredList.add(p);
			}
			con++;
			if (con>settings.getNumRetrieved()) {
			    break;	
			}
		}
		//topProductsTable.setItems(retrieveList);
		topProductsTable.setItems(retrieveFilteredList);
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
		textFieldTaskName.setValue(product.getTask().getTaskName());
		
		//Set product date
		textFieldAddedDate.setValue(product.getTask().getRunDateTime().toString());
				
		//Set product image
		ExternalResource externalResource = new ExternalResource(product.getImageUrl());
		productImage.setSource(externalResource);
		
		//Set description
		
		description.setValue(product.getDescription());
		
		//Set product link
		
		ExternalResource externalResourceLink = new ExternalResource(product.getProductUrl());
		productLink.setResource(externalResourceLink);
		productLink.setTargetName("_blank");
		
		

	}
	
	
		
}