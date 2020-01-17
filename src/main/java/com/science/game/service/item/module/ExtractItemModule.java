package com.science.game.service.item.module;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Item;
import com.science.game.entity.config.ItemConfig.ItemType;
import com.science.game.entity.scene.ItemData;

@Component
public class ExtractItemModule {

	@Autowired
	private DataCenter dataCenter;

	/**
	 * 抽取道具
	 * 
	 * @param itemId
	 * @param count
	 * @return
	 */
	public List<Item> extractItemByItemId(int itemId, int count) {
		List<Item> list = new ArrayList<>(count);
		ItemData itemData = dataCenter.getScene().getItemData();
		List<Item> center = itemData.getAllItemsByItemId().get(itemId);
		if (center != null && center.size() != 0) {
			if (center.get(0).getProto().getType() == ItemType.ITEM) {
				Reserve r = Reserve.builder().delta(-count).store(center.size()).useAll(false).build();
				if (r.transfer()) {
					for (int i = 0; i < count; i++) {
						list.add(center.remove(0));
					}
				}
			} else if (center.get(0).getProto().getType() == ItemType.RES) {
				Reserve r = Reserve.builder().delta(-count).store(center.get(0).getNum()).useAll(false).build();
				if (r.transfer()) {
					for (int i = 0; i < count; i++) {
						// 资源型道具在分离之后不用注册到全局道具表中
						Item item = Item.create(itemId);
						item.getNumRef().set(1);
						list.add(item);
					}
				}
			}
		}

		return list;
	}
}
