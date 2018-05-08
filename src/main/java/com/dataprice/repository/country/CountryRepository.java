package com.dataprice.repository.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer>{

}
