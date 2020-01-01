package com.science.game.service.nature;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
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
	}

}
