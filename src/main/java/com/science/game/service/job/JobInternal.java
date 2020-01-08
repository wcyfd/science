package com.science.game.service.job;

import com.science.game.entity.JobTimeData;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;

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
	 * 获取工作时间
	 * 
	 * @param jobType
	 * @param vid
	 * @param itemId
	 * @return
	 */
	JobTimeData getJobTime(JobType jobType, int vid, int itemId);

	/**
	 * 停止工作
	 * 
	 * @param vid
	 * @return
	 */
	long stopAndReturnRemainTime(int vid);

	/**
	 * 改变工作效率
	 * 
	 * @param vid
	 */
	void changeJobRate(int vid);
}
