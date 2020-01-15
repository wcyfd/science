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
	 * 申请建造
	 * 
	 * @param buildId
	 */
	void applyBuild(int buildId);

	/**
	 * 建造模块
	 * 
	 * @param vid
	 * @param moduleId
	 */
	void build(int vid, int moduleId);

	/**
	 * 加入团队
	 * 
	 * @param vid
	 * @param buildOnlyId
	 */
	void joinTeam(int vid, int buildOnlyId);

}
