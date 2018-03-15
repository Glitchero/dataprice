package com.dataprice.ui.classification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Product;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.modifygender.ModifyGenderService;
import com.dataprice.service.removegender.RemoveGenderService;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.ui.UIComponentBuilder;
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

import net.bytebuddy.asm.Advice.This;

import static com.dataprice.ui.EasyLayout.*;

@org.springframework.stereotype.Component
public class GenderLayoutFactory implements UIComponentBuilder {
	
	 private List<Gender> genders;
		
	 private Grid<Gender> gendersTable;
		
	 private Gender gender;
	 
	 private TextField genderName;
	 
	 private Button saveButton;
	
	 private Button editButton;
	 
	 private Button addNewButton;
		
	 private Button cancelButton;
	 
	 private Binder<Gender> binder;
		
	private class GenderLayout extends VerticalLayout implements Button.ClickListener{
		
		
		
		
		
		
		
	//	private GenderSavedListener genderSavedListener;
		
	//	 public AddGenderMainLayout(GenderSavedListener genderSavedListener) {
	//			this.genderSavedListener = genderSavedListener;
	//		}
		 
		
		 public GenderLayout init() {

		    	binder = new Binder<>(Gender.class);

		    	gender = new Gender();
		    	
		    	genderName = new TextField("GenderName");
		    
		    	genderName.setVisible(false);
		    	
				saveButton = new Button("Save");

				saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				saveButton.addClickListener(this);
				
				saveButton.setVisible(false);
				
				saveButton.setWidth("100%");
				
				addNewButton = new Button("Add New Gender");

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
				gendersTable = new Grid<>(Gender.class);
				gendersTable.setItems(genders);
				
				// Render a button that deletes the data row (item)
				gendersTable.addColumn(gender -> "Delete",
				      new ButtonRenderer(clickEvent -> {
				    	  genders.remove(clickEvent.getItem());
				    	  removeGenderService.removeGender((Gender) clickEvent.getItem());
				    	  gendersTable.setItems(genders);
				    }))
				.setCaption("Delete")
				.setMaximumWidth(100)
				.setResizable(false);

				gendersTable.addColumn(product -> "Edit",
				          new ButtonRenderer(clickEvent -> {
				        	  editData(clickEvent.getItem());
				        }))
				.setCaption("Edit")
				.setMaximumWidth(100)
				.setResizable(false);
				
				//gendersTable.setHeight("100%");
				gendersTable.removeColumn("genderId");
				return this;
			    
		    }
		 
		 
		 public GenderLayout bind() {
				binder.forField(genderName)
				  .asRequired("genderName is required")
				  .bind("genderName");
				
				binder.readBean(gender);
				
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

				g1.addComponent(genderName,0,0,2,0);
				g1.addComponent(h,0,1,2,1);
				g1.addComponent(cancelButton,0,2,2,2);
				
				
				gridLayout.addComponent(g1,1,1);
				gridLayout.addComponent(addNewButton,0,0);
				gridLayout.addComponent(gendersTable,0,1,0,3);
				gridLayout.setHeight("100%");
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
			 genderName.setVisible(false);
			 editButton.setVisible(false);
			 saveButton.setVisible(false);
			 cancelButton.setVisible(false);
			 clearField();
		}


		private void edit() {
			
			 try {
					binder.writeBean(gender);
				} catch (ValidationException e) {
					Notification.show("ERROR","Gender is not saved",Type.ERROR_MESSAGE);
					return;
				}
					
				modifyGenderService.modifyGender(gender);
				refreshTable();
				cancel();
				Notification.show("SAVE","Gender is saved",Type.WARNING_MESSAGE);
				
		}


		private void addNew() {
			 clearField();
			 genderName.setVisible(true);
			 saveButton.setVisible(true);
			 cancelButton.setVisible(true);
			 editButton.setVisible(false);
		}


		private void save() {
				try {
					binder.writeBean(gender);
				} catch (ValidationException e) {
					Notification.show("ERROR","Gender is not saved",Type.ERROR_MESSAGE);
					return;
				}
		 
				addGenderService.saveGender(gender);
				refreshTable();
				cancel();
				Notification.show("SAVE","Gender is saved",Type.WARNING_MESSAGE);
			}
		
		 
		 private void clearField() {
				genderName.setValue("");
		 }


		public GenderLayout load() {
			genders = showAllGendersService.getAllGenders();    			
			return this;
		}
		 
		 
	}

	@Autowired
    private AddGenderService addGenderService;
	
	//public Component createComponent(GenderSavedListener genderSavedListener) {
	//	return new AddGenderMainLayout(genderSavedListener).init().bind().layout();
	//}

	public void refreshTable() {
		genders = showAllGendersService.getAllGenders();
        gendersTable.setItems(genders);
		
	}
	
	public void editData(Object item) {
		this.gender = (Gender) item;
		genderName.setValue(gender.getGenderName());
		genderName.setVisible(true);
		editButton.setVisible(true);
		cancelButton.setVisible(true);
		saveButton.setVisible(false);
	}
	
	
	@Autowired
    private ShowAllGendersService showAllGendersService;
	
	@Autowired
	private RemoveGenderService removeGenderService;
	
	@Autowired
	private ModifyGenderService modifyGenderService;
	
	
	@Override
	public Component createComponent() {
		return new GenderLayout().load().init().bind().layout();
	}
	
	
}