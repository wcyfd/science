package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.science.game.entity.scene.BuildData;
import com.science.game.entity.scene.ClimateData;
import com.science.game.entity.scene.DamageData;
import com.science.game.entity.scene.ItemData;
import com.science.game.entity.scene.LabData;
import com.science.game.entity.scene.PlaceData;
import com.science.game.entity.scene.VillageData;

import lombok.Getter;

@Component
public class Scene {
	private static AtomicInteger ID = new AtomicInteger();

	@Getter
	private int id;

	@Getter
	private LabData labData;

	@Getter
	private PlaceData placeData;

	@Getter
	private ItemData itemData;

	@Getter
	private VillageData villageData;

	@Getter
	private BuildData buildData;

	@Getter
	private ClimateData climateData;

	@Getter
	private DamageData damageData;

	public Scene() {
		id = ID.incrementAndGet();
		labData = new LabData();
		placeData = new PlaceData();
		itemData = new ItemData();
		villageData = new VillageData();
		buildData = new BuildData();
		climateData = new ClimateData();
		damageData = new DamageData();
	}

}
