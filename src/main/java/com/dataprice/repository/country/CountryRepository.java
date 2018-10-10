package com.dataprice.repository.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer>{

	
	@Query("select c.countryName from Country c order by c.countryName")
	List<String> getAllCountriesNames();	
	
}
