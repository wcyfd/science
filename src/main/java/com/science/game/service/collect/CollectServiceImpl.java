package com.science.game.service.collect;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.config.JobConfigCache;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.entity.config.PlaceConfig;
import com.science.game.entity.village.PlaceData;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;

@Service
public class CollectServiceImpl extends AbstractService implements CollectInternal, IWork {
	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private PlaceInternal placeInternal;

	@Autowired
	private WorkInternal workInternal;

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private PlaceConfigCache placeConfigCache;

	@Autowired
	private JobConfigCache jobConfigCache;

	@Autowired
	private DataCenter dataCenter;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void collect(int vid, JobType jobType, int placeId) {
		Village v = villageInternal.getVillage(vid);

		placeInternal.enter(v, PlaceType.PLACE, placeId);

		workInternal.beginWork(v.getWorkData(), jobType, this);
	}

	@Override
	public void workLoop(WorkData workData) {
		Scene scene = dataCenter.getScene();
		Village v = villageInternal.getVillage(workData.getVid());
		PlaceData placeData = v.getPlaceData();
		int resId = scene.getPlaceData().getAreaList().get(placeData.getPlace().getPlaceId());

		PlaceConfig placeConfig = placeConfigCache.placeMap.get(resId);
		int velocity = jobConfigCache.jobMap.get(workData.getJobType().getJobId()).getUnitVelocity();
		workData.getCurrent().addAndGet(velocity);
		if (workData.getCurrent().get() >= workData.getTotal()) {
			itemInternal.createResItemSpace(placeConfig.getItemId());
			itemInternal.addItem(placeConfig.getItemId(), workData.getCurrent().get() / workData.getTotal());
		}
		workInternal.setProgress(workData, workData.getCurrent().get() % workData.getTotal());

		villageInternal.think(v.getId());
	}

	@Override
	public void enterWork(WorkData workData) {
		workData.setTotal(jobConfigCache.jobMap.get(workData.getJobType().getJobId()).getUnitTotal());
	}

	@Override
	public void exitWork(WorkData workData) {
		workData.setTotal(0);
	}

}
