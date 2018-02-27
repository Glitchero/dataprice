package com.dataprice.service.showallstudents;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Student;
import com.dataprice.repository.student.StudentRepository;

@Service
public class ShowAllStudentsServiceImpl implements ShowAllStudentsService{

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public List<Student> getAllStudents() {
		return studentRepository.getAllStudents();
	}
	                                               
}
