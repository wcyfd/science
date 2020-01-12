package com.science.game.service.place;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.entity.scene.PlaceData;
import com.science.game.service.AbstractService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlaceServiceImpl extends AbstractService implements PlaceInternal {

	@Autowired
	private Scene scene;

	@Autowired
	private PlaceInternal placeInternal;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	protected void initCache() {

		scene.getPlaceData().getAreaList().addAll(Arrays.asList(2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2,
				3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4));
		scene.getPlaceData().getIncreAreaId().set(5);
		int size = scene.getPlaceData().getIncreAreaId().get();
		for (int i = 0; i < size; i++) {
			placeInternal.createIfAbsent(PlaceType.PLACE, i);
		}
		// 生成荒地的位置
		placeInternal.createIfAbsent(PlaceType.PLACE, -2);
		log.info("地形初始化");
	}

	@Override
	public void enter(Village v, PlaceType placeType, int placeId) {
		exit(v);

		Place place = this.getPlace(placeType, placeId);
		v.getPlaceData().setPlace(place);
		v.getPlaceData().setPlaceType(placeType);

		place.getVillageIds().add(v.getId());
	}

	@Override
	public void exit(Village v) {
		Place place = v.getPlaceData().getPlace();

		if (place != null)
			place.getVillageIds().remove(v.getId());
		v.getPlaceData().setPlace(null);

		v.getPlaceData().setPlaceType(null);
	}

	@Override
	public Place getPlace(PlaceType type, int id) {
		if (type == PlaceType.PLACE) {
			return scene.getPlaceData().getResPlace().get(id);
		} else if (type == PlaceType.ITEM) {
			return scene.getPlaceData().getItemPlace().get(id);
		}

		log.info("读取地点失败 placeType={},placeId={}", type, id);
		return null;
	}

	@Override
	public Place createIfAbsent(PlaceType type, int id) {

		Map<Integer, Place> placeMap = null;
		if (type == PlaceType.ITEM) {
			placeMap = scene.getPlaceData().getItemPlace();
		} else if (type == PlaceType.PLACE) {
			placeMap = scene.getPlaceData().getResPlace();
		}
		if (placeMap == null) {
			return null;
		}

		if (!placeMap.containsKey(id)) {
			placeMap.putIfAbsent(id, Place.create(id));
		}

		return placeMap.get(id);
	}

	@Override
	public boolean isMaxPlace() {
		return scene.getPlaceData().getAreaId() >= scene.getPlaceData().getAreaList().size();
	}
}
