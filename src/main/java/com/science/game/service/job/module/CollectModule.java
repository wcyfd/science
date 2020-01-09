package com.science.game.service.job.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.PlaceConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;
import com.science.game.service.tech.TechInternal;
import com.science.game.service.village.VillageInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CollectModule {

	@Autowired
	private JobService jobService;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private TechInternal techInternal;

	@Autowired
	private JobInternal jobInternal;

	@Autowired
	private PlaceConfigCache placeConfigCache;

	@Autowired
	private JobConfigCache jobConfigCache;

	@Autowired
	private VillageInternal villageInternal;

	/**
	 * 采集
	 * 
	 * @param vid
	 * @param areaId
	 * @param service
	 */
	public void collect(int vid, int areaId, AbstractService service) {
		Place place = Data.resPlace.get(areaId);

		if (place == null) {
			log.error("没有这个地点{},保持原样不变", areaId);
			return;
		}

		int resId = Data.areaList.get(areaId);
		JobType jobType = null;
		switch (resId) {
		case 3:
			jobType = JobType.DIG;
			break;
		case 4:
			jobType = JobType.CHOP;
			break;
		}

		if (jobType == null) {
			log.error("没有这个工作{}", resId);
			return;
		}

		// 先停止工作
		jobService.stop(vid);

		Village v = villageInternal.getVillage(vid);

		jobInternal.preStartJob(vid, PlaceType.PLACE, areaId, jobType);

		new CollectJob(resId, v, service).start();

	}

	class CollectJob extends JobTask {
		private int resId;

		public CollectJob(int resId, Village v, AbstractService service) {
			super(v, service);
			this.resId = resId;
		}

		@Override
		public void work(Village village) {
			JobData jobData = village.getJobData();
			if (jobData.getCurrent().get() < jobData.getTotal()) {
				int velocity = jobConfigCache.jobMap.get(jobData.getJobType().getJobId()).getUnitVelocity();
				jobInternal.addJobProgress(jobData, velocity);

				PlaceConfig placeConfig = placeConfigCache.placeMap.get(resId);

				if (jobData.getCurrent().get() >= jobData.getTotal()) {
					itemInternal.createItemIfAbsent(placeConfig.getItemId());
					itemInternal.addItem(placeConfig.getItemId(), 1);
				}

			} else {
				resetProgress();
			}

			techInternal.think(village.getId());
		}

		@Override
		protected void initJobProgress(Village v) {
			JobData jobData = v.getJobData();
			jobData.setTotal(jobConfigCache.jobMap.get(jobData.getJobType().getJobId()).getUnitTotal());
			jobData.getCurrent().set(0);
		}

	}
}
