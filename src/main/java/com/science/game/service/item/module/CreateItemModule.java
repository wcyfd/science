package com.science.game.service.item.module;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Place;

@Component
public class CreateItemModule {
	public Item createItemIfAbsent(int itemId) {
		if (!Data.itemMap.containsKey(itemId)) {
			Data.itemMap.putIfAbsent(itemId, Item.create(itemId));
			Data.itemPlace.put(itemId, Place.create(itemId));// 资源型item不需要创建道具位
		}

		return Data.itemMap.get(itemId);
	}
}
