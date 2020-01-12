package com.science.game.service.item.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Scene;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;
import com.science.game.entity.scene.ItemData;
import com.science.game.service.lab.LabInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddItemModule {

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private LabInternal labInternal;

	@Autowired
	private Scene scene;

	/**
	 * 增减道具数量<br>
	 * 增加是新建的对象
	 * 
	 * @param itemId
	 * @param count
	 */
	public void addItem(int itemId, int count) {

		ItemConfig proto = itemConfigCache.itemMap.get(itemId);
		List<Item> items = scene.getItemData().getAllItemsByItemId().get(itemId);
		if (proto.getType() == ItemType.ITEM) {// 装备型道具，还要调整道具全局表

			Reserve reserve = Reserve.builder().delta(count).store(items.size()).build();
			if (reserve.transfer()) {
				int realDelta = reserve.getRealDelta();
				if (realDelta < 0) {// 删除道具
					removeItem(items, Math.abs(realDelta));
				} else if (realDelta > 0) {// 添加道具
					addNewItem(items, itemId, count);
				}
			}
		} else if (proto.getType() == ItemType.RES) {
			// 资源型道具
			Item item = items.get(0);
			Reserve reserve = Reserve.builder().delta(count).store(item.getNum()).build();
			if (reserve.transfer()) {
				changeItemByNum(item, reserve.getRemainCount());
			}
		}

	}

	/**
	 * 移除指定数量个道具
	 * 
	 * @param items
	 * @param count
	 */
	private void removeItem(List<Item> items, int count) {
		for (int i = 0; i < count; i++) {
			removeUniqueItem(items.remove(0));
		}
	}

	/**
	 * 创建指定数量个道具
	 * 
	 * @param items
	 * @param itemId
	 * @param count
	 */
	private void addNewItem(List<Item> items, int itemId, int count) {
		for (int i = 0; i < count; i++) {
			Item item = Item.create(itemId);
			item.setAge(item.getProto().getAge());

			items.add(item);
			addUniqueItem(item);
		}
	}

	/**
	 * 直接改变道具的数量，不创建新的对象
	 * 
	 * @param item
	 * @param count
	 */
	private void changeItemByNum(Item item, int count) {
		item.getNumRef().set(count);
	}

	private void removeUniqueItem(Item item) {
		if (item == null)
			return;
		scene.getItemData().getUniqueItems().remove(item.getId());
	}

	private void addUniqueItem(Item item) {
		if (item == null)
			return;
		scene.getItemData().getUniqueItems().put(item.getId(), item);
	}

	/**
	 * 插入道具
	 * 
	 * @param item
	 */
	public void insertItem(Item item) {
		ItemConfig itemConfig = item.getProto();
		ItemData itemData = scene.getItemData();
		if (itemConfig.getType() == ItemType.ITEM) {
			List<Item> list = itemData.getAllItemsByItemId().get(itemConfig.getItemId());
			if (!list.contains(item)) {
				list.add(item);
			}
		} else if (itemConfig.getType() == ItemType.RES) {
			Item res = itemData.getAllItemsByItemId().get(itemConfig.getItemId()).get(0);
			res.getNumRef().addAndGet(item.getNum());
		}
	}
}
