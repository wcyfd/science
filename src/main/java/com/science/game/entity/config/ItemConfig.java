package com.science.game.entity.config;

import java.util.HashSet;
import java.util.Set;

public class ItemConfig {
	public enum ItemType {
		RES, ITEM
	}

	private int itemId;
	private String name;
	private ItemType type;
	private int practice;
	private int developPoint;
	private int effect;
	private int age;
	private long unitTotal;
	private int unitVelocity;

	private Set<Integer> jobs = new HashSet<>();

	public int getEffect() {
		return effect;
	}

	public void setEffect(int effect) {
		this.effect = effect;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public int getPractice() {
		return practice;
	}

	public void setPractice(int practice) {
		this.practice = practice;
	}

	public int getDevelopPoint() {
		return developPoint;
	}

	public void setDevelopPoint(int developPoint) {
		this.developPoint = developPoint;
	}

	@Override
	public String toString() {
		return name;
	}

	public long getUnitTotal() {
		return unitTotal;
	}

	public void setUnitTotal(long unitTime) {
		this.unitTotal = unitTime;
	}

	public int getUnitVelocity() {
		return unitVelocity;
	}

	public void setUnitVelocity(int unitCount) {
		this.unitVelocity = unitCount;
	}

	public Set<Integer> getJobs() {
		return jobs;
	}

}
