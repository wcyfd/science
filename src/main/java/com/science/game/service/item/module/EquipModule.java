package com.science.game.service.item.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Village;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobInternal;

@Component
public class EquipModule {

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private JobInternal jobInternal;

	/**
	 * 装备
	 * 
	 * @param vid
	 * @param onlyId
	 */
	public void equip(int vid, int onlyId) {
		Village v = Data.villages.get(vid);
		Item item = Data.itemIdMap.get(onlyId);
		int itemId = item.getProto().getItemId();
		List<Item> equips = Data.itemMap.get(itemId);
		if (equips.remove(item)) {
			unequip(vid, itemId);

			v.getEquips().put(itemId, item);
		}
		
		jobInternal.changeJobRate(vid);
	}

	/**
	 * 卸下装备
	 * 
	 * @param vid
	 * @param itemId
	 */
	public void unequip(int vid, int itemId) {
		Village v = Data.villages.get(vid);
		Item item = v.getEquips().remove(itemId);
		if (item != null) {
			itemInternal.insertItem(item);
		}

		jobInternal.changeJobRate(vid);
	}
}
