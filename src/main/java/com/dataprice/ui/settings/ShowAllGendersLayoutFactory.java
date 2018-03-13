package com.dataprice.ui.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Task;
import com.dataprice.service.addtask.AddTaskService;
import com.dataprice.service.removegender.RemoveGenderService;
import com.dataprice.service.removetask.RemoveTaskService;
import com.dataprice.service.showallgenders.ShowAllGendersService;
import com.dataprice.ui.UIComponentBuilder;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

@org.springframework.stereotype.Component
public class ShowAllGendersLayoutFactory implements UIComponentBuilder {

    private List<Gender> genders;
	
	private Grid<Gender> gendersTable;
	
	private class ShowAllGendersLayout extends VerticalLayout{
		
		public ShowAllGendersLayout init() {
			setMargin(true);
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
		
		public ShowAllGendersLayout layout() {
			addComponent(gendersTable);
			return this;
		}

		public ShowAllGendersLayout load() {
			genders = showAllGendersService.getAllGenders();    			
			return this;
		}
		
	}
	
	
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
		return new ShowAllGendersLayout().load().init().layout();
	}

}
