package com.dataprice.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.dataprice.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.username=:username")
	User findByUsername(@Param("username") String username);
	
	@Query("select u from User u where u.username<>:admin")
	List<User> getAllUsersExceptAdmin(@Param("admin") String admin);
	
}
