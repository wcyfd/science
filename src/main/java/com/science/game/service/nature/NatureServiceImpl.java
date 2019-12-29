package com.science.game.service.nature;

import java.util.List;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConfigCache;
import com.science.game.service.Service;

public class NatureServiceImpl extends Service implements NatureService {

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	protected void initCache() {
		int[] resArray = { 1, 3, 1, 3, 2, 1, 2, 2, 3 };
		for (int i : resArray) {
			Data.areaList.add(ConfigCache.res.resMap.get(i));
		}
	}

}
