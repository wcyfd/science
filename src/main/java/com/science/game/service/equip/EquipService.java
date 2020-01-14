package com.science.game.service.equip;

import com.science.game.service.ServiceInterface;

/**
 * z装备系统
 * 
 * @author heyue
 *
 */
public interface EquipService extends ServiceInterface {
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
}
