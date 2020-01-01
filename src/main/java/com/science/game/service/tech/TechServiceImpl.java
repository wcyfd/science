package com.science.game.service.tech;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.cache.config.ThinkConfigCache;
import com.science.game.entity.Village;
import com.science.game.entity.config.ThinkConfig;
import com.science.game.service.AbstractService;

@Service
public class TechServiceImpl extends AbstractService implements TechService, TechInternal {

	@Autowired
	private ThinkConfigCache thinkConfigCache;

	@Override
	protected void dispatch(String cmd, List<String> args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initCache() {
	}

	@Override
	public void think(int vid) {
		Village village = Data.villages.get(vid);
		int jobId = village.getJobId();
		List<ThinkConfig> list = thinkConfigCache.jobThinkMap.get(jobId);
		// 检查是否是工作地点允许

		// 检查目前解锁资源是否允许出现这个想法

	}

}
