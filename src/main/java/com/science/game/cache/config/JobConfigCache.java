package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.JobConfig;

@Component
public class JobConfigCache implements IConfigCache {

	public Map<Integer, JobConfig> jobMap = new HashMap<>();

	@Override
	public void load() {
		JobConfig config = new JobConfig();

		config.setId(i(0));
		config.setJob(str(1));
		config.setUnitTotal(i(2));
		config.setUnitVelocity(i(3));

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
