package com.science.game.service.work;

import com.science.game.entity.JobType;
import com.science.game.entity.ProgressData;
import com.science.game.entity.village.WorkData;

/**
 * 
 * @author aimfd
 *
 */
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
	void addWorkProgress(ProgressData progressData, int delta);

	/**
	 * 工作是否完成
	 * 
	 * @param v
	 */
	boolean isWorkComplete(ProgressData progressData);

	/**
	 * 重置进度
	 * 
	 * @param v
	 */
	void resetProgress(ProgressData progressData);

	/**
	 * 设置进度
	 * 
	 * @param workData
	 * @param val
	 */
	void setProgress(ProgressData progressData, int val);

	/**
	 * 退出工作
	 * 
	 * @param v
	 */
	void exitWork(WorkData workData);
}
