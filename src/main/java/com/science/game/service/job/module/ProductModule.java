package com.science.game.service.job.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.service.AbstractService;

@Component
public class ProductModule {

	@Autowired
	private ConsistConfigCache consistConfigCache;

	/**
	 * 批量生产
	 * 
	 * @param vid
	 * @param itemId
	 * @param service
	 */
	public void product(int vid, int itemId, AbstractService service) {

		Map<Integer, Integer> transferCount = new HashMap<>(16);
		

		for (ConsistConfig config : consistConfigCache.consistMap.get(itemId)) {
			int needItemId = config.getNeedItemId();
			int needCount = config.getCount();

			Item needItem = Data.itemMap.get(needItemId);
			if (needItem == null || (needCount > 0 && needItem.getNum() == 0)) {
				return;
			}
			Reserve reserve = Reserve.builder().store(needItem.getNum()).delta(-needCount).build();
			if (!reserve.transfer()) {
				return;
			} else {
				transferCount.put(needItemId, reserve.getRemainCount());
			}
		}
	}

}
