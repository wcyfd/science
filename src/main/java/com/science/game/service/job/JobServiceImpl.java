package com.science.game.service.job;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
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
		}

	}

	@Override
	public void assart(int vid) {
		doAssart(vid, 5);
	}

	private void doAssart(int vid, int second) {
		stopWork(vid);
		if (Data.areaId < Data.areaList.size()) {

			Village village = Data.villages.get(vid);
			village.setJob("assart");
			ScheduledFuture<?> future = this.delay(new Task() {

				@Override
				public void execute() {
					if (Data.areaId < Data.areaList.size())
						Data.areaId++;
				}

				@Override
				public void afterExecute() {
					Data.villageFutures.remove(vid);
					doAssart(vid, second);
				}
			}, second, TimeUnit.SECONDS);

			Data.villageFutures.put(vid, future);
		}

	}

	@Override
	public void stopWork(int vid) {

		ScheduledFuture<?> future = Data.villageFutures.remove(vid);
		if (future != null)
			future.cancel(false);

		Village v = Data.villages.get(vid);
		v.setJob(null);
	}

	@Override
	public void collect(int vid, int areaId) {
		if (areaId < Data.areaId) {
			stopWork(vid);
			this.delay(new Task() {

				@Override
				public void afterExecute() {

				}

				@Override
				public void execute() {

				}

			}, 2, TimeUnit.SECONDS);
		}
	}

}
