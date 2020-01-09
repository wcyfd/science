package com.science.game.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Village {
	private static AtomicInteger ID = new AtomicInteger(1);
	private int id;
	// 每种道具的熟练度
	private Map<Integer, AtomicInteger> skillValues = new HashMap<>();
	// 装备列表
	private Map<Integer, Item> equips = new HashMap<>();

	private JobTimeData jobTimeData = new JobTimeData();

	private JobData jobData = new JobData();

	public Village() {
		id = ID.getAndIncrement();
	}

	public int getId() {
		return id;
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

	public JobData getJobData() {
		return jobData;
	}

}
