package com.science.game.service.place;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.data.DataCenter;
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
	private DataCenter dataCenter;

	@Autowired
	private PlaceInternal placeInternal;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	protected void initCache() {
		Scene scene = dataCenter.getScene();
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
		Scene scene = dataCenter.getScene();
		return scene.getPlaceData().getPlaceMapByType(type).get(id);

	}

	@Override
	public Place createIfAbsent(PlaceType type, int id) {
		Scene scene = dataCenter.getScene();

		if (type == null)
			return null;

		Map<Integer, Place> placeMap = scene.getPlaceData().getPlaceMapByType(type);

		if (!placeMap.containsKey(id)) {
			placeMap.putIfAbsent(id, Place.create(id));
		}

		return placeMap.get(id);
	}

	@Override
	public boolean isMaxPlace() {
		Scene scene = dataCenter.getScene();
		return scene.getPlaceData().getAreaId() >= scene.getPlaceData().getAreaList().size();
	}

	@Override
	public void deletePlace(PlaceType placeType, int id) {
		PlaceData placeData = dataCenter.getScene().getPlaceData();
		if (placeType == null)
			return;

		placeData.getPlaceMapByType(placeType).remove(id);

	}
}
