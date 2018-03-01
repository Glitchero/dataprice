package com.dataprice.service.removetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Task;
import com.dataprice.repository.task.TaskRepository;

@Service
public class RemoveTaskServiceImpl implements RemoveTaskService {

	@Autowired
	private TaskRepository taskRepository;
	

	@Override
	public void removeTask(Task task) {
		taskRepository.delete(task);
		
	}
}
