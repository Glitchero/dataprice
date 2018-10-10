package com.dataprice.service.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Role;
import com.dataprice.model.entity.Settings;
import com.dataprice.model.entity.User;
import com.dataprice.repository.security.UserRepository;
import com.dataprice.repository.settings.SettingsRepository;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void save(String username, String password, Role role) {
		//Check if user already exists 
		if (userRepository.findByUsername(username)!=null) {
			User user = userRepository.findByUsername(username);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole(role);
			userRepository.save(user);
		}else {

		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(role);
		Settings settings = new Settings();
		settings.setUser(user);
		user.setSettings(settings);
		userRepository.save(user);
		}
		
	}
}