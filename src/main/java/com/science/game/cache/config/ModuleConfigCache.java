package com.science.game.cache.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.ParamReader;
import com.science.game.entity.config.ModuleConfig;

@Component
public class ModuleConfigCache implements IConfigCache {

	public Map<Integer, ModuleConfig> moduleMap = new HashMap<>();

	// <buildId,<moduleId,int_total_progress>>
	public Map<Integer, Map<Integer, Integer>> buildModuleTotalProgressMap = new HashMap<>();
	// <build,<moduleId,<itemId,config>>>
	public Map<Integer, Map<Integer, Map<Integer, ModuleConfig>>> moduleItemMap = new HashMap<>();

	@Override
	public void load(ParamReader i) {
		ModuleConfig config = new ModuleConfig();
		config.setOnlyId(i.i());
		config.setBuildId(i.i());
		config.setModuleId(i.i());
		config.setModuleName(i.str());
		config.setNeedItemId(i.i());
		config.setTotal(i.i());

		moduleMap.put(config.getOnlyId(), config);

		if (!buildModuleTotalProgressMap.containsKey(config.getBuildId())) {
			buildModuleTotalProgressMap.putIfAbsent(config.getBuildId(), new HashMap<>());
		}

		buildModuleTotalProgressMap.get(config.getBuildId()).put(config.getModuleId(), config.getTotal());

		if (!moduleItemMap.containsKey(config.getBuildId())) {
			moduleItemMap.putIfAbsent(config.getBuildId(), new HashMap<>());
		}

		if (!moduleItemMap.get(config.getBuildId()).containsKey(config.getModuleId())) {
			moduleItemMap.get(config.getBuildId()).putIfAbsent(config.getModuleId(), new HashMap<>());
		}

		moduleItemMap.get(config.getBuildId()).get(config.getModuleId()).put(config.getNeedItemId(), config);

	}

	@Override
	public String getFileName() {
		return "build_module.csv";
	}

	@Override
	public void afterLoad() {
		ConfigCache.module = this;
	}

}
