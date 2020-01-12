package com.science.game.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.entity.scene.ItemData;
import com.science.game.entity.scene.LabData;
import com.science.game.entity.scene.PlaceData;
import com.science.game.entity.scene.VillageData;

import lombok.Getter;

@Component
public class Scene {

	@Autowired
	@Getter
	private LabData labData;

	@Autowired
	@Getter
	private PlaceData placeData;

	@Autowired
	@Getter
	private ItemData itemData;

	@Autowired
	@Getter
	private VillageData villageData;

}
