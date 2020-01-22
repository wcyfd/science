package com.science.game.service.lab;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.scene.LabData;
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

	@Autowired
	private DataCenter dataCenter;

	@Override
	public void develop(int vid, int itemId) {

		if (isDeveloped(itemId)) {
			log.info("已经研发完成，不用再研发itemId={}", itemId);
			return;
		}

		Village v = villageInternal.getVillage(vid);

		workInternal.exitWork(v.getWorkData());

		DevelopData developData = v.getDevelopData();
		developData.setItemId(itemId);
		
		placeInternal.createIfAbsent(PlaceType.DEVELOP, itemId);
		placeInternal.enter(v, PlaceType.DEVELOP, itemId);
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
	private boolean isDevelopSuccess(int itemId) {
		ItemConfig itemConfig = itemConfigCache.itemMap.get(itemId);
		Scene scene = dataCenter.getScene();
		LabData labData = scene.getLabData();
		Set<Integer> developIds = placeInternal.getPlace(PlaceType.DEVELOP, itemId).getVillageIds();

		// 获得最大的熟练度，目前是遍历所有开发者，并获取最大的熟练度
		int maxPractice = 0;
		for (int developId : developIds) {
			Village village = scene.getVillageData().getByOnlyId(developId);
			AtomicInteger value = village.getDevelopData().getPracticeMap().get(itemId);
			if (value == null) {
				continue;
			}

			if (value.get() > maxPractice) {
				maxPractice = value.get();
			}
		}

		// 检验研发点和熟练度是否大于指定研发点
		if (labData.getDevelopPoint().getOrDefault(itemId, new AtomicInteger(0)).get() >= itemConfig.getDevelopPoint()
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
			if (isDevelopSuccess(itemId)) {
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

	/**
	 * 开发成功
	 * 
	 * @param itemId
	 */
	private void successFunc(int itemId) {
		itemInternal.createEquipItemSpace(itemId);
		placeInternal.createIfAbsent(PlaceType.ITEM, itemId);// 资源型item不需要创建道具位
		itemInternal.addItem(itemId, 1);
		Scene scene = dataCenter.getScene();
		LabData labData = scene.getLabData();

		// 停止所有参与研发的村民该工作
		log.info("研发成功,停止所有参与研发的村民该工作 itemId={} vid={}", itemId, labData.getTeamMap().get(itemId));
		placeInternal.getPlace(PlaceType.DEVELOP, itemId).getVillageIds()
				.forEach(vid -> workInternal.exitWork(villageInternal.getVillage(vid).getWorkData()));

		labData.getDevelopPoint().remove(itemId);// 清空研发点
		labData.getIdeaList().remove((Integer) itemId);// 从想法中移除
		labData.getSciences().add(itemId);// 添加科技信息

		// 清空所有人
		new HashSet<>(placeInternal.getPlace(PlaceType.DEVELOP, itemId).getVillageIds()).stream()
				.forEach(vid -> placeInternal.exit(villageInternal.getVillage(vid)));
		labData.getTryCountMap().remove(itemId);
		log.info("删除开发团队 itemId={}", itemId);
		labData.getTeamMap().remove(itemId);// 删除开发团队
		// 清空场地
		placeInternal.deletePlace(PlaceType.DEVELOP, itemId);
	}

	/**
	 * 开发失败
	 * 
	 * @param itemId
	 */
	private void failedFunc(int itemId) {

		List<ConsistConfig> list = consistConfigCache.consistMap.get(itemId);
		if (!enoughConsistItem(list)) {// 如果材料不够了就停止工作
			log.info("材料不足研发等待 itemId={}", itemId);
			return;
		}
		Scene scene = dataCenter.getScene();
		LabData labData = scene.getLabData();

		// 扣除道具数量,先扣除再研究
		for (ConsistConfig config : list) {
			itemInternal.addItem(config.getNeedItemId(), -config.getCount());
		}
		int addPoint = 2;
		int skillValue = 4;
		// 研发点按照总值来算，熟练度按照村民来算
		if (!labData.getDevelopPoint().containsKey(itemId)) {
			labData.getDevelopPoint().put(itemId, new AtomicInteger());
		}
		labData.getDevelopPoint().get(itemId).addAndGet(addPoint);

		Set<Integer> developerIds = placeInternal.getPlace(PlaceType.DEVELOP, itemId).getVillageIds();
		for (int developerId : developerIds) {
			Village developer = scene.getVillageData().getByOnlyId(developerId);

			// 添加熟练度
			developer.getDevelopData().getPracticeMap().putIfAbsent(itemId, new AtomicInteger());
			developer.getDevelopData().getPracticeMap().get(itemId).addAndGet(skillValue);
		}
		log.info("研发失败，次数加1 itemId={}", itemId);

		labData.getTryCountMap().get(itemId).incrementAndGet();
	}

	@Override
	public void enterWork(WorkData workData) {
		int vid = workData.getVid();
		Village v = villageInternal.getVillage(vid);
		int itemId = v.getDevelopData().getItemId();

		LabData labData = dataCenter.getScene().getLabData();

		labData.getTryCountMap().putIfAbsent(itemId, new AtomicInteger());

		// 加入团队

		if (!labData.getTeamMap().containsKey(itemId)) {
			labData.getTeamMap().putIfAbsent(itemId, new HashSet<>());
		}
		labData.getTeamMap().get(itemId).add(vid);
		log.info("加入研发团队 vid={} itemId={}", vid, itemId);
	}

	@Override
	public void exitWork(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());

		DevelopData developData = v.getDevelopData();
		int itemId = developData.getItemId();
		dataCenter.getScene().getLabData().getTeamMap().get(itemId).remove(v.getId());
		developData.setItemId(0);

		log.info("退出研发团队 itemId={} vid={}", itemId, v.getId());

		placeInternal.exit(v);
	}

	@Override
	public boolean isDeveloped(int itemId) {
		return dataCenter.getScene().getLabData().getSciences().contains(itemId);
	}

	@Override
	public Set<Integer> getDevelopSuccessItem() {
		return dataCenter.getScene().getLabData().getSciences();
	}

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "develop":
			develop(i.i(), i.i());
			break;
		}
	}

	@Override
	public void addPractice(DevelopData developData, int itemId, int val) {
		developData.getPracticeMap().putIfAbsent(itemId, new AtomicInteger());
		developData.getPracticeMap().get(itemId).addAndGet(val);
	}

	@Override
	public void addNewIdea(int id) {
		log.info("加入想法 itemId={}", id);
		Scene scene = dataCenter.getScene();
		LabData labData = scene.getLabData();
		if (!isOldIdea(id)) {
			labData.getIdeaList().add(id);
		}
	}

	@Override
	public boolean isOldIdea(int id) {
		Scene scene = dataCenter.getScene();
		return isDeveloped(id) || scene.getLabData().getIdeaList().contains((Integer) id);
	}

	@Override
	public List<Integer> getIdeaList() {
		Scene scene = dataCenter.getScene();
		return scene.getLabData().getIdeaList();
	}

	@Override
	public int getTryCount(int itemId) {
		return dataCenter.getScene().getLabData().getTryCountMap().getOrDefault(itemId, new AtomicInteger(0)).get();
	}
}
