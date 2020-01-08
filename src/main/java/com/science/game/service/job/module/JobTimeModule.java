package com.science.game.service.job.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.entity.JobType;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.JobConfig;

@Component
public class JobTimeModule {

	@Autowired
	private JobConfigCache jobConfigCache;

	@Autowired
	private ItemConfigCache itemConfigCache;

	/**
	 * 获取工作时间
	 * 
	 * @param jobId
	 * @param vid
	 * @return
	 */
	public long getJobTime(JobType jobType, int vid, int itemId) {
		switch (jobType) {
		case ASSART:
		case CHOP:
		case DIG: {
			JobConfig jobConfig = jobConfigCache.jobMap.get(jobType.getJobId());
			return jobConfig.getUnitTotal() / jobConfig.getUnitVelocity();
		}
		case DEVELOP:
		case PRODUCT: {
			ItemConfig config = itemConfigCache.itemMap.get(itemId);
			return config.getUnitTotal() / config.getUnitVelocity();
		}
		case NULL:
			break;
		default:
			break;

		}
		return 0L;
	}

	public long getRemainJobTime(int jobId, int vid, long startTime, long needTime) {
		return 0L;
	}

}
