package com.science.game.entity.village;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.entity.JobType;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkTask;

import lombok.Getter;
import lombok.Setter;

public class WorkData {
	@Getter
	private int vid;
	@Getter
	@Setter
	private WorkTask task;

	@Getter
	private AtomicInteger current = new AtomicInteger();
	@Getter
	@Setter
	private int total;

	@Getter
	private JobType jobType;

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	@Getter
	@Setter
	private IWork work;

	public WorkData(int vid) {
		this.vid = vid;
		this.jobType = JobType.NULL;
	}

}
