package com.dataprice.ui.tasks.TaskExecuteOrder;

import java.util.List;

import com.dataprice.model.entity.Task;

public class OriginalIterator implements ITaskIterator {

	protected List<Task> tasks;
	protected int counter = 0;
	
	
	public OriginalIterator(List<Task> tasks) {
	       this.tasks = tasks;
	}
	
	
	@Override
	public Task getNextTask() {
		  if (counter<tasks.size()){
				 Task task = tasks.get(counter); 
				 counter++;
				 return task;
			  }else{
				   return null;
			  }
	}

}
