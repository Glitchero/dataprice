package com.dataprice.ui.tasks;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dataprice.model.entity.Task;
import com.dataprice.service.showalltasks.ShowAllTasksService;
import com.dataprice.ui.students.UIComponentBuilder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
public class ShowAllTasksLayoutFactory implements UIComponentBuilder{

	private List<Task> tasks;

	private Grid<Task> tasksTable;
	
	private class ShowAllTasksLayout extends VerticalLayout {

		public ShowAllTasksLayout init() {

			setMargin(true);
			tasksTable = new Grid<>(Task.class);
			tasksTable.setColumnOrder("taskName", "retail", "seed", "status");
			tasksTable.removeColumn("taskId");
			tasksTable.setItems(tasks);
			return this;
		}



		public ShowAllTasksLayout load() {
			tasks = showAllTasksService.getAllTasks();     			
			return this;
		}
		
		public ShowAllTasksLayout layout() {
			addComponent(tasksTable); //Add component to the verticalLayout, That's why we extend the class.
			return this;
		}
		
	}

	@Autowired
	private ShowAllTasksService showAllTasksService;
	
	public Component createComponent() {
		return new ShowAllTasksLayout().load().init().layout();
	}

	public void refreshTable() {
             tasks = showAllTasksService.getAllTasks();
             tasksTable.setItems(tasks);
	}
	
}
