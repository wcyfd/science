package com.science.game.service.forest;

import com.science.game.service.ServiceInterface;

public interface ForestService extends ServiceInterface {
	/**
	 * 砍伐
	 * 
	 * @param vid
	 * @param placeId
	 */
	void chop(int vid, int placeId);
}
