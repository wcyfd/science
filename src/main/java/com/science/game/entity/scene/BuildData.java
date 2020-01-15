package com.science.game.entity.scene;

import java.util.HashMap;
import java.util.Map;

import com.science.game.entity.Build;

import lombok.Getter;

public class BuildData {
	@Getter
	private Map<Integer, Build> onlyIdBuildMap = new HashMap<>();

	@Getter
	private Map<Integer, Map<Integer, Build>> typeBuildMap = new HashMap<>();

	public BuildData() {
	}
}
