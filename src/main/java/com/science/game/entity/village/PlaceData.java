package com.science.game.entity.village;

import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;

import lombok.Getter;
import lombok.Setter;

public class PlaceData {
	@Getter
	private int vid;
	@Getter
	@Setter
	private Place place;
	@Getter
	@Setter
	private PlaceType placeType;

	public PlaceData(int vid) {
		this.vid = vid;
	}
}
