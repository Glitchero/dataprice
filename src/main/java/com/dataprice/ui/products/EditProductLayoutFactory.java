package com.dataprice.ui.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Product;
import com.dataprice.model.entity.Task;
import com.dataprice.model.entity.University;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.utils.NotificationsMessages;
import com.dataprice.utils.StudentsStringUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class EditProductLayoutFactory {

   private Product product;
	 
   private class EditProductLayout extends VerticalLayout implements Button.ClickListener{

		 private ComboBox gender;
		
		 private Button editButton;
		
		 private ProductSaveListener productSaveListener;

		 
		 private Binder<Product> binder;
		 
		 
		public EditProductLayout(ProductSaveListener productSaveListener) {
			this.productSaveListener = productSaveListener;
		}

		
		public EditProductLayout init() {
		   	binder = new Binder<>(Product.class);
			gender = new ComboBox("Genders");
			editButton = new Button("Editar");
			editButton.addClickListener(this);
			return this;
		}

		public EditProductLayout load() {
			List<Gender> genderList = showAllGendersService.getAllGenders();
			gender.setItems(genderList);
			return this;
		}


		public Component layout() {
			 setMargin(true);
			 HorizontalLayout h = new HorizontalLayout();
			 h.addComponent(gender);
			 h.addComponent(editButton);
			 return h;
		}
		
		
		
		@Override
		public void buttonClick(ClickEvent event) {
			save();
		}


		private void save() {
			
			try {
				binder.writeBean(product);
			} catch (ValidationException e) {
				Notification.show(NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_TITLE.getString(),NotificationsMessages.STUDENT_SAVE_VALIDATION_ERROR_DESCRIPTION.getString(),Type.ERROR_MESSAGE);
				return;
			}
			//System.out.println(student);
			modifyProductService.modifyProduct(product);
			productSaveListener.productSaved();
			
		}


		public EditProductLayout bind() {
			binder.forField(gender)
			  .asRequired("genders must be set")
			  .bind("gender");
					
			binder.readBean(product);
			
			return this;
		}
		
		
	}

        
   @Autowired
   private ShowAllGendersService showAllGendersService;
   
   @Autowired
   private ModifyProductService modifyProductService;
   
   
   public Component createComponent(ProductSaveListener productSaveListener) {
    		return new EditProductLayout(productSaveListener).init().load().bind().layout();
    }
    	
    	
	public void loadData(Object item) {
		this.product = (Product) item;
	}
		
		
}