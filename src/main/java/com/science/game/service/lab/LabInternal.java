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

	/**
	 * 增加熟练度
	 * 
	 * @param developData
	 * @param itemId
	 * @param val
	 */
	void addPractice(DevelopData developData, int itemId, int val);

	/**
	 * 添加新的想法
	 * 
	 * @param id
	 */
	void addNewIdea(int id);

	/**
	 * 是否已经有这个想法
	 * 
	 * @param id
	 */
	boolean isOldIdea(int id);

	/**
	 * 获取正在思考的想法列表
	 * 
	 * @return
	 */
	List<Integer> getIdeaList();

	/**
	 * 获取尝试次数
	 * 
	 * @param itemId
	 * @return
	 */
	int getTryCount(int itemId);
}
