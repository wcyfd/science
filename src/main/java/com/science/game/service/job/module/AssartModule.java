package com.science.game.service.job.module;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Place;
import com.science.game.entity.Village;
import com.science.game.service.AbstractService;
import com.science.game.service.job.JobService;
import com.science.game.service.tech.TechInternal;

import game.quick.window.Task;

@Component
public class AssartModule {

	@Autowired
	private JobService jobService;

	@Autowired
	private TechInternal techInternal;

	public void assart(int vid, AbstractService service) {
		if (Data.areaId < Data.areaList.size()) {
			jobService.stop(vid);

			Village v = Data.villages.get(vid);
			v.setJobId(1);
			v.setPlaceId(1);
			v.setPlaceType(Place.Type.PLACE);

			doAssart(vid, 5, service);
		}
	}

	private void doAssart(int vid, int second, AbstractService service) {

		Data.villageFutures.put(vid, service.delay(new Task() {

			@Override
			public void execute() {
				Data.areaId++;
				Data.resPlace.putIfAbsent(Data.areaId, Place.create(Data.areaId));// 创建一个资源点位置

				techInternal.think(vid);
			}

			@Override
			public void afterExecute() {
				if (Data.areaId < Data.areaList.size())
					doAssart(vid, second, service);
				else {
					jobService.stop(vid);
				}
			}
		}, second, TimeUnit.SECONDS));

	}
}
