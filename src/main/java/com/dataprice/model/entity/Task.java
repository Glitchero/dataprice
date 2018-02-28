package com.dataprice.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TASK")
public class Task {
	
	  @Id
	  @GeneratedValue
	  @Column(name = "task_id")
      private Integer taskId;
      
	  @Column(name = "task_name")
      private String taskName;
      
	  @Column(name = "retail")
      private String retail;
       
	  @Column(name = "seed")
      private String seed;
	  
	  @Column(name = "status")
      private String status;
      
      public Task() {
    	  
      }


   
	public Integer getTaskId() {
		return taskId;
	}



	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}



	public String getTaskName() {
		return taskName;
	}



	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}



	public String getRetail() {
		return retail;
	}



	public void setRetail(String retail) {
		this.retail = retail;
	}



	public String getSeed() {
		return seed;
	}



	public void setSeed(String seed) {
		this.seed = seed;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	@Override
	public String toString() {
		return taskName + "-" + retail + "-" + seed;
		
	}
}