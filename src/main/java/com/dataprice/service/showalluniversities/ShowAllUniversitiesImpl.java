package com.dataprice.service.showalluniversities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dataprice.model.entity.University;
import com.dataprice.repository.university.UniversityRepository;

@Service
public class ShowAllUniversitiesImpl implements ShowAllUniversities{

	@Autowired
	private UniversityRepository universityRepository;
	
	@Override
	public List<University> getAllUniversities() {
		return universityRepository.getAllUniversities();
	}

}
