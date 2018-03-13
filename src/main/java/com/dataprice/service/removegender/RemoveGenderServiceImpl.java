package com.dataprice.service.removegender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Gender;
import com.dataprice.repository.gender.GenderRepository;

@Service
public class RemoveGenderServiceImpl implements RemoveGenderService {

	@Autowired
	private GenderRepository genderRepository;
	
	
	@Override
	public void removeGender(Gender gender) {
		genderRepository.delete(gender);
		
	}

}
