package com.dataprice.ui.students;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Student;
import com.dataprice.service.showallstudents.ShowAllStudentsService;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class ShowAllStudentsLayoutFactory implements UIComponentBuilder{

	private List<Student> students;

	private Grid<Student> studentsTable;
	
	private class ShowAllStudentsLayout extends VerticalLayout {

		


		public ShowAllStudentsLayout init() {

			setMargin(true);
			studentsTable = new Grid<>(Student.class);
			studentsTable.setColumnOrder("firstName", "lastName", "age", "gender");
			studentsTable.removeColumn("id");
			studentsTable.setItems(students);
			return this;
		}



		public ShowAllStudentsLayout load() {
			students = showAllStudentsService.getAllStudents();
			return this;
		}
		
		public ShowAllStudentsLayout layout() {
			addComponent(studentsTable); //Add component to the verticalLayout, That's why we extend the class.
			return this;
		}
		
	}

	@Autowired
	private ShowAllStudentsService showAllStudentsService;
	
	public Component createComponent() {
		return new ShowAllStudentsLayout().load().init().layout();
	}

	public void refreshTable() {
             students = showAllStudentsService.getAllStudents();
             studentsTable.setItems(students);
	}
	
}
