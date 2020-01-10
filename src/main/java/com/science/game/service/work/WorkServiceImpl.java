package com.science.game.service.work;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.entity.JobType;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.village.VillageInternal;

@Service
public class WorkServiceImpl extends AbstractService implements WorkInternal, WorkService {

	@Autowired
	private VillageInternal villageInternal;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void beginWork(WorkData workData, JobType jobType, IWork context) {
		WorkTask task = new WorkTask(this) {

			@Override
			public void execute() {
				context.workLoop(workData);
			}
		};

		exitWork(workData);

		workData.setTask(task);
		workData.setJobType(jobType);
		workData.getCurrent().set(0);
		context.enterWork(workData);

		workData.setWork(context);

		task.start();
	}

	@Override
	public void addWorkProgress(WorkData workData, int delta) {
		Reserve reserve = Reserve.builder().store(workData.getCurrent().get()).delta(delta)
				.capacity(workData.getTotal()).fill(true).useAll(true).build();
		if (reserve.transfer()) {
			workData.getCurrent().addAndGet(reserve.getRealDelta());
		}
	}

	@Override
	public boolean isWorkComplete(WorkData workData) {
		return workData.getCurrent().get() >= workData.getTotal();
	}

	@Override
	public void resetProgress(WorkData workData) {
		workData.getCurrent().set(0);
	}

	@Override
	public void exitWork(WorkData workData) {
		workData.getTask().setStop(true);
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
