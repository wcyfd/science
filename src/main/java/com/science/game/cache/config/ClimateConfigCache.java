package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.ParamReader;
import com.science.game.entity.config.ClimateConfig;

@Component
public class ClimateConfigCache implements IConfigCache {

	public Map<Integer, ClimateConfig> map = new HashMap<>();

	@Override
	public void load(ParamReader i) {
		ClimateConfig c = new ClimateConfig();
		c.setId(i.i());
		c.setName(i.str());
		c.setVal(i.i());

		map.put(c.getId(), c);
	}

	@Override
	public String getFileName() {
		return "climate.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.climate = this;
	}

}
