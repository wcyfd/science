package com.science.game.service.job.module;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;

import game.quick.window.Task;
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

		doProduct(vid, itemId, 3, service);
	}

	private void doProduct(int vid, int itemId, int delay, AbstractService service) {
		Data.villageFutures.put(vid, service.delay(new Task() {

			@Override
			public void afterExecute() {
				doProduct(vid, itemId, delay, service);
			}

			@Override
			public void execute() {
				Map<Integer, Integer> transferCount = new HashMap<>(16);

				for (ConsistConfig config : consistConfigCache.consistMap.get(itemId)) {
					int needItemId = config.getNeedItemId();
					int needCount = config.getCount();

					Item needItem = Data.itemMap.get(needItemId);
					if (needItem == null) {
						log.info("该道具不在Data.itemMap表中 {}", needItemId);
						return;
					}

					if (needCount > 0 && needItem.getNum() == 0) {
						log.info("该道具在合成表中不生效，目标要合成的道具是{},需要的道具是{}", needItemId, itemId);
						return;
					}

					Reserve reserve = Reserve.builder().store(needItem.getNum()).delta(-needCount).build();
					if (!reserve.transfer()) {
						log.info("合成{}的材料不足  {}=>当前数量{},需要数量{}", itemConfigCache.itemMap.get(itemId).getName(),
								itemConfigCache.itemMap.get(itemId).getName(), needItem.getNum(), needCount);
						return;
					} else {
						transferCount.put(needItemId, reserve.getRealDelta());
					}
				}

				transferCount.forEach((id, count) -> itemInternal.addItem(id, count));

				itemInternal.addItem(itemId, 1);
			}

		}, delay, TimeUnit.SECONDS));
	}

}
