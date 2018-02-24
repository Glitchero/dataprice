package com.dataprice.service.addstudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dataprice.repository.student.*;
import com.dataprice.model.entity.Student;

@Service
public class AddStudentServiceImpl implements AddStudentService{

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public void saveStudent(Student studentDAO) {
		
		Student student = new Student();
		student.setFirstName(studentDAO.getFirstName());
		student.setLastName(studentDAO.getLastName());
		student.setAge(studentDAO.getAge());
		student.setGender(studentDAO.getGender());
		studentRepository.save(student);
	}

}
