package com.science.game.service.job.module;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Place.Type;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
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

	public void develop(int vid, int itemId, AbstractService service) {
		List<ConsistConfig> list = consistConfigCache.consistMap.get(itemId);

		if (!enoughMaterial(list) || Data.itemMap.containsKey(itemId))
			return;

		jobService.stop(vid);

		Village v = Data.villages.get(vid);

		v.setPlaceType(Type.ITEM);
		v.setPlaceId(itemId);
		v.setJobId(4);

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
				if (Data.itemMap.containsKey(itemId)) {
					jobService.stop(vid);
					return;
				}
				doDevelop(vid, itemId, second, service);
			}

			@Override
			public void execute() {

				if (!Data.itemMap.containsKey(itemId)) {// 还没有研发成功的话就往下走
					// 研发结果
					if (developSuccess(itemId)) {
						// 成功
						Item item = itemInternal.createItemIfAbsent(itemId);
						item.setNum(1);
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
							village.getSkillValues().putIfAbsent(itemId, 0);
							village.getSkillValues().put(itemId, village.getSkillValues().get(itemId) + skillValue);
						}
					}
				}

			}

		}, second, TimeUnit.SECONDS));
	}

	private boolean developSuccess(int itemId) {
		Random rand = new Random();
		return rand.nextBoolean();
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	private boolean enoughMaterial(List<ConsistConfig> list) {
		for (ConsistConfig config : list) {
			int needItemId = config.getNeedItemId();
			int count = config.getCount();

			Item item = Data.itemMap.get(needItemId);
			if (item == null || item.getNum() < count) {
				return false;
			}
		}

		return true;
	}
}
