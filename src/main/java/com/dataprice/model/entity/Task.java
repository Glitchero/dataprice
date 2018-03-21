package com.dataprice.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
      private String status = "Pendiente a descarga";
	  
	  @Column(name = "progress")
      private Double progress = 0.0;
      
	  @Temporal(TemporalType.TIMESTAMP)
	  @Column(name = "run_datetime")
	  private Date runDateTime = null;
	  
	  @OneToMany(mappedBy="task",cascade=CascadeType.ALL)
		private List<Product> products;
	  
	  
      public Task() {
    	  this.products = new ArrayList<>();
      }

  //    public Task(String categoryName) {
  //		this();
  //		this.categoryName = categoryName;
  //	}
   
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

	public Double getProgress() {
			return progress;
	}

	public void setProgress(Double progress) {
			this.progress = progress;
	}

	public Date getRunDateTime() {
		return runDateTime;
	}

	public void setRunDateTime(Date runDateTime) {
		this.runDateTime = runDateTime;
	}

	@Override
	public String toString() {
		return taskName + "-" + retail + "-" + seed + "-" + status;
		
	}
}