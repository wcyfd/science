package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.I;
import com.science.game.entity.config.JobConfig;

@Component
public class JobConfigCache implements IConfigCache {

	public Map<Integer, JobConfig> jobMap = new HashMap<>();

	@Override
	public void load(I i) {
		JobConfig config = new JobConfig();

		config.setId(i.i());
		config.setJob(i.str());
		config.setUnitTotal(i.i());
		config.setUnitVelocity(i.i());

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
