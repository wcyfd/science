package com.science.game.entity.config;

import lombok.Data;

@Data
public class ModuleConfig {
	private int onlyId;
	private int buildId;
	private int moduleId;
	private String moduleName;
	private int needItemId;
	private int needCount;
}
