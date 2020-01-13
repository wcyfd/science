package com.science.game.entity.scene;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.science.game.entity.Item;

public class ItemData {
	private Map<Integer, List<Item>> itemMap = new ConcurrentHashMap<>();// 存放装备型道具

	private Map<Integer, Item> itemIdMap = new ConcurrentHashMap<>();// 每个道具都有一个唯一的id,可追踪生命周期内每个道具的全局追踪

	/**
	 * 获得道具类型的对应表
	 * 
	 * @return
	 */
	public Map<Integer, List<Item>> getAllItemsByItemId() {
		return itemMap;
	}

	public List<Item> getItemsByItemId(int itemId) {
		return itemMap.get(itemId);
	}

	/**
	 * 每个道具都有一个唯一的id,可追踪生命周期内每个道具的全局追踪
	 * 
	 * @return
	 */
	public Map<Integer, Item> getUniqueItems() {
		return itemIdMap;
	}

	public Item getItemByOnlyId(int onlyId) {
		return itemIdMap.get(onlyId);
	}

}
