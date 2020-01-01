package com.science.game.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.PlaceConfig;

@Component
public class PlaceConfigCache implements IConfigCache {

	public Map<Integer, PlaceConfig> placeMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		PlaceConfig config = new PlaceConfig();
		config.setPlaceId(getInt(values, 0));
		config.setName(values.get(1));
		config.setItemId(getInt(values, 2));

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
