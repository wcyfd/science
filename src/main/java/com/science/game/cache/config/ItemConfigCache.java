package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.ParamReader;
import com.science.game.entity.config.ItemConfig;

@Component
public class ItemConfigCache implements IConfigCache {

	public Map<Integer, ItemConfig> itemMap = new HashMap<>();

	@Override
	public void load(ParamReader i) {
		ItemConfig config = new ItemConfig();

		config.setItemId(i.i());
		config.setName(i.str());
		config.setType(ItemConfig.ItemType.valueOf(i.str()));
		config.setPractice(i.i());
		config.setDevelopPoint(i.i());
		config.setEffect(i.i());
		config.setAge(i.i());
		config.setUnitTotal(i.i());
		config.setUnitVelocity(i.i());

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
