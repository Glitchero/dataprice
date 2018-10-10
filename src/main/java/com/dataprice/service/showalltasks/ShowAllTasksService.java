package com.dataprice.service.showalltasks;

import java.util.List;

import com.dataprice.model.entity.Task;

public interface ShowAllTasksService {

	public List<Task> getAllTasks();
	
	public List<Task> getAllTasksByStatus(String status);
	
	public List<Task> getAllTasksFromRetailId(Integer retailId);
	
	public Integer getNumOfTasks();
	
}
