package com.dataprice.repository.task;


import org.springframework.stereotype.Repository;
import com.dataprice.model.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {

	@Query("select t from Task t order by t.taskName")
	List<Task> getAllTasks();

	
}