package com.dataprice.ui.classification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Product;
import com.dataprice.service.addcategory.AddCategoryService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifycategory.ModifyCategoryService;
import com.dataprice.service.removecategory.RemoveCategoryService;
import com.dataprice.service.showallcategories.ShowAllCategoriesService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
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
				saveButton.setIcon(VaadinIcons.EDIT);			
				saveButton.addClickListener(this);				
				saveButton.setVisible(false);				
				saveButton.setWidth("100%");
				
				addNewButton = new Button("Add New Category");
				addNewButton.setIcon(VaadinIcons.PLUS_CIRCLE);				
				addNewButton.addClickListener(this);				
				addNewButton.setWidth("50%");
				
				editButton = new Button("Edit");
				editButton.setIcon(VaadinIcons.EDIT);			
				editButton.addClickListener(this);			
				editButton.setVisible(false);			
				editButton.setWidth("100%");
				
				cancelButton = new Button("Cancel");				
				cancelButton.setIcon(VaadinIcons.CLOSE);			
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
				
				//categoriesTable.setWidth("100%");
				categoriesTable.removeColumn("categoryId");
				
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
			 
			    HorizontalLayout h = new HorizontalLayout(saveButton,editButton);
			    h.setWidth("100%");
				h.setMargin(false);
				
			    VerticalLayout vl = new VerticalLayout(addNewButton,categoriesTable);
			    vl.setWidth("100%");
			    vl.setMargin(new MarginInfo(false, false, false, false));
			    
			    VerticalLayout v2 = new VerticalLayout(categoryName,h,cancelButton);
			    v2.setWidth("100%");
			    v2.setMargin(new MarginInfo(false, false, false, true));
			    
			    HorizontalSplitPanel h1 = new HorizontalSplitPanel();   
		        h1.setFirstComponent(vl);
		        h1.setSecondComponent(v2);
		        h1.setSplitPosition(70);
		        h1.setSizeFull();
		        
		        return h1;

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
			 
				modifycategoryservice.modifyCategory(category);
				refreshTable();
				cancel();
				Notification.show("EDIT","Category is editted",Type.WARNING_MESSAGE);
				
		}


		private void addNew() {
			 clearField();
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
				addcategoryservice.saveCategory(category);
				refreshTable();
				cancel();
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
		categoryName.setValue(category.getCategoryName());
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