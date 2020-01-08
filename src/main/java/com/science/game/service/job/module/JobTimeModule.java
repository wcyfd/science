package com.science.game.service.job.module;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.cache.config.ThinkConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.JobTimeData;
import com.science.game.entity.JobType;
import com.science.game.entity.Village;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.JobConfig;
import com.science.game.entity.config.ThinkConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.job.JobInternal;
import com.science.game.service.village.VillageInternal;

import game.quick.window.Task;

@Component
public class JobTimeModule {

	@Autowired
	private JobConfigCache jobConfigCache;

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private ThinkConfigCache thinkConfigCache;

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private JobInternal jobInternal;

	/**
	 * 获取工作时间
	 * 
	 * @param jobId
	 * @param vid
	 * @return
	 */
	public JobTimeData getJobTime(JobType jobType, int vid, int itemId) {
		Village v = villageInternal.getVillage(vid);
		JobTimeData data = villageInternal.getVillage(vid).getJobTimeData();
		switch (jobType) {
		case ASSART:
		case CHOP:
		case DIG: {
			JobConfig jobConfig = jobConfigCache.jobMap.get(jobType.getJobId());
			data.setTotal(jobConfig.getUnitTotal());
			data.setVelocity(jobConfig.getUnitVelocity());
			addEffect(v, jobType, data);
			return data;
		}
		case DEVELOP:
		case PRODUCT: {
			ItemConfig config = itemConfigCache.itemMap.get(itemId);
			data.setTotal(config.getUnitTotal());
			data.setVelocity(config.getUnitVelocity());
			addEffect(v, jobType, data);
			return data;
		}
		case NULL:
			break;
		default:
			break;

		}
		return data;
	}

	public JobTimeData getJobTime(JobType jobType, int vid, int itemId, long total) {
		Village v = villageInternal.getVillage(vid);
		JobTimeData data = v.getJobTimeData();
		data.setTotal(total);

		if (jobType == JobType.ASSART || jobType == JobType.CHOP || jobType == JobType.DIG) {
			JobConfig jobConfig = jobConfigCache.jobMap.get(jobType.getJobId());
			data.setVelocity(jobConfig.getUnitVelocity());
			addEffect(v, jobType, data);
		} else if (jobType == JobType.DEVELOP || jobType == JobType.PRODUCT) {
			ItemConfig config = itemConfigCache.itemMap.get(itemId);
			data.setVelocity(config.getUnitVelocity());
			addEffect(v, jobType, data);
		}

		return data;
	}

	private void addEffect(Village v, JobType jobType, JobTimeData data) {
		List<ThinkConfig> list = thinkConfigCache.jobThinkMap.get(jobType.getJobId());
		for (ThinkConfig config : list) {
			Item item = v.getEquips().get(config.getItemId());
			if (item != null) {
				int effect = item.getProto().getEffect();
				data.setVelocity(data.getVelocity() + effect);
			}
		}
	}

	public void changeJobRage(int vid, AbstractService service) {
		Village v = villageInternal.getVillage(vid);
		ScheduledFuture<Task> f = Data.villageFutures.get(vid);
		if (f == null)
			return;
		if (!f.cancel(false))
			return;

		JobTimeData data = v.getJobTimeData();
		long remainTime = f.getDelay(TimeUnit.SECONDS);
		long remainTotal = data.getVelocity() * remainTime;

		Data.villageFutures.put(vid, service.delay(data.getTask(), data.getDelayTime()));
	}

}
