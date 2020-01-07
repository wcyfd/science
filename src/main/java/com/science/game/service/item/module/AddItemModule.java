package com.science.game.service.item.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.Item;
import com.science.game.service.item.ItemInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddItemModule {

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private ItemConfigCache itemConfigCache;

	/**
	 * 增减道具数量
	 * 
	 * @param itemId
	 * @param count
	 */
	public void addItem(int itemId, int count) {
		List<Item> items = Data.itemMap.get(itemId);

		if (itemInternal.itemIsDeveloped(itemId)) {
			Reserve reserve = Reserve.builder().delta(count).store(items.size()).build();
			if (reserve.transfer()) {
				int realDelta = reserve.getRealDelta();
				if (realDelta < 0) {
					int absRealDelta = Math.abs(realDelta);
					for (int i = 0; i < absRealDelta; i++) {
						items.remove(0);
					}
				} else if (realDelta > 0) {
					for (int i = 0; i < realDelta; i++) {
						Item item = Item.create(itemId);
						item.setAge(item.getProto().getAge());

						items.add(item);
					}
				}
			}
		} else {
			log.error("该道具还没有研发完成 {}", itemConfigCache.itemMap.get(itemId).getName());
		}

	}
}
