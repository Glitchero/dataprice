package com.dataprice.service.addtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Task;
import com.dataprice.repository.task.TaskRepository;

@Service
public class AddTaskServiceImpl implements AddTaskService{

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public void saveTask(Task taskDAO) {
		
		Task task = new Task();
		task.setTaskName(taskDAO.getTaskName());
		task.setRetail(taskDAO.getRetail());
		task.setSeed(taskDAO.getSeed());
		task.setStatus(taskDAO.getStatus());
		taskRepository.save(task);
	}

}