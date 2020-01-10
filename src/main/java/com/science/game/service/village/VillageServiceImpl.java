package com.science.game.service.village;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ThinkConfigCache;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ThinkConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.lab.LabInternal;
import com.science.game.service.village.module.CreateVillageModule;

@Service
public class VillageServiceImpl extends AbstractService implements VillageService, VillageInternal {

	@Autowired
	private CreateVillageModule createVillageModule;

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private LabInternal labInternal;

	@Autowired
	private ThinkConfigCache thinkConfigCache;

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Override
	public void initCache() {
	}

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "recruite":
			recruite();
			break;
		}
	}

	@Override
	public void recruite() {
		createVillageModule.createVillage();
	}

	@Override
	public Village getVillage(int vid) {
		return Data.villages.get(vid);
	}

	@Override
	public void think(int vid) {
		Village village = villageInternal.getVillage(vid);

		int jobId = village.getWorkData().getJobType().getJobId();
		List<ThinkConfig> list = thinkConfigCache.jobThinkMap.get(jobId);
		// 检查当前职业
		List<Integer> targets = new ArrayList<>(list.size());
		for (ThinkConfig config : list) {
			int itemId = config.getItemId();

			if (Data.thinkList.contains((Integer) itemId)) {
				continue;
			}

			// 检查目前解锁资源是否允许出现这个想法
			List<ConsistConfig> consistConfigList = consistConfigCache.consistMap.get(itemId);

			boolean allDevelop = checkAllDevelop(consistConfigList);
			if (allDevelop) {
				targets.add(itemId);
			}

		}

		targets.removeAll(labInternal.getDevelopSuccessItem());

		if (targets.size() != 0) {
			int itemId = targets.get(new Random().nextInt(targets.size()));
			Data.thinkList.add(itemId);
		}

	}

	/**
	 * 检查所有组件都已经被研发
	 * 
	 * @param consistConfigList
	 * @return
	 */
	private boolean checkAllDevelop(List<ConsistConfig> consistConfigList) {
		for (ConsistConfig consistConfig : consistConfigList) {
			int needItemId = consistConfig.getNeedItemId();
			if (!labInternal.isDeveloped(needItemId)) {
				// 有资源没有获取到过直接跳过
				return false;
			}
		}

		return true;
	}
}
