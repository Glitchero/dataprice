package com.dataprice.repository.student;

import org.springframework.stereotype.Repository;
import com.dataprice.model.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

	@Query("select s from Student s order by s.firstName")
	List<Student> getAllStudents();
	
}
