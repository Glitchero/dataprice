package com.dataprice.model.entity;

public class TaskJson {

	private String taskName;
	
	private Integer retailId;
	
	private String seed;

	
	
	public TaskJson(String taskName, Integer retailId, String seed) {
		this.taskName = taskName;
		this.retailId = retailId;
		this.seed = seed;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getRetailId() {
		return retailId;
	}

	public void setRetailId(Integer retailId) {
		this.retailId = retailId;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}
	
	@Override
	public String toString() {
		return taskName + "-" + retailId + "-" + seed;
		
	}
	
}
