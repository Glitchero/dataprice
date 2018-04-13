package com.dataprice.service.modifysettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataprice.model.entity.Settings;
import com.dataprice.repository.settings.SettingsRepository;

@Service
public class ModifySettingsServiceImpl implements ModifySettingsService {

	@Autowired
	private SettingsRepository settingsRepository;
	
	@Override
	public void modifySettings(Settings settings) {
		settingsRepository.save(settings);
		
	}

}
