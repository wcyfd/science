package com.science.game.entity.build;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class ModuleData {
	@Getter
	private int id;
	@Getter
	private Map<Integer, ModuleWorkData> workDataMap = new HashMap<>();

	public ModuleData(int id) {
		this.id = id;
	}

	public ModuleWorkData getWorkDataByModuleId(int id) {
		return workDataMap.get(id);
	}

}
