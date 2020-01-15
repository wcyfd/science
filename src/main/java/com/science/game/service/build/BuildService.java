package com.science.game.service.build;

import com.science.game.service.ServiceInterface;

/**
 * 建造服务
 * 
 * @author heyue
 *
 */
public interface BuildService extends ServiceInterface {

	/**
	 * 开始建造
	 * 
	 * @param buildId
	 */
	void startBuild(int buildId);

	/**
	 * 建造模块
	 * 
	 * @param vid
	 * @param moduleId
	 */
	void build(int vid, int moduleId);

	/**
	 * 加入建造
	 * 
	 * @param vid
	 * @param buildOnlyId
	 */
	void joinTeam(int vid, int buildOnlyId);
}
