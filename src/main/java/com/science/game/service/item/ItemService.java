package com.science.game.service.item;

import com.science.game.service.ServiceInterface;

public interface ItemService extends ServiceInterface {

	/**
	 * 装备
	 * 
	 * @param vid
	 * @param onlyId 装备唯一id
	 */
	void equip(int vid, int onlyId);

	/**
	 * 卸下装备
	 * 
	 * @param vid
	 * @param itemId
	 */
	void unequip(int vid, int itemId);

	void gc();
}
