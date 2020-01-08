package com.science.game.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.JobConfig;

@Component
public class JobConfigCache implements IConfigCache {

	public Map<Integer, JobConfig> jobMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		JobConfig config = new JobConfig();

		config.setId(getInt(values, 0));
		config.setJob(values.get(1));
		config.setUnitTotal(getLong(values, 2));
		config.setUnitVelocity(getInt(values, 3));

		jobMap.put(config.getId(), config);
	}

	@Override
	public String getFileName() {
		return "job.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.job = this;
	}
}
