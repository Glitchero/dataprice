package com.dataprice.service.removestudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Student;
import com.dataprice.repository.student.StudentRepository;

@Service
public class RemoveStudentServiceImpl implements RemoveStudentService{

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public void removeStudent(Student student) {
		studentRepository.delete(student);
		
	}

}
