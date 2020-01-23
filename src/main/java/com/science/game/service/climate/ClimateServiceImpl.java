package com.science.game.service.climate;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.config.ConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Scene;
import com.science.game.entity.config.ClimateConfig;
import com.science.game.service.AbstractService;

@Service
public class ClimateServiceImpl extends AbstractService implements ClimateService, ClimateInternal {

	@Autowired
	private DataCenter dataCenter;

	@Autowired
	private ClimateInternal climateInternal;

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "value":
			value(i.i(), i.i());
			break;
		}
	}

	@Override
	protected void initCache() {
		initSceneClimateData();
	}

	private void initSceneClimateData() {
		Scene scene = dataCenter.getScene();
		Map<Integer, AtomicInteger> map = scene.getClimateData().getParams();
		for (ClimateConfig c : ConfigCache.climate.map.values()) {
			map.putIfAbsent(c.getId(), new AtomicInteger());
			map.get(c.getId()).set(c.getVal());
		}
	}

	@Override
	public void value(int id, int val) {
		climateInternal.setValue(id, val);
	}

	@Override
	public void setValue(int id, int val) {
		dataCenter.getScene().getClimateData().getRefById(id).set(val);
	}

}
