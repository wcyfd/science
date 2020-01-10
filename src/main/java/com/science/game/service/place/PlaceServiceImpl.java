package com.science.game.service.place;

import java.util.List;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.village.PlaceData;
import com.science.game.service.AbstractService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlaceServiceImpl extends AbstractService implements PlaceInternal {

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void enter(Village v, PlaceType placeType, int placeId) {
		exit(v);

		PlaceData placeData = v.getPlaceData();
		Place place = this.getPlace(placeType, placeId);
		placeData.setPlace(place);
		placeData.setPlaceType(placeType);

		place.getVid2().add(v.getId());
	}

	@Override
	public void exit(Village v) {
		PlaceData placeData = v.getPlaceData();
		Place place = placeData.getPlace();

		if (place != null)
			place.getVid2().remove(v.getId());
		placeData.setPlace(null);

		placeData.setPlaceType(null);
	}

	@Override
	public Place getPlace(PlaceType type, int id) {
		if (type == PlaceType.PLACE) {
			return Data.resPlace.get(id);
		} else if (type == PlaceType.ITEM) {
			return Data.itemPlace.get(id);
		}

		log.info("读取地点失败 placeType={},placeId={}", type, id);
		return null;
	}

}
