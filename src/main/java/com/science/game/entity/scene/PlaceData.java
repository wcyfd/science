package com.science.game.entity.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.science.game.entity.Place;

import lombok.Getter;
import lombok.Setter;

@Component
public class PlaceData {
	@Getter
	private Map<Integer, Place> resPlace = new HashMap<>();
	@Getter
	private Map<Integer, Place> itemPlace = new HashMap<>();
	@Getter
	private List<Integer> areaList = new ArrayList<>();
	@Setter
	@Getter
	private AtomicInteger increAreaId = new AtomicInteger();

	public PlaceData() {
		this.increAreaId.set(-1);
	}

	public int getAreaId() {
		return increAreaId.get();
	}
}
