package com.dataprice.service.showalltasks;

import java.util.List;

import com.dataprice.model.entity.Task;

public interface ShowAllTasksService {

	public List<Task> getAllTasks();

	public List<Task> getAllPendingTasks(String status);
	
	public List<Task> getAllTasksFromRetail(String retail);
}
