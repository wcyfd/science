package com.science.game.service.item;

import java.util.Set;

import com.science.game.entity.Item;
import com.science.game.entity.JobType;

public interface ItemInternal {
	/**
	 * 创建一个道具
	 * 
	 * @param itemId
	 * @return
	 */
	void createItemIfAbsent(int itemId);

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

	/**
	 * 获取研发成功的道具id
	 * 
	 * @return
	 */
	Set<Integer> getDevelopSuccessItem();

}
