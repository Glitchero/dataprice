package com.dataprice.service.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dataprice.model.entity.User;
import com.dataprice.repository.security.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Should check for empty users!! Do it later!!
	 * https://stackoverflow.com/questions/37615034/spring-security-spring-boot-how-to-set-roles-for-users
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		 User user = userRepository.findByUsername(username);
		 List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
	     list.add(new SimpleGrantedAuthority(user.getRole().name()));
		return new CustomUserDetails(user.getUsername(), user.getPassword(), true, true, true, true, list);
	}
	
	
    public User getUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		return user;
	}
    
    public List<User> getAllUsers(){
    	return userRepository.getAllUsers();
    }


}