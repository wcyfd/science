package com.science.game.entity.build;

import java.util.HashMap;
import java.util.Map;

import com.science.game.entity.Item;

import lombok.Getter;

public class ModuleData {
	@Getter
	private Map<Integer, Item> modules = new HashMap<>();
	@Getter
	private int id;

	public ModuleData(int id) {
		this.id = id;
	}

}
