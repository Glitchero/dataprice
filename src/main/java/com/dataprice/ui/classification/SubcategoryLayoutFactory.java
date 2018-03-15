package com.dataprice.ui.classification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Category;
import com.dataprice.model.entity.Subcategory;
import com.dataprice.service.addsubcategory.AddSubcategoryService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.modifysubcategory.ModifySubcategoryService;
import com.dataprice.service.removesubcategory.RemoveSubcategoryService;
import com.dataprice.service.showallcategories.ShowAllCategoriesService;
import com.dataprice.service.showallsubcategories.ShowAllSubcategoriesService;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@org.springframework.stereotype.Component
public class SubcategoryLayoutFactory implements UIComponentBuilder {
	
	 private List<Subcategory> subcategories;
		
	 private Grid<Subcategory> subcategoriesTable;
		
	 private Subcategory subcategory;
	 
	 private ComboBox category;
	 
	 private TextField subcategoryName;
	 
	 private Button saveButton;
	
	 private Button editButton;
	 
	 private Button addNewButton;
		
	 private Button cancelButton;
	 
	 private Binder<Subcategory> binder;
		
	private class SubcategoryLayout extends VerticalLayout implements Button.ClickListener{
		 
		
		 public SubcategoryLayout init() {

		    	binder = new Binder<>(Subcategory.class);

		    	subcategory = new Subcategory();
		    	
		    	category = new ComboBox("Category");
		    	category.setVisible(false);
		    	
		    	subcategoryName = new TextField("SubcategoryName");		    
		    	subcategoryName.setVisible(false);
		    	
				saveButton = new Button("Save");
				saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);				
				saveButton.addClickListener(this);				
				saveButton.setVisible(false);				
				saveButton.setWidth("100%");
				
				addNewButton = new Button("Add New Subcategory");
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
				subcategoriesTable = new Grid<>(Subcategory.class);
				//subcategoriesTable.setItems(subcategories);
				
				// Render a button that deletes the data row (item)
				subcategoriesTable.addColumn(Subcategory -> "Delete",
				      new ButtonRenderer(clickEvent -> {
				    	  subcategories.remove(clickEvent.getItem());
				    	  removeSubcategoryService.removeSubcategory((Subcategory) clickEvent.getItem());
				    	  subcategoriesTable.setItems(subcategories);
				    }))
				.setCaption("Delete")
				.setMaximumWidth(100)
				.setResizable(false);

				subcategoriesTable.addColumn(product -> "Edit",
				          new ButtonRenderer(clickEvent -> {
				        	  editData(clickEvent.getItem());
				        }))
				.setCaption("Edit")
				.setMaximumWidth(100)
				.setResizable(false);
				
				//subcategoriesTable.setWidth("100%");
				subcategoriesTable.removeColumn("subcategoryId");
				
				return this;
			    
		    }
		 
		 
		 public SubcategoryLayout bind() {
			    binder.forField(category)
			      .asRequired("Category is required")
			      .bind("category");
			 
				binder.forField(subcategoryName)
				  .asRequired("SubcategoryName is required")
				  .bind("subcategoryName");
				
				binder.readBean(subcategory);
				
				return this;
			}	
		 
		 
		 public Component layout() {		
		    	setMargin(true);
                
		    		    	
		    	GridLayout gridLayout = new GridLayout(2, 4);
		    	gridLayout.setSizeUndefined();
				gridLayout.setSpacing(true);
         
				HorizontalLayout h = new HorizontalLayout(saveButton,editButton);
				h.setWidth("100%");

				GridLayout g1 = new GridLayout(3, 4);
				g1.setSizeUndefined();
				g1.setSpacing(true);

				
				g1.addComponent(subcategoryName,0,0,2,0);
				g1.addComponent(category,0,1,2,1);
				g1.addComponent(h,0,2,2,2);
				g1.addComponent(cancelButton,0,3,2,3);
				
				gridLayout.addComponent(g1,1,1);
				gridLayout.addComponent(addNewButton,0,0);
				gridLayout.addComponent(subcategoriesTable,0,1,0,3);
				
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
			 category.setVisible(false);
			 subcategoryName.setVisible(false);
			 editButton.setVisible(false);
			 saveButton.setVisible(false);
			 cancelButton.setVisible(false);
			 clearField();
		}


		private void edit() {
			
			 try {
					binder.writeBean(subcategory);
				} catch (ValidationException e) {
					Notification.show("ERROR","Subcategory is not editted",Type.ERROR_MESSAGE);
					return;
				}
			    
				modifySubcategoryService.modifySubcategory(subcategory);
				//modifyProductService.updateCategoryFromSubcategory(subcategory.getCategory().getCategoryId(), subcategory.getSubcategoryId());
				refreshTable();
				cancel();
				Notification.show("EDIT","Subcategory is editted",Type.WARNING_MESSAGE);
				
		}


		private void addNew() {
			 clearField();
			 category.setVisible(true);
			 subcategoryName.setVisible(true);
			 saveButton.setVisible(true);
			 cancelButton.setVisible(true);
			 editButton.setVisible(false);
		}


		private void save() {
			
			if(!isSaveOperationValid()) {
				Notification.show("ERROR","Must have at least one category",Type.ERROR_MESSAGE);
				return;
			}
			
			try {
					binder.writeBean(subcategory);
			} catch (ValidationException e) {
					Notification.show("ERROR","Subcategory is not saved",Type.ERROR_MESSAGE);
					return;
			}
			      
				addSubcategoryService.saveSubcategory(subcategory);
				refreshTable();
				cancel();
				Notification.show("SAVE","Subcategory is saved",Type.WARNING_MESSAGE);
			}
		
		 
		 private void clearField() {
				subcategoryName.setValue("");
		        category.setValue(null); 
		 }

		 
		private boolean isSaveOperationValid() {
			return showAllcategoriesService.getAllCategories().size() !=0;
		}
			
			
		public SubcategoryLayout load() {
			subcategories = showAllSubcategoriesService.getAllSubcategories();    	
			subcategoriesTable.setItems(subcategories);
			List<Category> categories = showAllcategoriesService.getAllCategories();
			category.setItems(categories);
			return this;
		}
		 
		 
	}




	public void refreshTable() {
		subcategories = showAllSubcategoriesService.getAllSubcategories(); 
		subcategoriesTable.setItems(subcategories);
		
	}
	
	public void editData(Object item) {
		this.subcategory = (Subcategory) item;
		//Set the values from the edit!!
		subcategoryName.setValue(subcategory.getSubcategoryName());
		category.setValue(subcategory.getCategory());
		//Change visibility
		subcategoryName.setVisible(true);
		category.setVisible(false);
		editButton.setVisible(true);
		cancelButton.setVisible(true);
		saveButton.setVisible(false);
	}
	
	@Autowired
	private ModifyProductService modifyProductService;
	
	@Autowired
    private ShowAllCategoriesService showAllcategoriesService;
	
	@Autowired
    private AddSubcategoryService addSubcategoryService;
	
	@Autowired
    private ShowAllSubcategoriesService showAllSubcategoriesService;
	
	@Autowired
	private RemoveSubcategoryService removeSubcategoryService;
	
	@Autowired
	private ModifySubcategoryService modifySubcategoryService;
	
	
	@Override
	public Component createComponent() {
		return new SubcategoryLayout().init().load().bind().layout();
	}
	
	
}