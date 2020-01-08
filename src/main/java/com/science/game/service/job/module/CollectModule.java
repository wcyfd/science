package com.science.game.service.job.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.JobTimeData;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.config.PlaceConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;
import com.science.game.service.tech.TechInternal;

import game.quick.window.Task;
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
		int itemId = placeConfigCache.placeMap.get(resId).getItemId();
		JobType jobType = null;
		switch (resId) {
		case 3:
			jobType = JobType.DIG;
			break;
		case 4:
			jobType = JobType.CHOP;
			break;
		}

		if (jobType != null) {
			// 先停止工作
			jobService.stop(vid);
			jobInternal.preStartJob(vid, PlaceType.PLACE, 0, jobType);
			doCollect(vid, areaId, jobType, jobInternal.getJobTime(jobType, vid, itemId), service);
		} else {
			log.error("没有这个工作{}", resId);
		}

	}

	private void doCollect(int vid, int areaId, JobType jobType, JobTimeData data, AbstractService service) {
		int resId = Data.areaList.get(areaId);
		PlaceConfig placeConfig = placeConfigCache.placeMap.get(resId);

		// 解锁且资源点没有这个人
		Data.villageFutures.put(vid, service.delay(new Task() {

			@Override
			public void execute() {
				itemInternal.createItemIfAbsent(placeConfig.getItemId());
				itemInternal.addItem(placeConfig.getItemId(), 1);
				techInternal.think(vid);
			}

			@Override
			public void afterExecute() {
				doCollect(vid, areaId, jobType, jobInternal.getJobTime(jobType, vid, 0), service);
			}

		}, data.getDelayTime()));

	}
}
