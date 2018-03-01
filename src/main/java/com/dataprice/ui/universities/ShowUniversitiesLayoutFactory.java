package com.dataprice.ui.universities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Student;
import com.dataprice.model.entity.University;
import com.dataprice.service.showalluniversities.ShowAllUniversities;
import com.dataprice.ui.students.UIComponentBuilder;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class ShowUniversitiesLayoutFactory implements UIComponentBuilder {

	@Autowired
	private ShowAllUniversities showUniversitiesService;
	
	private List<University> universities;
	
	private Grid<University> universityTable;
	
	private class ShowUniversityLayout extends VerticalLayout {

	
		public ShowUniversityLayout init() {

			setMargin(true);
			universityTable = new Grid<>(University.class);
			universityTable.setColumnOrder("universityName", "universityCountry", "universityCity");
			universityTable.removeColumn("id");
			universityTable.setItems(universities);
			return this;
		
		}

		public ShowUniversityLayout layout() {
			addComponent(universityTable);
			return this;
		}

		public ShowUniversityLayout load() {
			universities = showUniversitiesService.getAllUniversities();
			return this;
		}
		
		
		
	}
	
	public void refreshTable() {
		universities = showUniversitiesService.getAllUniversities();
		universityTable.setItems(universities);
	}
	
	@Override
	public Component createComponent() {
		return new ShowUniversityLayout().load().init().layout();
	}


	

}
