package com.science.game.entity;

import java.util.HashSet;
import java.util.Set;

public class Place {

	private int placeId;
	private Set<Integer> villageIds = new HashSet<>();

	public int getPlaceId() {
		return this.placeId;
	}

	/**
	 * 获取工作中的村民的id
	 * 
	 * @return
	 */
	public Set<Integer> getVillageIds() {
		return villageIds;
	}

	public static Place create(int placeId) {
		Place place = new Place();
		place.placeId = placeId;
		return place;
	}

}
