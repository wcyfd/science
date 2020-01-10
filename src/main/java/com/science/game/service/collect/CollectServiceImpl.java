package com.science.game.service.collect;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.config.JobConfigCache;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
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
		Village v = villageInternal.getVillage(workData.getVid());
		PlaceData placeData = v.getPlaceData();

		PlaceConfig placeConfig = placeConfigCache.placeMap.get(placeData.getPlace().getPlaceId());
		if (workData.getCurrent().get() >= workData.getTotal()) {
			itemInternal.createItemIfAbsent(placeConfig.getItemId());
			itemInternal.addItem(placeConfig.getItemId(), workData.getCurrent().get() / workData.getTotal());
		}
		workData.getCurrent().set(workData.getCurrent().get() % workData.getTotal());
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
