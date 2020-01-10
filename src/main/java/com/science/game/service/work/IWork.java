package com.science.game.service.work;

import com.science.game.entity.village.WorkData;

/**
 * 
 * @author heyue
 *
 */
public interface IWork {

	void enterWork(WorkData workData);

	/**
	 * 工作循环接口
	 * 
	 * @param v
	 */
	void workLoop(WorkData workData);

	
	void exitWork(WorkData workData);
}
