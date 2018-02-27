package com.dataprice.ui.students;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Student;
import com.dataprice.service.removestudent.RemoveStudentService;
import com.dataprice.service.showallstudents.ShowAllStudentsService;
import com.dataprice.utils.NotificationsMessages;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.themes.ValoTheme;

@SpringView
public class RemoveStudentLayoutFactory extends VerticalLayout implements View,Button.ClickListener{

	private Grid<Student> removeStudentTable;
	private Button removeStudentsButton;
	private List<Student> students;
	
	@Autowired
	private ShowAllStudentsService allStudentsService;
	
	@Autowired
	private RemoveStudentService removeStudentService;
	
	private void addLayout() {
		removeStudentsButton = new Button("Remove");
		setMargin(true);
		removeStudentTable = new Grid<>(Student.class);
		removeStudentTable.setColumnOrder("firstName", "lastName", "age", "gender");
		removeStudentTable.removeColumn("id");
		removeStudentTable.setItems(students);
		removeStudentTable.setSelectionMode(SelectionMode.MULTI);
		removeStudentsButton.addClickListener(this);
		removeStudentsButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		addComponent(removeStudentTable);
		addComponent(removeStudentsButton);

	}
	
	@PostConstruct
	void init() {
		  if (removeStudentTable!=null) return;
		  loadStudents();
		  addLayout();
	}

	private void loadStudents() {
		students = allStudentsService.getAllStudents();
		
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// This view is constructed in the init() method()
	}

	@Override
	public void buttonClick(ClickEvent event) {
		MultiSelectionModel<Student> selectionModel = (MultiSelectionModel<Student>) removeStudentTable.getSelectionModel();
		
		//List<Student> dataProvider = (List<Student>) removeStudentTable.getDataProvider();
		for(Student student : selectionModel.getSelectedItems()) {
			System.out.println("EL estudiante es: " + student);
			students.remove(student);
			removeStudentService.removeStudent(student);
		}
		
		Notification.show(NotificationsMessages.STUDENT_REMOVE_SUCCESS_TITLE.getString(),
				NotificationsMessages.STUDENT_REMOVE_SUCCESS_DESCRIPTION.getString(), Type.WARNING_MESSAGE);
		
		removeStudentTable.getDataProvider().refreshAll();

		
	}
	
	
}
