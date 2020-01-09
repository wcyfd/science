package com.science.game.service.job.module;

import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.entity.JobData;

@Component
public class JobProgressModule {

	public void addJobProgress(JobData jobData, int value) {
		Reserve reserve = Reserve.builder().store(jobData.getCurrent().get()).delta(value).capacity(jobData.getTotal())
				.fill(true).build();
		if (reserve.transfer()) {
			jobData.getCurrent().addAndGet(reserve.getRealDelta());
		}
	}

}
