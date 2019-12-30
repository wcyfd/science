package com.science.game.service.village;

import java.util.List;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Village;
import com.science.game.service.AbstractService;

@Service
public class VillageServiceImpl extends AbstractService implements VillageService {

	@Override
	public void initCache() {
		createVillage();
	}

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "recruite":
			recruite();
			break;
		}
	}

	@Override
	public void recruite() {
		createVillage();

	}

	private void createVillage() {
		Village v = new Village();
		Data.villages.put(v.getId(), v);
	}
}
