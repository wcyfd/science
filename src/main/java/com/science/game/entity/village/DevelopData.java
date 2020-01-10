package com.science.game.entity.village;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

public class DevelopData {
	@Getter
	private int vid;
	@Getter
	@Setter
	private int itemId;

	// 每种道具的熟练度

	private Map<Integer, AtomicInteger> practiceMap = new HashMap<>();

	public Map<Integer, AtomicInteger> getPracticeMap() {
		return practiceMap;
	}

	public DevelopData(int vid) {
		this.vid = vid;
	}
}
