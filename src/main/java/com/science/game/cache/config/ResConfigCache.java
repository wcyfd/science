package com.science.game.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.science.game.entity.config.ResConfig;

public class ResConfigCache implements IConfigCache {

	public Map<Integer, ResConfig> resMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		ResConfig config = new ResConfig();
		config.setResId(Integer.valueOf(values.get(0)));
		config.setName(values.get(1));
		config.setItemId(Integer.valueOf(values.get(2)));

		resMap.put(config.getResId(), config);
	}

}
