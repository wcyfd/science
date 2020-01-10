package com.science.game.service.village.module;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Village;

@Component
public class CreateVillageModule {

	public void createVillage() {
		Village v = new Village();
		Data.villages.put(v.getId(), v);
	}
}
