package com.science.game.service.job.module;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.entity.Village;
import com.science.game.entity.config.PlaceConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobService;
import com.science.game.service.tech.TechInternal;

import game.quick.window.Task;

@Component
public class CollectModule {

	@Autowired
	private JobService jobService;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private TechInternal techInternal;

	@Autowired
	private PlaceConfigCache placeConfigCache;

	public void collect(int vid, int areaId, AbstractService service) {
		Place place = Data.resPlace.get(areaId);

		if (place != null && !place.getVillages().contains(vid)) {
			// 先停止工作
			jobService.stop(vid);

			place.getVillages().add(vid);
			Village v = Data.villages.get(vid);

			int resId = Data.areaList.get(areaId);
			int itemId = placeConfigCache.placeMap.get(resId).getItemId();
			switch (resId) {
			case 3:
				v.setJobId(2);
				v.setPlaceId(itemId);
				v.setPlaceType(Place.Type.PLACE);
				doCollect(vid, areaId, 2, service);
				break;
			case 4:
				v.setJobId(3);
				v.setPlaceId(itemId);
				v.setPlaceType(Place.Type.PLACE);
				doCollect(vid, areaId, 2, service);
				break;
			}

		}
	}

	private void doCollect(int vid, int areaId, int second, AbstractService service) {
		int resId = Data.areaList.get(areaId);
		PlaceConfig placeConfig = placeConfigCache.placeMap.get(resId);

		// 解锁且资源点没有这个人

		Data.villageFutures.put(vid, service.delay(new Task() {

			@Override
			public void execute() {
				Item item = itemInternal.createItemIfAbsent(placeConfig.getItemId());
				item.setNum(item.getNum() + 1);
				techInternal.think(vid);
			}

			@Override
			public void afterExecute() {
				doCollect(vid, areaId, 2, service);
			}

		}, second, TimeUnit.SECONDS));

	}
}
