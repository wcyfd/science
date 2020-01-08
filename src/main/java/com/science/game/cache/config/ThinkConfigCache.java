package com.science.game.cache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ThinkConfig;

@Component
public class ThinkConfigCache implements IConfigCache {

	@Autowired
	private ItemConfigCache itemCache;

	public Map<Integer, ThinkConfig> thinkMap = new HashMap<>();
	public Map<Integer, List<ThinkConfig>> jobThinkMap = new HashMap<>();

	@Override
	public void load(List<String> values) {
		ThinkConfig config = new ThinkConfig();
		config.setId(getInt(values, 0));
		config.setJobId(getInt(values, 1));
		config.setItemId(getInt(values, 2));

		thinkMap.put(config.getId(), config);
		List<ThinkConfig> thinkItemList = jobThinkMap.get(config.getJobId());
		if (thinkItemList == null) {
			thinkItemList = new ArrayList<>();
			jobThinkMap.put(config.getJobId(), thinkItemList);
		}
		thinkItemList.add(config);

	}

	@Override
	public String getFileName() {
		return "think.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.think = this;

		for (ThinkConfig config : thinkMap.values()) {
			ItemConfig itemConfig = itemCache.itemMap.get(config.getItemId());
			itemConfig.getJobs().add(config.getJobId());
		}
	}

}
