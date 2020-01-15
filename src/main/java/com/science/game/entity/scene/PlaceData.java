package com.science.game.entity.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;

import lombok.Getter;
import lombok.Setter;

public class PlaceData {

	private Map<PlaceType, Map<Integer, Place>> placeTypeMap = new HashMap<>();
	@Getter
	private List<Integer> areaList = new ArrayList<>();
	@Setter
	@Getter
	private AtomicInteger increAreaId = new AtomicInteger();

	public PlaceData() {
		this.increAreaId.set(-1);

		PlaceType[] placeTypes = PlaceType.values();
		for (PlaceType placeType : placeTypes) {
			placeTypeMap.put(placeType, new HashMap<>());
		}
	}

	public Map<Integer, Place> getPlaceMapByType(PlaceType type) {
		return placeTypeMap.get(type);
	}

	public int getAreaId() {
		return increAreaId.get();
	}
}
