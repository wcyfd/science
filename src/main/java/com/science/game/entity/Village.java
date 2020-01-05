package com.science.game.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.config.JobConfig;

public class Village {
	private static AtomicInteger ID = new AtomicInteger(1);
	private int id;
	private int jobId;
	private int placeId;
	private Place.Type placeType;
	// 每种道具的熟练度
	private Map<Integer, Integer> skillValues = new HashMap<>();

	public Village() {
		id = ID.getAndIncrement();
	}

	public int getId() {
		return id;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getJobId() {
		return jobId;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public void setPlaceType(Place.Type placeType) {
		this.placeType = placeType;
	}

	public Place.Type getPlaceType() {
		return placeType;
	}

	public Map<Integer, Integer> getSkillValues() {
		return skillValues;
	}

	@Override
	public String toString() {
		JobConfig config = ConfigCache.job.jobMap.get(jobId);

		return "Village [id=" + id + ", job=" + (config == null ? null : config.getJob()) + "]";
	}

}
