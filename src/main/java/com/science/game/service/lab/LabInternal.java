package com.science.game.service.lab;

import java.util.Set;

import com.science.game.entity.village.DevelopData;

public interface LabInternal {
	/**
	 * 是否开发完成
	 * 
	 * @param itemId
	 * @return
	 */
	boolean isDeveloped(int itemId);

	/**
	 * 获取研发成功的道具id
	 * 
	 * @return
	 */
	Set<Integer> getDevelopSuccessItem();

	void addPractice(DevelopData developData, int itemId, int val);
}
