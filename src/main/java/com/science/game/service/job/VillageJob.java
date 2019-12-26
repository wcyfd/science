package com.science.game.service.job;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.science.game.cache.Data;
import com.science.game.entity.Village;

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
			ScheduledFuture<?> future = Data.scheduled.schedule(new Runnable() {

				@Override
				public void run() {
					onExecute(vid);
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
			ScheduledFuture<?> future = Data.scheduled.schedule(new Runnable() {

				@Override
				public void run() {
					stopWork(vid);
					Data.villageFutures.remove(vid);
					onExecute(vid);
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
