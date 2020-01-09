package com.science.game.service.job.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.Village;
import com.science.game.service.village.VillageInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StopModule {

	@Autowired
	private VillageInternal villageInternal;

	public long stop(int vid) {
		long remainTime = 0L;
		Village v = villageInternal.getVillage(vid);
		if (v == null) {
			return -1;
		}

		JobData jobData = v.getJobData();
		removeFromPlace(v.getJobData(), vid);

		jobData.setJobType(JobType.NULL);
		jobData.setPlaceId(-1);
		jobData.setPlaceType(null);
		jobData.getCurrent().set(0);
		jobData.setTotal(0);
		JobTask task = jobData.getJobTask();
		if (task != null) {
			task.stop();
		}
		jobData.setJobTask(null);

		return remainTime;
	}

	private void removeFromPlace(JobData jobData, int vid) {
		log.info("从工作地点移除 vid={},placeType={},placeId={}", vid, jobData.getPlaceType(), jobData.getPlaceId());
		if (jobData.getPlaceType() != null) {
			switch (jobData.getPlaceType()) {
			case ITEM:
				Data.itemPlace.get(jobData.getPlaceId()).getVillages().remove((Integer) vid);
				break;
			case PLACE:
				Data.resPlace.get(jobData.getPlaceId()).getVillages().remove((Integer) vid);
				break;
			default:
				break;
			}
		}

	}
}
