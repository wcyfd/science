package com.science.game.service.assart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.entity.config.JobConfig;
import com.science.game.entity.scene.PlaceData;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;
import com.science.game.service.work.WorkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssartServiceImpl extends AbstractService implements AssartService, IWork {

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private PlaceInternal placeInternal;

	@Autowired
	private WorkInternal workInternal;

	@Autowired
	private WorkService workService;

	@Autowired
	private JobConfigCache jobConfigCache;

	@Autowired
	private DataCenter dataCenter;

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "assart":
			assart(i.i());
			break;
		}
	}

	@Override
	public void assart(int vid) {
		if (placeInternal.isMaxPlace()) {
			log.info("荒地全部开发完了");
			return;
		}

		Village v = villageInternal.getVillage(vid);

		workInternal.exitWork(v.getWorkData());

		placeInternal.enter(v, PlaceType.PLACE, -2);
		workInternal.beginWork(v.getWorkData(), JobType.ASSART, this);
	}

	@Override
	public void workLoop(WorkData workData) {
		Scene scene = dataCenter.getScene();
		if (workData.getCurrent().get() < workData.getTotal()) {
			int delta = 1;
			workInternal.addWorkProgress(workData, delta);

			if (workInternal.isWorkComplete(workData)) {
				PlaceData placeData = scene.getPlaceData();
				placeData.getIncreAreaId().incrementAndGet();
				placeInternal.createIfAbsent(PlaceType.PLACE, placeData.getAreaId());// 创建一个资源点位置
			}

		} else {
			workInternal.resetProgress(workData);

			if (placeInternal.isMaxPlace())
				workService.stop(workData.getVid());
		}

		villageInternal.think(workData.getVid());
	}

	@Override
	public void enterWork(WorkData workData) {

		JobConfig config = jobConfigCache.jobMap.get(workData.getJobType().getJobId());
		workData.setTotal(config.getUnitTotal());
	}

	@Override
	public void exitWork(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		placeInternal.exit(v);
	}

}
