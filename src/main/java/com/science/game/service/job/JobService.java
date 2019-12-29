package com.science.game.service.job;

/**
 * 工作
 * 
 * @author aimfd
 *
 */
public interface JobService {
	/**
	 * 拓荒
	 * 
	 * @param vid
	 */
	void assart(int vid);

	/**
	 * 停止工作
	 * 
	 * @param vid
	 */
	void stopWork(int vid);

	/**
	 * 采集
	 * 
	 * @param vid
	 * @param areaId
	 */
	void collect(int vid, int areaId);
}
