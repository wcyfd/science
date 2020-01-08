package com.science.game.service.job.module;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.JobType;
import com.science.game.entity.Village;

@Component
public class StopModule {

	public long stop(int vid) {
		long remainTime = 0L;
		Village v = Data.villages.get(vid);
		if (v == null) {
			return -1;
		}
		ScheduledFuture<?> future = Data.villageFutures.remove(vid);
		if (future != null)
			if (future.cancel(false)) {
				remainTime = future.getDelay(TimeUnit.MILLISECONDS);
			}

		if (v.getPlaceType() != null) {
			switch (v.getPlaceType()) {
			case ITEM:
				Data.itemPlace.get(v.getPlaceId()).getVillages().remove((Integer) vid);
				break;
			case PLACE:
				Data.resPlace.get(v.getPlaceId()).getVillages().remove((Integer) vid);
				break;
			default:
				break;
			}
		}

		v.setJobId(JobType.NULL.getJobId());
		v.setPlaceId(-1);
		v.setPlaceType(null);

		return remainTime;
	}
}
