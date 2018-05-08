package com.dataprice.service.security;

import com.dataprice.model.entity.Role;

public interface RegisterUserService {
	public void save(String username, String password, Role role);
}
