package com.science.game.service.work;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.ParamReader;
import com.science.game.entity.JobType;
import com.science.game.entity.ProgressData;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.village.VillageInternal;

@Service
public class WorkServiceImpl extends AbstractService implements WorkInternal, WorkService {

	@Autowired
	private VillageInternal villageInternal;

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "stop":
			stop(i.i());
			break;
		}
	}

	@Override
	public void beginWork(WorkData workData, JobType jobType, IWork context) {
		WorkTask task = new WorkTask(this) {

			@Override
			public void doJob() {
				context.workLoop(workData);
			}
		};

		workData.setTask(task);
		workData.setJobType(jobType);
		workData.getCurrent().set(0);
		context.enterWork(workData);

		workData.setWork(context);

		task.start();
	}

	@Override
	public void addWorkProgress(ProgressData progressData, int delta) {
		Reserve reserve = Reserve.builder().store(progressData.getCurrent().get()).delta(delta)
				.capacity(progressData.getTotal()).fill(true).useAll(true).build();
		if (reserve.transfer()) {
			progressData.getCurrent().addAndGet(reserve.getRealDelta());
		}
	}

	@Override
	public boolean isWorkComplete(ProgressData progressData) {
		return progressData.getCurrent().get() >= progressData.getTotal();
	}

	@Override
	public void resetProgress(ProgressData progressData) {
		progressData.getCurrent().set(0);
	}

	@Override
	public void setProgress(ProgressData progressData, int val) {
		progressData.getCurrent().set(val);
	}

	@Override
	public void exitWork(WorkData workData) {
		WorkTask workTask = workData.getTask();
		if (workTask != null) {
			workTask.setStop(true);
		}
		workData.getCurrent().set(0);
		workData.setJobType(JobType.NULL);
		workData.setTotal(0);

		IWork work = workData.getWork();
		if (work != null) {
			work.exitWork(workData);
			workData.setWork(null);
		}
	}

	@Override
	public void stop(int vid) {

		WorkData workData = villageInternal.getVillage(vid).getWorkData();

		exitWork(workData);
	}

}
