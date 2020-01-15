package com.science.game.entity.village;

import lombok.Getter;

public class BuildData {
	@Getter
	private int vid;

	private int buildOnlyId;

	private int moduleId;// 正在进行建造的模块id

	public BuildData(int vid) {
		this.vid = vid;
	}

	public int getBuildOnlyId() {
		return buildOnlyId;
	}

	public void setBuildOnlyId(int buildOnlyId) {
		this.buildOnlyId = buildOnlyId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
}
