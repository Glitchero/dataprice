package com.dataprice.repository.task;


import org.springframework.stereotype.Repository;
import com.dataprice.model.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {

	@Query("select t from Task t order by t.taskName")
	List<Task> getAllTasks();
	
	@Query("select t from Task t where t.status=:statusKey")
	List<Task> getAllTasksByStatus(@Param("statusKey") String statusKey);
	
	@Query("select t from Task t where t.retail.retailId=:retailKey")
	List<Task> getAllTasksFromRetailId(@Param("retailKey") Integer retailKey);
	
	@Query("select count(t.taskId) from Task t")
	Integer getNumOfTasks();
	
	/**
	@Query("select count(t.taskId) from Task t where t.status:='Escaneando' or t.status:='Descargando'")
	Integer getNumOfWorkingTasks();
	
	@Query("select count(t.taskId) from Task t where t.status:='Cancelado'")
	Integer getNumOfCanceledTasks();
	
	
	
	@Query("select count(t.taskId) from Task t where t.status:='Pendiente'")
	Integer getNumOfPendingTasks();
	*/
	
	@Query("select count(t.taskId) from Task t where t.status=:statusKey")
	Integer getNumOfTasksByStatus(@Param("statusKey") String statusKey);
	
	@Query("select count(t.taskId) from Task t where t.retail.retailName=:retailKey")
	Integer getNumOfTasksByRetail(@Param("retailKey") String retailKey);
	
	@Query("select t.retail.retailName from Task t group by t.retail.retailName")
	List<String> getRetailersUsed();
	
}