package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.config.JobConfig;

import lombok.Data;

@Data
public class Village {
	private static AtomicInteger ID = new AtomicInteger(1);
	private int id;
	private int jobId;
	private int placeId;
	private Place.Type placeType;

	public Village() {
		id = ID.getAndIncrement();
	}

	@Override
	public String toString() {
		JobConfig config = ConfigCache.job.jobMap.get(jobId);

		return "Village [id=" + id + ", job=" + (config == null ? null : config.getJob()) + "]";
	}

}
