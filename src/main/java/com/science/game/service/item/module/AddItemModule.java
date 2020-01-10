package com.science.game.service.item.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;
import com.science.game.service.lab.LabInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddItemModule {

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private LabInternal labInternal;

	/**
	 * 增减道具数量
	 * 
	 * @param itemId
	 * @param count
	 */
	public void addItem(int itemId, int count) {
		List<Item> items = Data.itemMap.get(itemId);

		if (labInternal.isDeveloped(itemId)) {

			ItemConfig proto = itemConfigCache.itemMap.get(itemId);
			if (proto.getType() == ItemType.ITEM) {
				Reserve reserve = Reserve.builder().delta(count).store(items.size()).build();
				if (reserve.transfer()) {
					int realDelta = reserve.getRealDelta();
					if (realDelta < 0) {
						int absRealDelta = Math.abs(realDelta);
						for (int i = 0; i < absRealDelta; i++) {
							Item item = items.remove(0);
							if (item != null) {
								Data.itemIdMap.remove(item.getId());
							}
						}
					} else if (realDelta > 0) {
						for (int i = 0; i < realDelta; i++) {
							Item item = Item.create(itemId);
							item.setAge(item.getProto().getAge());

							items.add(item);

							Data.itemIdMap.put(item.getId(), item);
						}
					}
				}
			} else if (proto.getType() == ItemType.RES) {
				Item item = items.get(0);
				Reserve reserve = Reserve.builder().delta(count).store(item.getNum()).build();
				if (reserve.transfer()) {
					item.getNumRef().set(reserve.getRemainCount());
				}
			}

		} else {
			log.error("该道具还没有研发完成 {}", itemConfigCache.itemMap.get(itemId).getName());
		}

	}

	/**
	 * 插入道具
	 * 
	 * @param item
	 */
	public void insertItem(Item item) {
		ItemConfig itemConfig = item.getProto();
		if (itemConfig.getType() == ItemType.ITEM) {
			List<Item> list = Data.itemMap.get(itemConfig.getItemId());
			if (!list.contains(item)) {
				list.add(item);
			}
		} else if (itemConfig.getType() == ItemType.RES) {
			Item res = Data.itemMap.get(itemConfig.getItemId()).get(0);
			res.getNumRef().addAndGet(item.getNum());
		}
	}
}
