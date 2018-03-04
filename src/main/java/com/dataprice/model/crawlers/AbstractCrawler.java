package com.dataprice.model.crawlers;

import com.dataprice.model.entity.Task;

public abstract class AbstractCrawler implements Crawler {

	protected Task task;

	public AbstractCrawler() {
		
	}
	
	public AbstractCrawler(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	@Override
	public String toString()
	{
		return this.task.getRetail() + "<-->" + this.getTask().getSeed();
	}	
	

}
