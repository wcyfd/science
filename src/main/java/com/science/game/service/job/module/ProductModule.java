package com.science.game.service.job.module;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ItemConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;
import com.science.game.service.village.VillageInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductModule {

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private JobService jobService;

	@Autowired
	private JobInternal jobInternal;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private VillageInternal villageInternal;

	/**
	 * 批量生产
	 * 
	 * @param vid
	 * @param itemId
	 * @param service
	 */
	public void product(int vid, int itemId, AbstractService service) {

		jobService.stop(vid);
		jobInternal.preStartJob(vid, PlaceType.ITEM, itemId, JobType.PRODUCT);

		new ProductJob(itemId, villageInternal.getVillage(vid), service).start();
	}

	class ProductJob extends JobTask {
		private int itemId;
		private boolean cost;

		public ProductJob(int itemId, Village v, AbstractService service) {
			super(v, service);

			this.itemId = itemId;
			this.cost = true;
		}

		@Override
		public void work(Village village) {

			if (cost) {// 是否要消耗材料
				Map<Integer, Integer> transferCount = new HashMap<>(16);

				for (ConsistConfig config : consistConfigCache.consistMap.get(itemId)) {
					int needItemId = config.getNeedItemId();
					int needCount = config.getCount();

					if (!itemInternal.itemIsDeveloped(needItemId)) {
						log.info("该道具不在Data.itemMap表中 {}", needItemId);
						return;
					}

					if (needCount == 0) {
						log.info("该道具在合成表中不生效，目标要合成的道具是{},需要的道具是{}", needItemId, itemId);
						return;
					}

					int currentCount = itemInternal.getItemCount(needItemId);
					Reserve reserve = Reserve.builder().store(currentCount).delta(-needCount).build();
					if (!reserve.transfer()) {
						log.info("合成{}的材料不足  {} =>当前数量{},需要数量{}", itemConfigCache.itemMap.get(itemId).getName(),
								itemConfigCache.itemMap.get(itemId).getName(), currentCount, needCount);
						return;
					} else {
						transferCount.put(needItemId, reserve.getRealDelta());
					}
				}

				transferCount.forEach((id, count) -> itemInternal.addItem(id, count));
				cost = false;
			}

			JobData jobData = village.getJobData();
			int vid = village.getId();
			if (jobData.getCurrent().get() < jobData.getTotal()) {

				ItemConfig itemConfig = itemConfigCache.itemMap.get(itemId);
				int velocity = itemConfig.getUnitVelocity();
				velocity += jobInternal.getEffectByJobType(jobData.getJobType(), village);

				jobInternal.addJobProgress(jobData, velocity);
				if (jobData.getCurrent().get() == jobData.getTotal()) {
					itemInternal.addItem(itemId, 1);

					// 增加熟练度
					Data.villages.get(vid).getSkillValues().get(itemId).addAndGet(2);
					cost = true;
				}
			} else {
				resetProgress();
			}
		}

		@Override
		protected void initJobProgress(Village v) {
			JobData jobData = v.getJobData();
			ItemConfig itemConfig = itemConfigCache.itemMap.get(itemId);

			jobData.setTotal(itemConfig.getUnitTotal());
			jobData.getCurrent().set(0);
		}

	}
}
