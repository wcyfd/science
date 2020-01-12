package com.science.game.entity.scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.science.game.entity.Village;

@Component
public class VillageData {
	private Map<Integer, Village> villages = new ConcurrentHashMap<>();

	public Map<Integer, Village> getVillages() {
		return villages;
	}

	public Village getByOnlyId(int vid) {
		return villages.get(vid);
	}
}
