package com.science.game.service.job.module;

import java.util.concurrent.ScheduledFuture;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Village;

@Component
public class StopModule {

	public void stop(int vid) {
		Village v = Data.villages.get(vid);
		if (v == null) {
			return;
		}
		ScheduledFuture<?> future = Data.villageFutures.remove(vid);
		if (future != null)
			future.cancel(false);

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

		v.setJobId(-1);
		v.setPlaceId(-1);
		v.setPlaceType(null);

	}
}
