package com.science.game.service.item.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.entity.Item;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.service.item.ItemInternal;

@Component
public class EquipModule {

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private Scene scene;

	/**
	 * 装备
	 * 
	 * @param vid
	 * @param onlyId
	 */
	public void equip(int vid, int onlyId) {
		Village v = scene.getVillageData().getByOnlyId(vid);
		Item item = scene.getItemData().getItemByOnlyId(onlyId);
		int itemId = item.getProto().getItemId();
		List<Item> equips = scene.getItemData().getItemsByItemId(itemId);
		if (equips.remove(item)) {
			unequip(vid, itemId);

			v.getItemData().getEquips().put(itemId, item);
		}

	}

	/**
	 * 卸下装备
	 * 
	 * @param vid
	 * @param itemId
	 */
	public void unequip(int vid, int itemId) {
		Village v = scene.getVillageData().getByOnlyId(vid);
		Item item = v.getItemData().getEquips().remove(itemId);
		if (item != null) {
			itemInternal.insertItem(item);
		}

	}
}
