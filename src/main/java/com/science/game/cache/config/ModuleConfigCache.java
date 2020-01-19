package com.science.game.cache.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.I;
import com.science.game.entity.config.ModuleConfig;

@Component
public class ModuleConfigCache implements IConfigCache {

	public Map<Integer, ModuleConfig> moduleMap = new HashMap<>();
	public Map<Integer, List<ModuleConfig>> buildModuleMap = new HashMap<>();
	public Map<Integer, Map<Integer, ModuleConfig>> buildModuleIdMap = new HashMap<>();

	@Override
	public void load(I i) {
		ModuleConfig config = new ModuleConfig();
		config.setOnlyId(i.i());
		config.setBuildId(i.i());
		config.setModuleId(i.i());
		config.setModuleName(i.str());
		config.setNeedItemId(i.i());
		config.setTotal(i.i());

		moduleMap.put(config.getOnlyId(), config);

		if (!buildModuleMap.containsKey(config.getBuildId()))
			buildModuleMap.putIfAbsent(config.getBuildId(), new ArrayList<>());
		buildModuleMap.get(config.getBuildId()).add(config);

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
