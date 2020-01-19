package com.science.game.service.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.I;
import com.science.game.cache.config.ConsistConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.ConsistConfig;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.village.ProductData;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.lab.LabInternal;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl extends AbstractService implements ProductService, IWork {

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private PlaceInternal placeInternal;

	@Autowired
	private WorkInternal workInternal;

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private ConsistConfigCache consistConfigCache;

	@Autowired
	private LabInternal labInternal;

	@Autowired
	private ItemInternal itemInternal;

	@Override
	public void product(int vid, int itemId) {
		Village v = villageInternal.getVillage(vid);

		placeInternal.enter(v, PlaceType.ITEM, itemId);

		v.getProductData().setItemId(itemId);

		workInternal.beginWork(v.getWorkData(), JobType.PRODUCT, this);
	}

	@Override
	public void enterWork(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		ItemConfig config = itemConfigCache.itemMap.get(v.getProductData().getItemId());

		workData.setTotal(config.getUnitTotal());
		v.getProductData().setNeedCostItem(true);
	}

	@Override
	public void exitWork(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		ProductData productData = v.getProductData();
		productData.setNeedCostItem(false);

		placeInternal.exit(v);
	}

	@Override
	public void workLoop(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		ProductData productData = v.getProductData();
		int itemId = productData.getItemId();
		if (productData.isNeedCostItem()) {// 是否要消耗材料
			Map<Integer, Integer> transferCount = new HashMap<>(16);

			for (ConsistConfig config : consistConfigCache.consistMap.get(productData.getItemId())) {
				int needItemId = config.getNeedItemId();
				int needCount = config.getCount();

				if (!labInternal.isDeveloped(itemId)) {
					log.info("该道具没有研发过 itemId={}", itemId);
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
							itemConfigCache.itemMap.get(needItemId).getName(), currentCount, needCount);
					return;
				} else {
					transferCount.put(needItemId, reserve.getRealDelta());
				}
			}

			transferCount.forEach((id, count) -> itemInternal.addItem(id, count));
			productData.setNeedCostItem(false);
		}

		if (workData.getCurrent().get() < workData.getTotal()) {

			ItemConfig itemConfig = itemConfigCache.itemMap.get(itemId);
			int velocity = itemConfig.getUnitVelocity();

			workInternal.addWorkProgress(workData, velocity);
			if (workData.getCurrent().get() == workData.getTotal()) {
				itemInternal.addItem(itemId, 1);

				// 增加熟练度
				labInternal.addPractice(v.getDevelopData(), itemId, 2);
				productData.setNeedCostItem(true);
			}
		} else {
			workInternal.resetProgress(workData);
		}

		villageInternal.think(v.getId());
	}

	@Override
	protected void dispatch(String cmd, I i) {
		switch (cmd) {
		case "product":
			product(i.i(),i.i());
			break;
		}
	}

}
