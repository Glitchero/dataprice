package com.dataprice.service.modifygender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Gender;
import com.dataprice.repository.gender.GenderRepository;

@Service
public class ModifyGenderServiceImpl implements ModifyGenderService{

	@Autowired
	private GenderRepository genderRepository;
	
	@Override
	public void modifyGender(Gender genderDAO) {
		genderRepository.save(genderDAO);
		
	}

}
