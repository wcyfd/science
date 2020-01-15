package com.science.game.entity.village;

import lombok.Getter;

public class BuildData {
	@Getter
	private int vid;

	private int buildOnlyId;

	public BuildData(int vid) {
		this.vid = vid;
	}

	public int getBuildOnlyId() {
		return buildOnlyId;
	}

	public void setBuildOnlyId(int buildOnlyId) {
		this.buildOnlyId = buildOnlyId;
	}
}
