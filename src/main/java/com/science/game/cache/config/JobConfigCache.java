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
		config.setId(Integer.valueOf(values.get(0)));
		config.setJob(values.get(1));

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
