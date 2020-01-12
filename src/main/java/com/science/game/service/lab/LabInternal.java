package com.science.game.service.lab;

import java.util.List;
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

	/**
	 * 添加新的想法
	 * 
	 * @param id
	 */
	void addNewThink(int id);

	/**
	 * 是否已经有这个想法
	 * 
	 * @param id
	 */
	boolean isOldThinking(int id);

	/**
	 * 获取正在思考的想法列表
	 * 
	 * @return
	 */
	List<Integer> getThinkingList();
}
