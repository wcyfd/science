package com.science.game.service.job;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.science.game.App;
import com.science.game.cache.Data;
import com.science.game.entity.Village;

import game.quick.window.UIRunnable;

public abstract class VillageJob {
	/**
	 * 永久工作
	 * 
	 * @param vid
	 */
	public void workForever(int vid) {
		if (Data.villages.containsKey(vid)) {
			Village v = Data.villages.get(vid);
			v.setJob(this.getClass().getSimpleName());

			ScheduledFuture<?> future = App.win.schedule(new UIRunnable() {

				@Override
				public void execute() {
					onExecute(vid);
				}

				@Override
				public void afterExecute() {
					workForever(vid);
				}
			}, getWorkTime(), TimeUnit.SECONDS);
			Data.villageFutures.put(vid, future);
		} else {
			Data.villageFutures.remove(vid);
		}
	}

	/**
	 * 工作一次
	 * 
	 * @param vid
	 */
	public void workOnce(int vid) {
		if (Data.villages.containsKey(vid)) {
			Data.villages.remove(vid);
			ScheduledFuture<?> future = App.win.schedule(new UIRunnable() {

				@Override
				public void execute() {
					stopWork(vid);
					Data.villageFutures.remove(vid);
					onExecute(vid);
				}

				@Override
				public void afterExecute() {
				}
			}, getWorkTime(), TimeUnit.SECONDS);

			Data.villageFutures.put(vid, future);
		}
	}

	protected abstract void onExecute(int vid);

	public void stopWork(int vid) {
		Village v = Data.villages.get(vid);
		if (v != null) {
			v.setJob("");
		}
		/**
		 * 移除事件
		 */
		ScheduledFuture<?> future = Data.villageFutures.remove(vid);
		if (future != null)
			future.cancel(false);
	}

	protected abstract long getWorkTime();

}
