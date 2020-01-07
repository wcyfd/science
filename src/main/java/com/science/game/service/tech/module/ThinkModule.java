package com.science.game.service.tech.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ThinkConfigCache;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ThinkConfig;
import com.science.game.service.item.ItemInternal;

@Component
public class ThinkModule {

	@Autowired
	private ThinkConfigCache thinkConfigCache;

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Autowired
	private ItemInternal itemInternal;

	/**s
	 * 思考
	 * 
	 * @param vid
	 */
	public void think(int vid) {
		Village village = Data.villages.get(vid);
		int jobId = village.getJobId();
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

		targets.removeAll(Data.itemMap.keySet());

		if (targets.size() != 0) {
			int itemId = targets.get(new Random().nextInt(targets.size()));
			Data.thinkList.add(itemId);
		}

	}

	/**
	 * 检查所有组件都已经被研发
	 * @param consistConfigList
	 * @return
	 */
	private boolean checkAllDevelop(List<ConsistConfig> consistConfigList) {
		for (ConsistConfig consistConfig : consistConfigList) {
			int needItemId = consistConfig.getNeedItemId();
			if (!itemInternal.itemIsDeveloped(needItemId)) {
				// 有资源没有获取到过直接跳过
				return false;
			}
		}

		return true;
	}
}
