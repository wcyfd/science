package com.science.game.service.work;

import com.science.game.service.ServiceInterface;

public interface WorkService extends ServiceInterface {
	/**
	 * 停止工作
	 * 
	 * @param vid
	 */
	void stop(int vid);
}
