package com.science.game.service.village;

import com.science.game.entity.Village;

public interface VillageInternal {

	Village getVillage(int vid);

	void think(int vid);

}
