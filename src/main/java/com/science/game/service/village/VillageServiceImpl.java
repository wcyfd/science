package com.science.game.service.village;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.config.ThinkConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ItemConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.lab.LabInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VillageServiceImpl extends AbstractService implements VillageService, VillageInternal {

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private LabInternal labInternal;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private ThinkConfigCache thinkConfigCache;

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private DataCenter dataCenter;

	@Override
	public void initCache() {
	}

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "recruite":
			recruite();
			break;
		}
	}

	@Override
	public void recruite() {
		Village v = Village.create();
		Scene scene = dataCenter.getScene();
		scene.getVillageData().getVillages().put(v.getId(), v);
	}

	@Override
	public Village getVillage(int vid) {
		Scene scene = dataCenter.getScene();
		return scene.getVillageData().getByOnlyId(vid);
	}

	@Override
	public void think(int vid) {

		Village village = villageInternal.getVillage(vid);

		int jobId = village.getWorkData().getJobType().getJobId();

		if (!thinkConfigCache.jobThinkMap.containsKey(jobId))
			return;

		// 检查当前职业,过滤出还没有开发成功的道具
		List<Integer> targets = thinkConfigCache.jobThinkMap.get(jobId)// 获取该职业的所有可想到的道具
				.stream()//
				.filter(config -> !labInternal.isOldIdea(config.getItemId()))// 过滤旧想法
				.filter(config -> conditionAllowed(config.getItemId()))// 检查资源是否允许,检查目前解锁资源是否允许出现这个想法
				.map(config -> config.getItemId())// 只获取id
				.collect(Collectors.toList());

		if (targets.size() != 0) {
			// TODO 这里之后可以调整一下思考出的几率
			int itemId = targets.get(new Random().nextInt(targets.size()));
			labInternal.addNewIdea(itemId);
		}

	}

	/**
	 * 检查所有组件都已经被研发
	 * 
	 * @param consistConfigList
	 * @return
	 */
	private boolean conditionAllowed(int itemId) {
		List<ConsistConfig> consistConfigList = consistConfigCache.consistMap.get(itemId);
		for (ConsistConfig consistConfig : consistConfigList) {
			int needItemId = consistConfig.getNeedItemId();
			ItemConfig itemConfig = itemConfigCache.itemMap.get(needItemId);
			switch (itemConfig.getType()) {
			case ITEM:
				if (!labInternal.isDeveloped(needItemId)) {
					// 有资源没有获取到过直接跳过
					return false;
				}
				break;
			case RES:
				if (!itemInternal.hasItemRecord(needItemId)) {
					return false;
				}
				break;
			default:
				log.error("没有道具类型{}", needItemId);
				return false;

			}

		}

		return true;
	}
}
