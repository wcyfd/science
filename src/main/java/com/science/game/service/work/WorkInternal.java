package com.science.game.service.work;

import com.science.game.entity.JobType;
import com.science.game.entity.village.WorkData;

public interface WorkInternal {

	/**
	 * 开始工作
	 * 
	 * @param v
	 * @param context
	 */
	void beginWork(WorkData workData, JobType jobType, IWork w);

	/**
	 * 添加工作进度
	 * 
	 * @param v
	 * @param delta
	 */
	void addWorkProgress(WorkData workData, int delta);

	/**
	 * 工作是否完成
	 * 
	 * @param v
	 */
	boolean isWorkComplete(WorkData workData);

	/**
	 * 重置进度
	 * 
	 * @param v
	 */
	void resetProgress(WorkData workData);

	/**
	 * 退出工作
	 * 
	 * @param v
	 */
	void exitWork(WorkData workData);
}
