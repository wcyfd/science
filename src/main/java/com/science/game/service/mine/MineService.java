package com.science.game.service.mine;

import com.science.game.service.ServiceInterface;

public interface MineService extends ServiceInterface {
	/**
	 * 
	 * @param vid
	 * @param placeId
	 */
	void dig(int vid, int placeId);
}
