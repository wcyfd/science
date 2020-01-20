package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.ParamReader;
import com.science.game.entity.config.BuildConfig;

@Component
public class BuildConfigCache implements IConfigCache {

	public Map<Integer, BuildConfig> buildMap = new HashMap<>();

	@Override
	public void load(ParamReader i) {
		BuildConfig config = new BuildConfig();
		config.setBuildId(i.i());
		config.setName(i.str());

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
