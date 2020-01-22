package com.science.game.service.climate;

import com.science.game.service.ServiceInterface;

/**
 * 气候服务
 * 
 * @author aimfd
 *
 */
public interface ClimateService extends ServiceInterface {
	/**
	 * 设置值
	 * 
	 * @param id
	 * @param val
	 */
	void value(int id, int val);
}
