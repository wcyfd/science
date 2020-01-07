package com.science.game.service.job;

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
}
