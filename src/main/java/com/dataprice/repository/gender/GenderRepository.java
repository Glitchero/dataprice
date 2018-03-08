package com.dataprice.repository.gender;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Gender;


	@Repository
	public interface GenderRepository extends JpaRepository<Gender,Integer>{
		
		@Query("select g from Gender g order by g.genderName")
		List<Gender> getAllGenders();	
		
		

}
