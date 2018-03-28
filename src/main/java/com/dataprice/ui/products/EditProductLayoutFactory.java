package com.dataprice.ui.products;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Brand;
import com.dataprice.model.entity.Category;
import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Subcategory;
import com.dataprice.model.entity.Task;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.showallbrands.ShowAllBrandsService;
import com.dataprice.service.showallcategories.ShowAllCategoriesService;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.service.showallproducts.ShowAllProductsService;
import com.dataprice.service.showallsubcategories.ShowAllSubcategoriesService;
import com.dataprice.utils.NotificationsMessages;
import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class EditProductLayoutFactory {

   private Product product;
   private TextField pid;
   private ComboBox gender;
   private ComboBox category;
   private ComboBox subcategory;
   private ComboBox brand;
   private Button editButton;
   private Button cancelButton;
   private boolean isSaveOperationValidForSubcategory;
   private boolean isSaveOperationValidForBrand;
   
   
   private class EditProductLayout extends VerticalLayout implements Button.ClickListener,ValueChangeListener{

		 
		
		private ProductSaveListener productSaveListener;
		private Binder<Product> binder;
		 
		public EditProductLayout(ProductSaveListener productSaveListener) {
			this.productSaveListener = productSaveListener;
		}
		
		public EditProductLayout init() {
		   	binder = new Binder<>(Product.class);
		   	
		   	pid = new TextField("PID");
		   	pid.setVisible(false);
		   	
			gender = new ComboBox("Genders");
			gender.setVisible(false);
			
			category = new ComboBox("Categories");
			category.setVisible(false);
			category.addValueChangeListener(this);
			
			subcategory = new ComboBox("Subcategories");
			subcategory.setVisible(false);
			
			brand = new ComboBox("Brand");
			brand.setVisible(false);
			
			editButton = new Button("Editar");
			editButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			editButton.addClickListener(this);
			editButton.setVisible(false);
			
			cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			cancelButton.addClickListener(this);
			cancelButton.setVisible(false); 
			return this;
			
		}

		 private void clearField() {
			pid.setValue("");
		    gender.setValue(null);
			category.setValue(null);
			subcategory.setValue(null);
			brand.setValue(null);
		 }
		 
		private boolean isSaveOperationValidForCategory() {
			return showAllCategoriesService.getAllCategories().size() !=0;
		}
		
		private boolean isSaveOperationValidForGender() {
			return showAllGendersService.getAllGenders().size() !=0;
		}
		
		public EditProductLayout load() {
			List<Gender> genderList = showAllGendersService.getAllGenders();
			gender.setItems(genderList);
			List<Category> categoryList = showAllCategoriesService.getAllCategories();
			category.setItems(categoryList);
			return this;
		}


		public Component layout() {
			 setMargin(true);
			 HorizontalLayout h1 = new HorizontalLayout();
			 h1.addComponent(pid);
			 h1.addComponent(gender);
			 h1.addComponent(category);
			 h1.addComponent(subcategory);
			 h1.addComponent(brand);
			 h1.addComponent(editButton);
			 h1.addComponent(cancelButton);
			 h1.setComponentAlignment(editButton, Alignment.BOTTOM_CENTER);
			 h1.setComponentAlignment(cancelButton, Alignment.BOTTOM_CENTER);
		//	 HorizontalLayout h2 = new HorizontalLayout();
		//	 h2.addComponent(editButton);
		//	 h2.addComponent(cancelButton);
			 
		//	 VerticalLayout v1 = new VerticalLayout(h1,h2);
			 return h1;
		}
		
		
		
		@Override
		public void buttonClick(ClickEvent event) {
			 if (event.getSource()==editButton)	{
            	 edit();
             }else {
            	 cancel();
             }
		
			
		}


		private void cancel() {
			gender.setVisible(false);
			category.setVisible(false);
			subcategory.setVisible(false);
			brand.setVisible(false);
			editButton.setVisible(false);
			cancelButton.setVisible(false);
			pid.setVisible(false);
			clearField();
			
		}

		private void edit() {
			
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
			productSaveListener.productSaved();
			cancel();
			Notification.show("EDIT","Product profile editted",Type.WARNING_MESSAGE);
			
		}


		public EditProductLayout bind() {
			binder.forField(pid)
			  .asRequired("pid must be set")
			  .bind("pid");
			
			binder.forField(gender)
			  .asRequired("genders must be set")
			  .bind("gender");
			
			binder.forField(category)
			  .asRequired("category must be set")
			  .bind("category");
			
			binder.forField(subcategory)
			  .asRequired("subcategory must be set")
			  .bind("subcategory");
					
			binder.forField(brand)
			  .asRequired("brand must be set")
			  .bind("brand");
			
			binder.readBean(product);
			
			return this;
		}
		
		private boolean isEditOperationValid() {
			
			List<Product> productList = showAllProductsService.getAllProductsFromPid(pid.getValue());
			boolean validation = true;
			boolean isSameProduct = false;
			if (productList.size()==1) {
			  //	if (productList.get(0).getProductId().equals(product.getProductId()) && productList.get(0).getRetail().equals(product.getRetail())){
				if (productList.get(0).getProductKey().equals(product.getProductKey())){
					isSameProduct = true;
				}else {
					isSameProduct = false;
				}
			}
			
			if (!isSameProduct) {
				//System.out.println("entre : " + product.getProductId() + " - " + product.getRetail());
		    	Gender genderValue = (Gender) gender.getValue();
			    Category categoryValue = (Category) category.getValue();
			    Subcategory subcategoryValue = (Subcategory) subcategory.getValue();
			    Brand brandValue = (Brand) brand.getValue();
			
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
			
			subcategory.setValue(null);
			brand.setValue(null);
			
			Category categoryValue = (Category) category.getValue();
			if (categoryValue!=null) {
				List<Subcategory> subcategoryList = showAllSubcategoriesService.getAllSubcategoriesForCategory(categoryValue.getCategoryId());
				if (subcategoryList.size() !=0) {
					isSaveOperationValidForSubcategory = true;
				}else {
					isSaveOperationValidForSubcategory = false;
				}
				subcategory.setItems(subcategoryList);
				
				
				List<Brand> brandList = showAllBrandsService.getAllBrandsForCategory(categoryValue.getCategoryId());
				if (brandList.size() !=0) {
					isSaveOperationValidForBrand = true;
				}else {
					isSaveOperationValidForBrand = false;
				}
				brand.setItems(brandList);
				
			}
				
		}
		
		
		
	}

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
   
   public Component createComponent(ProductSaveListener productSaveListener) {
    		return new EditProductLayout(productSaveListener).init().load().bind().layout();
    }
    	
    	
	public void editData(Object item) {
		this.product = (Product) item;
		
		//Set Pid
		if (product.getPid()!=null) {
			pid.setValue(product.getPid());
		}else {
			pid.setValue("");
		}
		//Set Gender
		if (product.getGender()!=null) {
			gender.setValue(product.getGender());
		}else {
			gender.setValue(null);
		}
		//Set Category
		if (product.getCategory()!=null) {
			category.setValue(product.getCategory());
		}else {
			category.setValue(null);
		}
		//Set Subcategory
		if (product.getSubcategory()!=null) {
			subcategory.setValue(product.getSubcategory());
		}else {
			subcategory.setValue(null);
		}
		
		//Set Brand
		if (product.getBrand()!=null) {
			brand.setValue(product.getBrand());
		}else {
			brand.setValue(null);
		}
				
		pid.setVisible(true);
		gender.setVisible(true);
		category.setVisible(true);
		subcategory.setVisible(true);
		brand.setVisible(true);
		editButton.setVisible(true);
		cancelButton.setVisible(true);
	}
	
	
		
}