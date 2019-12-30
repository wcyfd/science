package com.science.game.service.job;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Res;
import com.science.game.entity.Village;
import com.science.game.service.AbstractService;

import game.quick.window.Task;

@Service
public class JobServiceImpl extends AbstractService implements JobService {

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "assart":
			assart(Integer.valueOf(args.get(0)));
			break;
		case "stopwork":
			stopWork(Integer.valueOf(args.get(0)));
			break;
		case "collect":
			collect(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
			break;
		}

	}

	@Override
	public void assart(int vid) {
		if (Data.areaId < Data.areaList.size()) {
			stopWork(vid);
			Village v = Data.villages.get(vid);
			v.setJob("assart");
			doAssart(vid, 5);
		}
	}

	private void doAssart(int vid, int second) {

		ScheduledFuture<?> future = this.delay(new Task() {

			@Override
			public void execute() {
				Data.areaId++;
			}

			@Override
			public void afterExecute() {
				if (Data.areaId < Data.areaList.size())
					doAssart(vid, second);
				else {
					stopWork(vid);
				}
			}
		}, second, TimeUnit.SECONDS);

		Data.villageFutures.put(vid, future);

	}

	@Override
	public void collect(int vid, int areaId) {
		Res res = Data.areaList.get(areaId);

		if (areaId < Data.areaId && !res.getPosition().contains(vid)) {
			// 先停止工作
			stopWork(vid);
			res.getPosition().add(vid);
			Village v = Data.villages.get(vid);
			v.setPos(res);
			v.setJob("collect");
			if (!Data.itemMap.containsKey(res.getProto().getItemId())) {
				Data.itemMap.putIfAbsent(res.getProto().getItemId(), Item.create(res.getProto().getItemId()));
			}
			doCollect(vid, areaId, 2);
		}
	}

	private void doCollect(int vid, int areaId, int second) {
		Res res = Data.areaList.get(areaId);

		// 解锁且资源点没有这个人

		ScheduledFuture<?> f = this.delay(new Task() {

			@Override
			public void execute() {
				Item item = Item.create(res.getProto().getItemId());
				Data.itemMap.putIfAbsent(item.getProto().getItemId(), item);
				item = Data.itemMap.get(item.getProto().getItemId());
				item.setNum(item.getNum() + 1);
			}

			@Override
			public void afterExecute() {
				doCollect(vid, areaId, 2);
			}

		}, second, TimeUnit.SECONDS);

		Data.villageFutures.put(vid, f);

	}

	@Override
	public void stopWork(int vid) {
		Village v = Data.villages.get(vid);
		ScheduledFuture<?> future = Data.villageFutures.remove(vid);
		if (future != null)
			future.cancel(false);

		v.setJob(null);
		if (v.getPos() != null)
			v.getPos().getPosition().remove((Integer) vid);

	}

}
