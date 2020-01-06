package com.science.game.entity.config;

public class ItemConfig {
	public enum ItemType {
		RES, ITEM
	}

	private int itemId;
	private String name;
	private ItemType type;
	private int practice;
	private int developPoint;

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

	@Override
	public String toString() {
		return name;
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

}
