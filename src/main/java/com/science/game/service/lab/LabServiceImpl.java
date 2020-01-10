package com.science.game.service.lab;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.village.DevelopData;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LabServiceImpl extends AbstractService implements LabService, LabInternal, IWork {
	@Autowired
	private PlaceInternal placeInternal;

	@Autowired
	private WorkInternal workInternal;

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private LabInternal labInternal;

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Override
	public void develop(int vid, int itemId) {

		if (isDeveloped(itemId)) {
			log.info("已经研发完成，不用再研发itemId={}", itemId);
			return;
		}

		Village v = villageInternal.getVillage(vid);

		placeInternal.enter(v, PlaceType.ITEM, itemId);

		DevelopData developData = v.getDevelopData();
		developData.setItemId(itemId);

		workInternal.beginWork(v.getWorkData(), JobType.DEVELOP, this);
	}

	/**
	 * 组件是否足够
	 * 
	 * @param list
	 * @return
	 */
	private boolean enoughConsistItem(List<ConsistConfig> list) {
		for (ConsistConfig config : list) {
			int needItemId = config.getNeedItemId();
			int count = config.getCount();

			if (itemInternal.getItemCount(needItemId) < count) {
				return false;
			}
		}

		return true;
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

		// 获得最大的熟练度，目前是遍历所有开发者，并获取最大的熟练度
		int maxPractice = 0;
		for (int developId : developIds) {
			Village village = Data.villages.get(developId);
			AtomicInteger value = village.getDevelopData().getPracticeMap().get(itemId);
			if (value == null) {
				continue;
			}

			if (value.get() > maxPractice) {
				maxPractice = value.get();
			}
		}

		// 检验研发点和熟练度是否大于指定研发点
		if (Data.developPoint.getOrDefault(itemId, 0) >= itemConfig.getDevelopPoint()
				&& maxPractice >= itemConfig.getPractice()) {
			Random rand = new Random();
			return rand.nextBoolean();
		}

		return false;

	}

	@Override
	public void workLoop(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		int itemId = v.getDevelopData().getItemId();
		if (!labInternal.isDeveloped(itemId)) {// 还没有研发成功的话就往下走
			// 研发结果
			if (developSuccess(itemId)) {
				// 成功
				successFunc(itemId);
			} else {
				// 失败
				failedFunc(itemId);
			}
		} else {
			workInternal.exitWork(v.getWorkData());
		}
	}

	private void successFunc(int itemId) {
		itemInternal.createItemIfAbsent(itemId);
		itemInternal.addItem(itemId, 1);
		Data.developVillages.getOrDefault(itemId, new LinkedList<>())
				.forEach(id -> workInternal.exitWork(villageInternal.getVillage(id).getWorkData()));// 停止所有参与研发的村民该工作
		Data.developVillages.remove(itemId);
		Data.developPoint.remove(itemId);
		Data.thinkList.remove((Integer) itemId);
	}

	private void failedFunc(int itemId) {
		List<ConsistConfig> list = consistConfigCache.consistMap.get(itemId);
		if (!enoughConsistItem(list)) {// 如果材料不够了就停止工作
			return;
		}

		// 扣除道具数量,先扣除再研究
		for (ConsistConfig config : list) {
			itemInternal.addItem(config.getNeedItemId(), -config.getCount());
		}
		int addPoint = 2;
		int skillValue = 4;
		// 研发点按照总值来算，熟练度按照村民来算
		Data.developPoint.putIfAbsent(itemId, 0);
		Data.developPoint.put(itemId, Data.developPoint.get(itemId) + addPoint);

		List<Integer> developerIds = Data.developVillages.get(itemId);
		for (int developerId : developerIds) {
			Village developer = Data.villages.get(developerId);

			// 添加熟练度
			developer.getDevelopData().getPracticeMap().putIfAbsent(itemId, new AtomicInteger());
			developer.getDevelopData().getPracticeMap().get(itemId).addAndGet(skillValue);
		}
	}

	@Override
	public void enterWork(WorkData workData) {
	}

	@Override
	public void exitWork(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		v.getDevelopData().setItemId(-1);
	}

	@Override
	public boolean isDeveloped(int itemId) {
		return Data.scienceMap.contains(itemId);
	}

	@Override
	public Set<Integer> getDevelopSuccessItem() {
		return Data.scienceMap;
	}

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "develop":
			develop(getInt(args, 0), getInt(args, 1));
			break;
		}
	}

	@Override
	public void addPractice(DevelopData developData, int itemId, int val) {
		developData.getPracticeMap().putIfAbsent(itemId, new AtomicInteger());
		developData.getPracticeMap().get(itemId).addAndGet(val);
	}
}
