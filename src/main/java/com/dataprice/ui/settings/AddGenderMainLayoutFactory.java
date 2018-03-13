package com.dataprice.ui.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Gender;
import com.dataprice.service.addgenderservice.AddGenderService;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.removegender.RemoveGenderService;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.ui.UIComponentBuilder;
import com.dataprice.ui.tasks.TaskSavedListener;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
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

@org.springframework.stereotype.Component
public class AddGenderMainLayoutFactory implements UIComponentBuilder {
	
	 private List<Gender> genders;
		
	 private Grid<Gender> gendersTable;
		
		
	private class AddGenderMainLayout extends VerticalLayout implements Button.ClickListener{
		
		private TextField genderName;
		
		private Button saveButton;
		 
		private Binder<Gender> binder;
		
		private Gender gender;
		
	//	private GenderSavedListener genderSavedListener;
		
	//	 public AddGenderMainLayout(GenderSavedListener genderSavedListener) {
	//			this.genderSavedListener = genderSavedListener;
	//		}
		 
		
		 public AddGenderMainLayout init() {

		    	binder = new Binder<>(Gender.class);

		    	gender = new Gender();
		    	
		    	genderName = new TextField("GenderName");
		    
				saveButton = new Button("save");

				saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				saveButton.addClickListener(this);
				
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

				return this;
			    
		    }
		 
		 
		 public AddGenderMainLayout bind() {
				binder.forField(genderName)
				  .asRequired("genderName is required")
				  .bind("genderName");
				
				binder.readBean(gender);
				
				return this;
			}	
		 
		 
		 public Component layout() {		
		    	setMargin(true);

				HorizontalLayout horizontalLayout = new HorizontalLayout();
				horizontalLayout.addComponent(genderName);
				horizontalLayout.addComponent(saveButton);
				horizontalLayout.addComponent(gendersTable);
				return horizontalLayout;

		    }

		 
		 @Override
			public void buttonClick(ClickEvent event) {
	                	 save();
			}
		 
		 private void save() {
				try {
					binder.writeBean(gender);
				} catch (ValidationException e) {
					Notification.show("ERROR","Gender is not saved",Type.ERROR_MESSAGE);
					return;
				}
				addGenderService.saveGender(gender);
				//genderSavedListener.genderSaved();
				refreshTable();
				clearField();
				Notification.show("SAVE","Gender is saved",Type.WARNING_MESSAGE);
			}
		
		 
		 private void clearField() {
				genderName.setValue("");
		 }


		public AddGenderMainLayout load() {
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
	
	
	@Autowired
    private ShowAllGendersService showAllGendersService;
	
	@Autowired
	private RemoveGenderService removeGenderService;
	
	
	@Override
	public Component createComponent() {
		return new AddGenderMainLayout().load().init().bind().layout();
	}
	
	
}
