package com.science.game.service.item.module;

import java.util.ArrayList;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Item;
import com.science.game.entity.Scene;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;
import com.science.game.entity.scene.ItemData;

@Component
public class CreateItemModule {

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private DataCenter dataCenter;

	/**
	 * 创建资源型道具
	 * 
	 * @param itemId
	 */
	public void createResItemIfAbsent(int itemId) {
		// 需要判断如果是资源型，则就默认加一个
		ItemConfig config = itemConfigCache.itemMap.get(itemId);
		Scene scene = dataCenter.getScene();
		ItemData itemData = scene.getItemData();

		if (config.getType() == ItemType.RES) {// 资源型道具列表里面始终只有一个道具
			if (!itemData.getAllItemsByItemId().containsKey(itemId)) {
				itemData.getAllItemsByItemId().putIfAbsent(itemId, new ArrayList<>(1));
				Item item = Item.create(itemId);
				itemData.getAllItemsByItemId().get(itemId).add(item);
				itemData.getUniqueItems().put(item.getId(), item);
			}
		}
	}

	/**
	 * 创建装备性道具
	 * 
	 * @param itemId
	 */
	public void createEquipItemIfAbsent(int itemId) {
		ItemConfig config = itemConfigCache.itemMap.get(itemId);
		Scene scene = dataCenter.getScene();
		ItemData itemData = scene.getItemData();

		if (config.getType() == ItemType.ITEM) {
			itemData.getAllItemsByItemId().putIfAbsent(itemId, new LinkedList<>());
		}

	}
}
