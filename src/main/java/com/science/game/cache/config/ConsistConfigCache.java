package com.science.game.cache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.ConsistConfig;

@Component
public class ConsistConfigCache implements IConfigCache {
	public Map<Integer, List<ConsistConfig>> consistMap = new HashMap<>();
	public Map<Integer, Set<Integer>> parentMap = new HashMap<>();

	@Override
	public void load() {
		ConsistConfig config = new ConsistConfig();
		config.setItemId(i(0));
		config.setNeedItemId(i(1));
		config.setCount(i(2));

		List<ConsistConfig> list = consistMap.get(config.getItemId());
		if (list == null) {
			list = new ArrayList<>();
			consistMap.put(config.getItemId(), list);
		}

		list.add(config);

		if (!parentMap.containsKey(config.getNeedItemId())) {
			parentMap.putIfAbsent(config.getNeedItemId(), new HashSet<>());
		}

		Set<Integer> parentItem = parentMap.get(config.getNeedItemId());
		parentItem.add(config.getItemId());
	}

	@Override
	public String getFileName() {
		return "consist.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.consist = this;
	}

}
