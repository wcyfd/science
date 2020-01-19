package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.I;
import com.science.game.entity.config.PlaceConfig;

@Component
public class PlaceConfigCache implements IConfigCache {

	public Map<Integer, PlaceConfig> placeMap = new HashMap<>();

	@Override
	public void load(I i) {
		PlaceConfig config = new PlaceConfig();
		config.setPlaceId(i.i());
		config.setName(i.str());
		config.setItemId(i.i());

		placeMap.put(config.getPlaceId(), config);
	}

	@Override
	public String getFileName() {
		return "place.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.place = this;
	}

}
