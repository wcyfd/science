package com.science.game.service.job;

import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;

public interface JobInternal {
	/**
	 * 
	 * @param vid
	 * @param type
	 * @param id
	 * @param jobType
	 */
	void preStartJob(int vid, PlaceType type, int id, JobType jobType);

	/**
	 * 停止工作
	 * 
	 * @param vid
	 * @return
	 */
	long stopAndReturnRemainTime(int vid);

	/**
	 * 添加工作进度
	 * 
	 * @param jobData
	 * @param val
	 */
	void addJobProgress(JobData jobData, int val);

	/**
	 * 获得增益
	 * 
	 * @param jobType
	 * @param v
	 * @return
	 */
	int getEffectByJobType(JobType jobType, Village v);
}
