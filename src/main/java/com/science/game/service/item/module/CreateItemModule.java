package com.science.game.service.item.module;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;

@Component
public class CreateItemModule {

	@Autowired
	private ItemConfigCache itemConfigCache;

	public void createItemIfAbsent(int itemId) {

		// 需要判断如果是资源型，则就默认加一个
		ItemConfig config = itemConfigCache.itemMap.get(itemId);
		if (config.getType() == ItemType.RES) {
			Data.itemMap.putIfAbsent(itemId, new ArrayList<>(1));
			List<Item> list = Data.itemMap.get(itemId);
			if (list.size() == 0) {
				list.add(Item.create(itemId));
			}
		} else if (config.getType() == ItemType.ITEM) {
			Data.itemMap.putIfAbsent(itemId, new LinkedList<>());
		}

		Data.itemPlace.putIfAbsent(itemId, Place.create(itemId));// 资源型item不需要创建道具位
	}

	public void createItemPlace(int itemId) {
		Data.itemPlace.putIfAbsent(itemId, Place.create(itemId));// 资源型item不需要创建道具位
	}
}
