package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.ItemConfig;

@Component
public class ItemConfigCache implements IConfigCache {

	public Map<Integer, ItemConfig> itemMap = new HashMap<>();

	@Override
	public void load() {
		ItemConfig config = new ItemConfig();

		config.setItemId(i(0));
		config.setName(str(1));
		config.setType(ItemConfig.ItemType.valueOf(str(2)));
		config.setPractice(i(3));
		config.setDevelopPoint(i(4));
		config.setEffect(i(5));
		config.setAge(i(6));
		config.setUnitTotal(i(7));
		config.setUnitVelocity(i(8));

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
