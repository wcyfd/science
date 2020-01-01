package com.science.game.entity;

import java.util.ArrayList;
import java.util.List;

public class Place {
	public enum Type {
		PLACE, ITEM
	}

	private int placeId;
	private List<Integer> vid = new ArrayList<>();

	/**
	 * 获取工作中的村民的id
	 * 
	 * @return
	 */
	public List<Integer> getVillages() {
		return this.vid;
	}

	public int getPlaceId() {
		return this.placeId;
	}

	public static Place create(int placeId) {
		Place place = new Place();
		place.placeId = placeId;
		return place;
	}

}
