package com.dataprice.ui.tasks;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Student;
import com.dataprice.model.entity.Task;
import com.dataprice.service.removestudent.RemoveStudentService;
import com.dataprice.service.removetask.RemoveTaskService;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.students.UIComponentBuilder;
import com.dataprice.utils.NotificationsMessages;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class ShowAllTasksLayoutFactory implements UIComponentBuilder{

	private List<Task> tasks;
	private Button removeTasksButton;
	private Grid<Task> tasksTable;
	
	private class ShowAllTasksLayout extends VerticalLayout implements Button.ClickListener{

		public ShowAllTasksLayout init() {
			removeTasksButton = new Button("Remove");
			setMargin(true);
			tasksTable = new Grid<>(Task.class);
			tasksTable.setColumnOrder("taskName", "retail", "seed", "status");
			tasksTable.removeColumn("taskId");
			tasksTable.setItems(tasks);
			tasksTable.setSelectionMode(SelectionMode.MULTI);
			removeTasksButton.addClickListener(this);
			removeTasksButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			return this;
		}



		public ShowAllTasksLayout load() {
			tasks = showAllTasksService.getAllTasks();     			
			return this;
		}
		
		public ShowAllTasksLayout layout() {
			addComponent(tasksTable); //Add component to the verticalLayout, That's why we extend the class.
			addComponent(removeTasksButton);
			return this;
		}



		@Override
		public void buttonClick(ClickEvent event) {
			MultiSelectionModel<Task> selectionModel = (MultiSelectionModel<Task>) tasksTable.getSelectionModel();
			
			for(Task task : selectionModel.getSelectedItems()) {
				//System.out.println("EL tas es: " + task);
				tasks.remove(task);
				removeTaskService.removeTask(task);
			}
			
			Notification.show("REMOVE","Tasks have been removed", Type.WARNING_MESSAGE);
			
			tasksTable.getDataProvider().refreshAll();

			
		}
		
	}

	@Autowired
	private ShowAllTasksService showAllTasksService;
	
	@Autowired
	private RemoveTaskService removeTaskService;
	
	public Component createComponent() {
		return new ShowAllTasksLayout().load().init().layout();
	}

	public void refreshTable() {
             tasks = showAllTasksService.getAllTasks();
             tasksTable.setItems(tasks);
	}

	
}
