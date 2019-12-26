package com.science.game.service.job;

import com.science.game.cache.Data;

/**
 * 开荒工作
 * 
 * @author heyue
 *
 */
public class AssartJob extends VillageJob {

	@Override
	protected void onExecute(int vid) {
		Data.resource++;
	}

	@Override
	protected long getWorkTime() {
		return 5;
	}

}
