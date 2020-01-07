package com.science.game.service.item;

import com.science.game.entity.Item;

public interface ItemInternal {
	/**
	 * 创建一个道具
	 * 
	 * @param itemId
	 * @return
	 */
	Item createItemIfAbsent(int itemId);

	/**
	 * 改变道具数量
	 * 
	 * @param item
	 * @param count
	 */
	void addItem(int itemId, int count);

	/**
	 * 创建道具放置位
	 * 
	 * @param itemId
	 */
	void createItemPlace(int itemId);

	/**
	 * 道具已经被研发成功
	 * 
	 * @param itemId
	 * @return
	 */
	boolean itemIsDeveloped(int itemId);
}
