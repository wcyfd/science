package com.science.game.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.ItemConfig;

@Component
public class ItemConfigCache implements IConfigCache {

	public Map<Integer, ItemConfig> itemMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		ItemConfig config = new ItemConfig();

		config.setItemId(getInt(values, 0));
		config.setName(values.get(1));
		config.setType(ItemConfig.ItemType.valueOf(values.get(2)));

		itemMap.put(config.getItemId(), config);
	}

	@Override
	public String getFileName() {
		return "item.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.item = this;
	}

}
