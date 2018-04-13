package com.dataprice.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dataprice.model.entity.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Integer>{

	@Query("select s from Settings s where s.id=:userId")
	Settings findSettingByUserid( @Param("userId") Integer userId);
	
}
