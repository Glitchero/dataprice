package com.dataprice.ui.classification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Brand;
import com.dataprice.model.entity.Category;
import com.dataprice.service.addbrand.AddBrandService;
import com.dataprice.service.modifybrand.ModifyBrandService;
import com.dataprice.service.modifyproduct.ModifyProductService;
import com.dataprice.service.removebrand.RemoveBrandService;
import com.dataprice.service.showallbrands.ShowAllBrandsService;
import com.dataprice.service.showallcategories.ShowAllCategoriesService;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
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

@org.springframework.stereotype.Component
public class BrandLayoutFactory implements UIComponentBuilder{

	private List<Brand> brands;
	
	 private Grid<Brand> brandsTable;
		
	 private Brand brand;
	 
	 private ComboBox category;
	 
	 private TextField brandName;
	 
	 private Button saveButton;
	
	 private Button editButton;
	 
	 private Button addNewButton;
		
	 private Button cancelButton;
	 
	 private Binder<Brand> binder;
	 
	 
	 private class BrandLayout extends VerticalLayout implements Button.ClickListener{
		 
			
		 public BrandLayout init() {

		    	binder = new Binder<>(Brand.class);

		    	brand = new Brand();
		    	
		    	category = new ComboBox("Category");
		    	category.setVisible(false);
		    	
		    	brandName = new TextField("BrandName");		    
		    	brandName.setVisible(false);
		    	
				saveButton = new Button("Save");
				saveButton.setIcon(VaadinIcons.EDIT);			
				saveButton.addClickListener(this);				
				saveButton.setVisible(false);				
				saveButton.setWidth("100%");
				
				addNewButton = new Button("Add New Brand");
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
				brandsTable = new Grid<>(Brand.class);
				
				
				// Render a button that deletes the data row (item)
				brandsTable.addColumn(Brands -> "Delete",
				      new ButtonRenderer(clickEvent -> {
				    	  brands.remove(clickEvent.getItem());
				    	  removeBrandService.removeBrand((Brand) clickEvent.getItem());
				    	  brandsTable.setItems(brands);
				    }))
				.setCaption("Delete")
				.setMaximumWidth(100)
				.setResizable(false);

				brandsTable.addColumn(product -> "Edit",
				          new ButtonRenderer(clickEvent -> {
				        	  editData(clickEvent.getItem());
				        }))
				.setCaption("Edit")
				.setMaximumWidth(100)
				.setResizable(false);
				
				//brandsTable.setWidth("100%");
				brandsTable.removeColumn("brandId");
				
				return this;
			    
		    }
		 
		 
		 public BrandLayout bind() {
			    binder.forField(category)
			      .asRequired("Category is required")
			      .bind("category");
			 
				binder.forField(brandName)
				  .asRequired("BrandName is required")
				  .bind("brandName");
				
				binder.readBean(brand);
				
				return this;
			}	
		 
		 
		 public Component layout() {	
			 /**
		    	setMargin(true);
                
		    		    	
		    	GridLayout gridLayout = new GridLayout(2, 4);
		    	gridLayout.setSizeUndefined();
				gridLayout.setSpacing(true);
         
				HorizontalLayout h = new HorizontalLayout(saveButton,editButton);
				h.setWidth("100%");

				GridLayout g1 = new GridLayout(3, 4);
				g1.setSizeUndefined();
				g1.setSpacing(true);

				
				g1.addComponent(brandName,0,0,2,0);
				g1.addComponent(category,0,1,2,1);
				g1.addComponent(h,0,2,2,2);
				g1.addComponent(cancelButton,0,3,2,3);
				
				gridLayout.addComponent(g1,1,1);
				gridLayout.addComponent(addNewButton,0,0);
				gridLayout.addComponent(brandsTable,0,1,0,3);
				
				return gridLayout;
               */
			 
			 HorizontalLayout h = new HorizontalLayout(saveButton,editButton);
			    h.setWidth("100%");
				h.setMargin(false);
				
			    VerticalLayout vl = new VerticalLayout(addNewButton,brandsTable);
			    vl.setWidth("100%");
			    vl.setMargin(new MarginInfo(false, false, false, false));
			    
			    VerticalLayout v2 = new VerticalLayout(brandName,category,h,cancelButton);
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
			 category.setVisible(false);
			 brandName.setVisible(false);
			 editButton.setVisible(false);
			 saveButton.setVisible(false);
			 cancelButton.setVisible(false);
			 clearField();
		}


		private void edit() {
			
			 try {
					binder.writeBean(brand);
				} catch (ValidationException e) {
					Notification.show("ERROR","Brand is not editted",Type.ERROR_MESSAGE);
					return;
				}
			    
				modifyBrandService.modifyBrand(brand);
				//modifyProductService.updateCategoryFromSubcategory(subcategory.getCategory().getCategoryId(), subcategory.getSubcategoryId());
				refreshTable();
				cancel();
				Notification.show("EDIT","Brand is editted",Type.WARNING_MESSAGE);
				
		}


		private void addNew() {
			 clearField();
			 category.setVisible(true);
			 brandName.setVisible(true);
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
					binder.writeBean(brand);
			} catch (ValidationException e) {
					Notification.show("ERROR","Brand is not saved",Type.ERROR_MESSAGE);
					return;
			}
			      
				addBrandService.saveBrand(brand);
				refreshTable();
				cancel();
				Notification.show("SAVE","Brand is saved",Type.WARNING_MESSAGE);
			}
		
		 
		 private void clearField() {
				brandName.setValue("");
		        category.setValue(null); 
		 }

		 
		private boolean isSaveOperationValid() {
			return showAllcategoriesService.getAllCategories().size() !=0;
		}
			
			
		public BrandLayout load() {
			brands = showAllBrandsService.getAllBrands();    	
			brandsTable.setItems(brands);
			List<Category> categories = showAllcategoriesService.getAllCategories();
			category.setItems(categories);
			return this;
		}
		 
		 
	}




	public void refreshTable() {
		brands = showAllBrandsService.getAllBrands(); 
		brandsTable.setItems(brands);
		
	}
	
	public void editData(Object item) {
		this.brand = (Brand) item;
		//Set the values from the edit!!
		brandName.setValue(brand.getBrandName());
		category.setValue(brand.getCategory());
		//Change visibility
		brandName.setVisible(true);
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
    private AddBrandService addBrandService;
	
	@Autowired
    private ShowAllBrandsService showAllBrandsService;
	
	@Autowired
	private RemoveBrandService removeBrandService;
	
	@Autowired
	private ModifyBrandService modifyBrandService;
	
	
	@Override
	public Component createComponent() {
		return new BrandLayout().init().load().bind().layout();
	}
	
}
