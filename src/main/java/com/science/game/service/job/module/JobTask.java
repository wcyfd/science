package com.science.game.service.job.module;

import java.util.concurrent.ScheduledFuture;

import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.Village;
import com.science.game.service.AbstractService;

import game.quick.window.Task;

public abstract class JobTask implements Task {

	protected Village village;
	protected AbstractService service;
	protected volatile ScheduledFuture<?> future;

	public JobTask(Village v, AbstractService service) {
		this.village = v;
		this.service = service;
		v.getJobData().setJobTask(this);
	}

	@Override
	public void execute() {
		JobData jobData = village.getJobData();
		if (jobData.getJobType() == JobType.NULL)
			return;

		work(village);
	}

	public abstract void work(Village village);

	@Override
	public void afterExecute() {

		JobData jobData = village.getJobData();
		if (jobData.getJobType() == JobType.NULL)
			return;

		this.future = service.delay(this);
	}

	protected void resetProgress() {
		initJobProgress(village);
	}

	protected abstract void initJobProgress(Village v);

	public void stop() {
		if (future != null) {
			future.cancel(false);
			future = null;
		}
	}

	public void start() {
		resetProgress();
		this.future = service.delay(this);
	}
}
