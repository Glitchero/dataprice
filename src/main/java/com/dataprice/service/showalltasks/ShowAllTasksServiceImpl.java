package com.dataprice.service.showalltasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Task;
import com.dataprice.repository.student.StudentRepository;
import com.dataprice.repository.task.TaskRepository;

@Service
public class ShowAllTasksServiceImpl implements ShowAllTasksService{

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public List<Task> getAllTasks() {
		return taskRepository.getAllTasks();
	}

}
