package com.science.game.service.job;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.entity.Village;
import com.science.game.entity.config.PlaceConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.tech.TechInternal;
import com.science.game.service.tech.TechService;

import game.quick.window.Task;

@Service
public class JobServiceImpl extends AbstractService implements JobService {

	@Autowired
	private PlaceConfigCache placeConfigCache;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private TechInternal techInternal;

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "assart":
			assart(Integer.valueOf(args.get(0)));
			break;
		case "stop":
			stop(Integer.valueOf(args.get(0)));
			break;
		case "collect":
			collect(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
			break;
		}

	}

	@Override
	public void initCache() {
		Data.resPlace.put(1, Place.create(1));// 开荒地区
	}

	@Override
	public void assart(int vid) {
		if (Data.areaId < Data.areaList.size()) {
			stop(vid);

			Village v = Data.villages.get(vid);
			v.setJobId(1);
			v.setPlaceId(1);
			v.setPlaceType(Place.Type.PLACE);

			doAssart(vid, 5);
		}
	}

	private void doAssart(int vid, int second) {

		Data.villageFutures.put(vid, this.delay(new Task() {

			@Override
			public void execute() {
				Data.areaId++;
				Data.resPlace.put(Data.areaId, Place.create(Data.areaId));// 创建一个资源点位置

				techInternal.think(vid);
			}

			@Override
			public void afterExecute() {
				if (Data.areaId < Data.areaList.size())
					doAssart(vid, second);
				else {
					stop(vid);
				}
			}
		}, second, TimeUnit.SECONDS));

	}

	@Override
	public void collect(int vid, int areaId) {
		Place place = Data.resPlace.get(areaId);

		if (place != null && !place.getVillages().contains(vid)) {
			// 先停止工作
			stop(vid);

			place.getVillages().add(vid);
			Village v = Data.villages.get(vid);

			int resId = Data.areaList.get(areaId);
			int itemId = placeConfigCache.placeMap.get(resId).getItemId();
			switch (resId) {
			case 3:
				v.setJobId(2);
				v.setPlaceId(itemId);
				v.setPlaceType(Place.Type.PLACE);
				doCollect(vid, areaId, 2);
				break;
			case 4:
				v.setJobId(3);
				v.setPlaceId(itemId);
				v.setPlaceType(Place.Type.PLACE);
				doCollect(vid, areaId, 2);
				break;
			}

		}
	}

	private void doCollect(int vid, int areaId, int second) {
		int resId = Data.areaList.get(areaId);
		PlaceConfig placeConfig = placeConfigCache.placeMap.get(resId);

		// 解锁且资源点没有这个人

		Data.villageFutures.put(vid, this.delay(new Task() {

			@Override
			public void execute() {
				Item item = itemInternal.getItem(placeConfig.getItemId());
				item.setNum(item.getNum() + 1);
				techInternal.think(vid);
			}

			@Override
			public void afterExecute() {
				doCollect(vid, areaId, 2);
			}

		}, second, TimeUnit.SECONDS));

	}

	@Override
	public void stop(int vid) {
		Village v = Data.villages.get(vid);
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

	@Override
	public void develop() {

	}

	@Override
	public void product() {

	}

}
