package com.dataprice.repository.student;

import org.springframework.stereotype.Repository;
import com.dataprice.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

}
