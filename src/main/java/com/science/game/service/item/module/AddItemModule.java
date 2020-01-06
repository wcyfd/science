package com.science.game.service.item.module;

import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.entity.Item;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddItemModule {
	public void addItem(int itemId, int count) {
		Item item = Data.itemMap.get(itemId);
		if (item != null) {
			Reserve reserve = Reserve.builder().delta(count).store(item.getNum()).build();
			if (reserve.transfer()) {
				log.debug("扣除{} {}", itemId, count);
				item.setNum(reserve.getRemainCount());
			}
		}

	}
}
