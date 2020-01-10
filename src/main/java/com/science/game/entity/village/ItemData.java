package com.science.game.entity.village;

import java.util.HashMap;
import java.util.Map;

import com.science.game.entity.Item;

public class ItemData {
	private int vid;
//装备列表
	private Map<Integer, Item> equips = new HashMap<>();

	public ItemData(int vid) {
		this.vid = vid;
	}

	public int getVid() {
		return vid;
	}

	public Map<Integer, Item> getEquips() {
		return equips;
	}
}
