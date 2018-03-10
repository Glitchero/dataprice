package com.dataprice.ui.classification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.service.addcategory.AddCategoryService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifycategory.ModifyCategoryService;
import com.dataprice.service.removecategory.RemoveCategoryService;
import com.dataprice.service.showallcategories.ShowAllCategoriesService;
import com.dataprice.ui.students.UIComponentBuilder;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;
import com.dataprice.model.entity.Category;
import net.bytebuddy.asm.Advice.This;

@org.springframework.stereotype.Component
public class CategoryLayoutFactory implements UIComponentBuilder {
	
	 private List<Category> categories;
		
	 private Grid<Category> categoriesTable;
		
	 private Category category;
	 
	 private TextField categoryName;
	 
	 private Button saveButton;
	
	 private Button editButton;
	 
	 private Button addNewButton;
		
	 private Button cancelButton;
	 
	 private Binder<Category> binder;
		
	private class CategoryLayout extends VerticalLayout implements Button.ClickListener{
		 
		
		 public CategoryLayout init() {

		    	binder = new Binder<>(Category.class);

		    	category = new Category();
		    	categoryName = new TextField("CategoryName");		    
		    	categoryName.setVisible(false);
		    	
				saveButton = new Button("Save");
				saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);				
				saveButton.addClickListener(this);				
				saveButton.setVisible(false);				
				saveButton.setWidth("100%");
				
				addNewButton = new Button("Add New Category");
				addNewButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);				
				addNewButton.addClickListener(this);				
				addNewButton.setWidth("100%");
				
				editButton = new Button("Edit");
				editButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);				
				editButton.addClickListener(this);			
				editButton.setVisible(false);			
				editButton.setWidth("100%");
				
				cancelButton = new Button("Cancel");				
				cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);				
				cancelButton.addClickListener(this);				
				cancelButton.setVisible(false);
				
				cancelButton.setWidth("100%");
				categoriesTable = new Grid<>(Category.class);
				categoriesTable.setItems(categories);
				
				// Render a button that deletes the data row (item)
				categoriesTable.addColumn(Category -> "Delete",
				      new ButtonRenderer(clickEvent -> {
				    	  categories.remove(clickEvent.getItem());
				    	  removecategoryservice.removeCategory((Category) clickEvent.getItem());
				    	  categoriesTable.setItems(categories);
				    }))
				.setCaption("Delete")
				.setMaximumWidth(100)
				.setResizable(false);

				categoriesTable.addColumn(product -> "Edit",
				          new ButtonRenderer(clickEvent -> {
				        	  editData(clickEvent.getItem());
				        }))
				.setCaption("Edit")
				.setMaximumWidth(100)
				.setResizable(false);
				
				
				return this;
			    
		    }
		 
		 
		 public CategoryLayout bind() {
				binder.forField(categoryName)
				  .asRequired("CategoryName is required")
				  .bind("categoryName");
				
				binder.readBean(category);
				
				return this;
			}	
		 
		 
		 public Component layout() {		
		    	setMargin(true);
                
		    		    	
		    	GridLayout gridLayout = new GridLayout(2, 4);
		    	gridLayout.setSizeUndefined();
				gridLayout.setSpacing(true);
         
				HorizontalLayout h = new HorizontalLayout(saveButton,editButton);
				h.setWidth("100%");

				GridLayout g1 = new GridLayout(3, 3);
				g1.setSizeUndefined();
				g1.setSpacing(true);

				g1.addComponent(categoryName,0,0,2,0);
				g1.addComponent(h,0,1,2,1);
				g1.addComponent(cancelButton,0,2,2,2);
				
				
				gridLayout.addComponent(g1,1,1);
				gridLayout.addComponent(addNewButton,0,0);
				gridLayout.addComponent(categoriesTable,0,1,0,3);
				
				return gridLayout;

		    }

		 
		 @Override
			public void buttonClick(ClickEvent event) {
			 if (event.getSource()==saveButton)	{
            	 save();
             }else {
            	 if (event.getSource()==editButton)	{
            	     edit();	 
            	 }else {
            		 if (event.getSource()==cancelButton)	{
            			 cancel(); 
                	 }else {
            	  addNew();	 
                	 }
            	 }         	 
              }
			}
		 
		 
		 private void cancel() {
			 categoryName.setVisible(false);
			 editButton.setVisible(false);
			 saveButton.setVisible(false);
			 cancelButton.setVisible(false);
			 clearField();
		}


		private void edit() {
			
			 try {
					binder.writeBean(category);
				} catch (ValidationException e) {
					Notification.show("ERROR","Category is not editted",Type.ERROR_MESSAGE);
					return;
				}
				
			    cancel();
				
				modifycategoryservice.modifyCategory(category);
				//categoriesavedListener.categoriesaved();
				refreshTable();
				clearField();
				Notification.show("EDIT","Category is editted",Type.WARNING_MESSAGE);
				
		}


		private void addNew() {
			 categoryName.setVisible(true);
			 saveButton.setVisible(true);
			 cancelButton.setVisible(true);
			 editButton.setVisible(false);
		}


		private void save() {
				try {
					binder.writeBean(category);
				} catch (ValidationException e) {
					Notification.show("ERROR","Category is not saved",Type.ERROR_MESSAGE);
					return;
				}
				
				cancel();
				 
				addcategoryservice.saveCategory(category);
				//categoriesavedListener.categoriesaved();
				refreshTable();
				clearField();
				Notification.show("SAVE","Category is saved",Type.WARNING_MESSAGE);
			}
		
		 
		 private void clearField() {
				categoryName.setValue("");
		 }


		public CategoryLayout load() {
			categories = showAllcategoriesService.getAllCategories();    			
			return this;
		}
		 
		 
	}




	public void refreshTable() {
		categories = showAllcategoriesService.getAllCategories();
        categoriesTable.setItems(categories);
		
	}
	
	public void editData(Object item) {
		this.category = (Category) item;
		categoryName.setValue(category.toString());
		categoryName.setVisible(true);
		editButton.setVisible(true);
		cancelButton.setVisible(true);
		saveButton.setVisible(false);
	}
	
	@Autowired
    private AddCategoryService addcategoryservice;
	
	@Autowired
    private ShowAllCategoriesService showAllcategoriesService;
	
	@Autowired
	private RemoveCategoryService removecategoryservice;
	
	@Autowired
	private ModifyCategoryService modifycategoryservice;
	
	
	@Override
	public Component createComponent() {
		return new CategoryLayout().load().init().bind().layout();
	}
	
	
}