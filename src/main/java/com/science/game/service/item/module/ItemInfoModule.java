package com.science.game.service.item.module;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;

@Component
public class ItemInfoModule {

	@Autowired
	private ItemConfigCache itemConfigCache;

	public int getItemCount(int itemId) {
		ItemConfig config = itemConfigCache.itemMap.get(itemId);
		ItemType type = config.getType();
		if (type == ItemType.RES) {
			return Data.itemMap.get(itemId).get(0).getNum();
		} else if (type == ItemType.ITEM) {
			return Data.itemMap.get(itemId).size();
		}
		throw new RuntimeException("itemType is error: itemId=" + itemId);
	}

	public Set<Integer> getDevelopItemId() {
		return new HashSet<>(Data.scienceMap);
	}
}
