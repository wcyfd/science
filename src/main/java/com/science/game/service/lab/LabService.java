package com.science.game.service.lab;

import com.science.game.service.ServiceInterface;

public interface LabService extends ServiceInterface {

	/**
	 * 开发
	 * 
	 * @param vid
	 * @param itemId
	 */
	void develop(int vid, int itemId);
}
