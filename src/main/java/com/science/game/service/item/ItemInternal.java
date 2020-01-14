package com.science.game.service.item;

import java.util.List;

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

	/**
	 * 根据唯一id获得道具
	 * 
	 * @param itemId
	 * @return
	 */
	Item getItemByOnlyId(int onlyId);

	/**
	 * 根据道具类型获得道具列表
	 * 
	 * @param itemId
	 * @return
	 */
	List<Item> getItemList(int itemId);

	/**
	 * 移除道具
	 * 
	 * @param onlyId
	 * @return
	 */
	Item removeItemByOnlyId(int onlyId);
}
