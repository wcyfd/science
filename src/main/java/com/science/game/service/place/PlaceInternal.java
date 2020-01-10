package com.science.game.service.place;

import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;

/**
 * 场地服务
 * 
 * @author heyue
 *
 */
public interface PlaceInternal {
	/**
	 * 进入某个地方
	 * 
	 * @param vid     村民id
	 * @param placeId 地点id
	 */
	void enter(Village v, PlaceType placeType, int placeId);

	/**
	 * 退出某个地方
	 * 
	 * @param vid 村民id
	 */
	void exit(Village v);

	/**
	 * 场地获取
	 * 
	 * @param type 场地类型
	 * @param id   场地id
	 * @return
	 */
	Place getPlace(PlaceType type, int id);
}
