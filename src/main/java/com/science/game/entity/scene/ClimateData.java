package com.science.game.entity.scene;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

/**
 * 气候数据
 * 
 * @author aimfd
 *
 */
public class ClimateData {
	@Getter
	private Map<Integer, AtomicInteger> params = new HashMap<>();

	public AtomicInteger getRefById(int id) {
		return params.get(id);
	}

	public int getValById(int id) {
		return params.get(id).get();
	}
}
