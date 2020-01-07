package com.science.game.service.job.module;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ItemConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.job.JobInternal;
import com.science.game.service.job.JobService;

import game.quick.window.Task;

/**
 * 研发模块
 * 
 * @author aimfd
 *
 */
@Component
public class DevelopModule {

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Autowired
	private JobService jobService;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private JobInternal jobInternal;

	@Autowired
	private ItemConfigCache itemConfigCache;

	public void develop(int vid, int itemId, AbstractService service) {
		List<ConsistConfig> list = consistConfigCache.consistMap.get(itemId);

		// 没有足够的材料或者道具表中显示已经研发出了这个道具
		if (itemInternal.itemIsDeveloped(itemId) || !enoughMaterial(list))
			return;

		jobService.stop(vid);
		itemInternal.createItemPlace(itemId);
		jobInternal.preStartJob(vid, PlaceType.ITEM, itemId, JobType.DEVELOP);

		Data.developVillages.putIfAbsent(itemId, new LinkedList<>());
		List<Integer> developerIds = Data.developVillages.get(itemId);
		if (!developerIds.contains((Integer) vid)) {
			developerIds.add(vid);
		}

		doDevelop(vid, itemId, 2, service);
	}

	private void doDevelop(int vid, int itemId, int second, AbstractService service) {
		List<ConsistConfig> list = consistConfigCache.consistMap.get(itemId);
		if (!enoughMaterial(list)) {// 如果材料不够了就停止工作
			jobService.stop(vid);
			return;
		}

		// 扣除道具数量,先扣除再研究
		for (ConsistConfig config : list) {
			itemInternal.addItem(config.getNeedItemId(), -config.getCount());
		}

		Data.villageFutures.put(vid, service.delay(new Task() {

			@Override
			public void afterExecute() {
				if (itemInternal.itemIsDeveloped(itemId)) {
					jobService.stop(vid);
					return;
				}
				doDevelop(vid, itemId, second, service);
			}

			@Override
			public void execute() {

				if (!itemInternal.itemIsDeveloped(itemId)) {// 还没有研发成功的话就往下走
					// 研发结果
					if (developSuccess(itemId)) {
						// 成功
						itemInternal.createItemIfAbsent(itemId);
						itemInternal.addItem(itemId, 1);
						Data.developVillages.getOrDefault(itemId, new LinkedList<>())
								.forEach(id -> jobService.stop(id));// 停止所有参与研发的村民该工作
						Data.developVillages.remove(itemId);
						Data.developPoint.remove(itemId);
						Data.thinkList.remove((Integer) itemId);
					} else {
						// 失败
						int addPoint = 2;
						int skillValue = 4;
						// 研发点按照总值来算，熟练度按照村民来算
						Data.developPoint.putIfAbsent(itemId, 0);
						Data.developPoint.put(itemId, Data.developPoint.get(itemId) + addPoint);

						List<Integer> developerIds = Data.developVillages.get(itemId);
						for (int developerId : developerIds) {
							Village village = Data.villages.get(developerId);
							// 添加熟练度
							village.getSkillValues().putIfAbsent(itemId, new AtomicInteger());
							village.getSkillValues().get(itemId).addAndGet(skillValue);
						}
					}
				}

			}

		}, second, TimeUnit.SECONDS));
	}

	/**
	 * 判定是否研发成功
	 * 
	 * @param itemId
	 * @return
	 */
	private boolean developSuccess(int itemId) {
		ItemConfig itemConfig = itemConfigCache.itemMap.get(itemId);
		List<Integer> developIds = Data.developVillages.get(itemId);
		int maxPractice = 0;
		for (int developId : developIds) {
			Village village = Data.villages.get(developId);
			AtomicInteger value = village.getSkillValues().get(itemId);
			if (value == null) {
				continue;
			}

			if (value.get() > maxPractice) {
				maxPractice = value.get();
			}
		}

		if (Data.developPoint.getOrDefault(itemId, 0) >= itemConfig.getDevelopPoint()
				&& maxPractice >= itemConfig.getPractice()) {
			Random rand = new Random();
			return rand.nextBoolean();
		}

		return false;

	}

	/**
	 * 是否有足够的材料
	 * 
	 * @param list
	 * @return
	 */
	private boolean enoughMaterial(List<ConsistConfig> list) {
		for (ConsistConfig config : list) {
			int needItemId = config.getNeedItemId();
			int count = config.getCount();

			if (!itemInternal.itemIsDeveloped(needItemId) || Data.itemMap.get(needItemId).size() < count) {
				return false;
			}
		}

		return true;
	}
}
