package com.science.game.service.nature;

import java.util.List;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConfigCache;
import com.science.game.service.AbstractService;

@Service
public class NatureServiceImpl extends AbstractService implements NatureService {

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void initCache() {
		int[] resArray = { 1, 3, 1, 3, 2, 1, 2, 2, 3 };
		for (int i : resArray) {
			Data.areaList.add(ConfigCache.res.resMap.get(i));
		}
	}

}
