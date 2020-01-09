package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.service.job.module.JobTask;

/**
 * 工作数据
 * 
 * @author aimfd
 *
 */

public class JobData {
	/** 总进度 */
	private int total;
	/** 当前进度 */
	private AtomicInteger current = new AtomicInteger();
	/** 工作地类型 */
	private PlaceType placeType;
	/** 工作地点 */
	private int placeId;
	/** 工作类型 */
	private JobType jobType;

	private volatile JobTask jobTask;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public AtomicInteger getCurrent() {
		return current;
	}

	public PlaceType getPlaceType() {
		return placeType;
	}

	public void setPlaceType(PlaceType placeType) {
		this.placeType = placeType;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public JobTask getJobTask() {
		return jobTask;
	}

	public void setJobTask(JobTask jobTask) {
		this.jobTask = jobTask;
	}

}
