package com.science.game.service.job.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.service.AbstractService;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;
import com.science.game.service.tech.TechInternal;

import game.quick.window.Task;

@Component
public class AssartModule {

	@Autowired
	private JobService jobService;

	@Autowired
	private TechInternal techInternal;

	@Autowired
	private JobInternal jobInternal;

	public void assart(int vid, AbstractService service) {
		if (Data.areaId < Data.areaList.size()) {
			jobService.stop(vid);

			// 荒地的地点是-2
			jobInternal.preStartJob(vid, PlaceType.PLACE, -2, JobType.ASSART);

			long jobTime = jobInternal.getJobTime(JobType.ASSART, vid, 0);
			doAssart(vid, jobTime, service);
		}
	}

	private void doAssart(int vid, long second, AbstractService service) {

		Data.villageFutures.put(vid, service.delay(new Task() {

			@Override
			public void execute() {
				Data.areaId++;
				Data.resPlace.putIfAbsent(Data.areaId, Place.create(Data.areaId));// 创建一个资源点位置

				techInternal.think(vid);
			}

			@Override
			public void afterExecute() {
				if (Data.areaId < Data.areaList.size()) {
					long jobTime = jobInternal.getJobTime(JobType.ASSART, vid, 0);
					doAssart(vid, jobTime, service);
				} else {
					jobService.stop(vid);
				}
			}
		}, second));

	}
}
