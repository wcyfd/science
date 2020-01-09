package com.science.game.service.nature;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Place;
import com.science.game.service.AbstractService;

@Service
public class NatureServiceImpl extends AbstractService implements NatureService {

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void initCache() {
		Data.areaList.addAll(Arrays.asList(2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3,
				4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4));
//		Data.areaList.addAll(Arrays.asList(2));
		Data.areaId = 5;
		for (int i = 0; i < Data.areaId; i++) {
			Data.resPlace.putIfAbsent(i, Place.create(i));
		}

		// 生成荒地的位置
		Data.resPlace.putIfAbsent(-2, Place.create(-2));

	}

}
