package com.dataprice.ui.tasks.TaskExecuteOrder;

import com.dataprice.model.entity.Task;

public interface ITaskIterator {

	/**
	 * The only way of breaking this method is by returning null;
	 * Null should be returned just once and in the end.
	 * @return
	 */
	public Task getNextTask();
	
}
