package com.dataprice.service.addgenderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Gender;
import com.dataprice.model.entity.Product;
import com.dataprice.repository.gender.GenderRepository;
import com.dataprice.repository.product.ProductRepository;

@Service
public class AddGenderServiceImpl implements AddGenderService {

	@Autowired
	private GenderRepository genderRepository;
	
	
	@Override
	public void saveGender(Gender genderDAO) {
		Gender gender = new Gender();
		gender.setGenderName(genderDAO.getGenderName());
		genderRepository.save(gender);
		
	}

}
