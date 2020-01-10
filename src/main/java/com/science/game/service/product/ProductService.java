package com.science.game.service.product;

import com.science.game.service.ServiceInterface;

public interface ProductService extends ServiceInterface {
	/**
	 * 生产
	 * 
	 * @param vid
	 * @param itemId
	 */
	void product(int vid, int itemId);
}
