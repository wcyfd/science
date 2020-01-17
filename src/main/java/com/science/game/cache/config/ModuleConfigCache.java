package com.science.game.cache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.entity.config.ModuleConfig;

@Component
public class ModuleConfigCache implements IConfigCache {

	public Map<Integer, ModuleConfig> moduleMap = new HashMap<>();
	public Map<Integer, List<ModuleConfig>> buildModuleMap = new HashMap<>();
	public Map<Integer, Map<Integer, ModuleConfig>> buildModuleIdMap = new HashMap<>();

	@Override
	public void load() {
		ModuleConfig config = new ModuleConfig();
		config.setOnlyId(i(0));
		config.setBuildId(i(1));
		config.setModuleId(i(2));
		config.setModuleName(str(3));
		config.setNeedItemId(i(4));
		config.setTotal(i(5));

		moduleMap.put(config.getOnlyId(), config);

		if (!buildModuleMap.containsKey(config.getBuildId()))
			buildModuleMap.putIfAbsent(config.getBuildId(), new ArrayList<>());
		buildModuleMap.get(config.getOnlyId()).add(config);

		if (!buildModuleIdMap.containsKey(config.getBuildId()))
			buildModuleIdMap.putIfAbsent(config.getBuildId(), new HashMap<>());
		buildModuleIdMap.get(config.getBuildId()).put(config.getModuleId(), config);
	}

	@Override
	public String getFileName() {
		return "module.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.module = this;
	}

}
