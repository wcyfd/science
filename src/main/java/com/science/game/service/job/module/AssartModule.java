package com.science.game.service.job.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.JobConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;
import com.science.game.service.tech.TechInternal;
import com.science.game.service.village.VillageInternal;

@Component
public class AssartModule {

	@Autowired
	private JobService jobService;

	@Autowired
	private TechInternal techInternal;

	@Autowired
	private JobInternal jobInternal;

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private JobConfigCache jobConfigCache;

	public void assart(int vid, AbstractService service) {
		if (Data.areaId >= Data.areaList.size())
			return;

		jobService.stop(vid);

		// 荒地的地点是-2
		jobInternal.preStartJob(vid, PlaceType.PLACE, -2, JobType.ASSART);

		Village v = villageInternal.getVillage(vid);

		new AssartJob(v, service).start();

	}

	private JobConfig getAssartJobConfig() {
		return jobConfigCache.jobMap.get(JobType.ASSART.getJobId());
	}

	class AssartJob extends JobTask {

		public AssartJob(Village v, AbstractService service) {
			super(v, service);
		}

		@Override
		public void work(Village village) {
			JobData jobData = village.getJobData();
			if (jobData.getCurrent().get() < jobData.getTotal()) {

				jobInternal.addJobProgress(jobData, getAssartJobConfig().getUnitVelocity()
						+ jobInternal.getEffectByJobType(jobData.getJobType(), village));

				if (jobData.getCurrent().get() >= jobData.getTotal()) {
					Data.areaId++;
					Data.resPlace.putIfAbsent(Data.areaId, Place.create(Data.areaId));// 创建一个资源点位置
				}

				techInternal.think(village.getId());
			} else {
				resetProgress();

				techInternal.think(village.getId());

				if (Data.areaId >= Data.areaList.size())
					jobService.stop(village.getId());
			}
		}

		@Override
		protected void initJobProgress(Village v) {
			int total = getAssartJobConfig().getUnitTotal();
			JobData jobData = v.getJobData();
			jobData.setTotal(total);
			jobData.getCurrent().set(0);
		}

	}
}
