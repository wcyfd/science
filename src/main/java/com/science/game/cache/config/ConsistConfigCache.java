package com.science.game.cache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.science.game.entity.config.ConsistConfig;

public class ConsistConfigCache implements IConfigCache {
	public Map<Integer, List<ConsistConfig>> consistMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		ConsistConfig config = new ConsistConfig();
		config.setItemId(Integer.valueOf(values.get(0)));
		config.setNeedItemId(Integer.valueOf(values.get(1)));
		config.setCount(Integer.valueOf(values.get(2)));

		List<ConsistConfig> list = consistMap.get(config.getItemId());
		if (list == null) {
			list = new ArrayList<>();
			consistMap.put(config.getItemId(), list);
		}

		list.add(config);
	}

}
