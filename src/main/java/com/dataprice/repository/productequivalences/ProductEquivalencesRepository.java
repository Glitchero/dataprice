package com.dataprice.repository.productequivalences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataprice.model.entity.ProductEquivalences;
import com.dataprice.model.entity.Settings;

@Repository
public interface ProductEquivalencesRepository extends JpaRepository<ProductEquivalences, String>{

}
