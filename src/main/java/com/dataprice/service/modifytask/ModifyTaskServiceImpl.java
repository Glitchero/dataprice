package com.dataprice.service.modifytask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Task;
import com.dataprice.repository.task.TaskRepository;

@Service
public class ModifyTaskServiceImpl implements ModifyTaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public void modifyTask(Task taskDAO) {
		taskRepository.save(taskDAO);
		
	}

}
