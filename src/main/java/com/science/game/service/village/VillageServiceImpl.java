package com.science.game.service.village;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.service.AbstractService;
import com.science.game.service.village.module.CreateVillageModule;

@Service
public class VillageServiceImpl extends AbstractService implements VillageService {

	@Autowired
	private CreateVillageModule createVillageModule;

	@Override
	public void initCache() {
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
		createVillageModule.createVillage();
	}

}
