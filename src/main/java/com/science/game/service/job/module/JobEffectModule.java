package com.science.game.service.job.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ThinkConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.JobType;
import com.science.game.entity.Village;
import com.science.game.entity.config.ThinkConfig;

@Component
public class JobEffectModule {

	@Autowired
	private ThinkConfigCache thinkConfigCache;

	public int addEffect(Village v, JobType jobType) {
		int result = 0;
		List<ThinkConfig> list = thinkConfigCache.jobThinkMap.get(jobType.getJobId());
		if (list == null)
			return result;
		for (ThinkConfig config : list) {
			Item item = v.getEquips().get(config.getItemId());
			if (item != null) {
				int effect = item.getProto().getEffect();
				result += effect;
			}
		}
		return result;
	}

}
