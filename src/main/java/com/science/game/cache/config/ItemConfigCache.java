package com.science.game.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.science.game.entity.config.ItemConfig;

public class ItemConfigCache implements IConfigCache {

	public Map<Integer, ItemConfig> itemConfig = new HashMap<>();

	@Override
	public void load(List<String> values) {
		ItemConfig config = new ItemConfig();
		
		config.setItemId(Integer.valueOf(values.get(0)));
		config.setName(values.get(1));
		config.setType(ItemConfig.ItemType.valueOf(values.get(2)));

		itemConfig.put(config.getItemId(), config);
	}
}
