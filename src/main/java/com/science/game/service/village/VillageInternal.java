package com.science.game.service.village;

import com.science.game.entity.Village;

public interface VillageInternal {

	Village getVillage(int vid);

	/**
	 * 思考
	 * 
	 * @param vid
	 */
	void think(int vid);

}
