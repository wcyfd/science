package com.science.game.service.item;

import com.science.game.entity.Item;

public interface ItemInternal {

	/**
	 * 
	 * 改变道具数量
	 * 
	 * @param item
	 * @param count
	 */
	void addItem(int itemId, int count);

	/**
	 * 插入道具
	 * 
	 * @param item
	 */
	void insertItem(Item item);

	/**
	 * 获取道具数量
	 * 
	 * @param itemId
	 * @return
	 */
	int getItemCount(int itemId);

	void createResItemSpace(int itemId);

	void createEquipItemSpace(int itemId);

	/**
	 * 是否有道具记录
	 * 
	 * @param itemId
	 */
	boolean hasItemRecord(int itemId);

}
