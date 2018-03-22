package com.dataprice.service.showalltasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataprice.model.entity.Task;
import com.dataprice.repository.task.TaskRepository;

@Service
@Transactional(readOnly=true)
public class ShowAllTasksServiceImpl implements ShowAllTasksService{

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public List<Task> getAllTasks() {
		return taskRepository.getAllTasks();
	}

	@Override
	public List<Task> getAllPendingTasks(String status) {
		return taskRepository.getAllPendingTasks(status);
	}

}
