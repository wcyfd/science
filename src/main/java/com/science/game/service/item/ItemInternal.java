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
}
