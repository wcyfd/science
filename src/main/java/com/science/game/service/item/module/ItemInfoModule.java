package com.science.game.service.item.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Scene;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemInfoModule {

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private Scene scene;

	public int getItemCount(int itemId) {
		ItemConfig config = itemConfigCache.itemMap.get(itemId);
		ItemType type = config.getType();
		List<Item> list = scene.getItemData().getItemsByItemId(itemId);
		if (list == null || list.size() == 0) {
			return 0;
		}
		if (type == ItemType.RES) {
			return list.get(0).getNum();
		} else if (type == ItemType.ITEM) {
			return list.size();
		}
		log.error("itemType is error: itemId=" + itemId);
		return 0;
	}

}
