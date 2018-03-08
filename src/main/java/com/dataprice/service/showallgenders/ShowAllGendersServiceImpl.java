package com.dataprice.service.showallgenders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Gender;
import com.dataprice.repository.gender.GenderRepository;

@Service
public class ShowAllGendersServiceImpl implements ShowAllGendersService{

	@Autowired
	private GenderRepository genderRepository;
	
	@Override
	public List<Gender> getAllGenders() {
		return genderRepository.getAllGenders();
	}

}
