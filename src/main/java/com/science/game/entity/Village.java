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
	private PlaceType placeType;
	// 每种道具的熟练度
	private Map<Integer, AtomicInteger> skillValues = new HashMap<>();
	// 装备列表
	private Map<Integer, Item> equips = new HashMap<>();

	private JobTimeData jobTimeData = new JobTimeData();

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

	public void setPlaceType(PlaceType placeType) {
		this.placeType = placeType;
	}

	public PlaceType getPlaceType() {
		return placeType;
	}

	public Map<Integer, AtomicInteger> getSkillValues() {
		return skillValues;
	}

	public Map<Integer, Item> getEquips() {
		return equips;
	}

	public JobTimeData getJobTimeData() {
		return this.jobTimeData;
	}

	@Override
	public String toString() {
		JobConfig config = ConfigCache.job.jobMap.get(jobId);

		return "Village [id=" + id + ", job=" + (config == null ? null : config.getJob()) + "]";
	}

}
