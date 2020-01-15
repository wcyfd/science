package com.science.game.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.BuildConfig;

@Component
public class BuildConfigCache implements IConfigCache {

	public Map<Integer, BuildConfig> buildMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		BuildConfig config = new BuildConfig();
		config.setBuildId(getInt(values, 0));
		config.setName(values.get(1));

		buildMap.put(config.getBuildId(), config);
	}

	@Override
	public String getFileName() {
		return "build.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.build = this;

	}

}
