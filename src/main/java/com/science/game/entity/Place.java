package com.science.game.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

public class Place {

	private int placeId;
	private List<Integer> vid = new ArrayList<>();
	@Getter
	private Set<Integer> vid2 = new HashSet<>();

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
