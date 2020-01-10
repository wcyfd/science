package com.science.game.service.assart;

import com.science.game.service.ServiceInterface;

/**
 * 开荒活动
 * 
 * @author heyue
 *
 */
public interface AssartService extends ServiceInterface {
	/**
	 * 访问某个荒地
	 * 
	 * @param vid     村民id
	 * @param placeId
	 */
	void assart(int vid);
}
