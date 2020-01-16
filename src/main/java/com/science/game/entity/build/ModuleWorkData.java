package com.science.game.entity.build;

import java.util.HashMap;
import java.util.Map;

import com.science.game.entity.Item;
import com.science.game.entity.village.WorkData;

/**
 * 用来记录道具和工作量的数据
 * 
 * @author heyue
 *
 */
public class ModuleWorkData extends WorkData {

	private Map<Integer, Item> itemMap = new HashMap<>();

	public ModuleWorkData(int vid) {
		super(vid);
	}

	public Map<Integer, Item> getItemMap() {
		return itemMap;
	}

}
