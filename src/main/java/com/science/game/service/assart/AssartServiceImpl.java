package com.science.game.service.assart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.config.JobConfig;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;

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
	private JobConfigCache jobConfigCache;

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "assart":
			assart(getInt(args, 0));
			break;
		}
	}

	@Override
	public void assart(int vid) {
		if (Data.areaId >= Data.areaList.size()) {
			log.info("荒地全部开发完了");
			return;
		}

		Village v = villageInternal.getVillage(vid);

		placeInternal.enter(v, PlaceType.PLACE, -2);

		workInternal.beginWork(v.getWorkData(), JobType.ASSART, this);
	}

	@Override
	public void workLoop(WorkData workData) {
		if (workData.getCurrent().get() < workData.getTotal()) {
			int delta = 1;
			workInternal.addWorkProgress(workData, delta);

			if (workInternal.isWorkComplete(workData)) {
				Data.areaId++;
				Data.resPlace.putIfAbsent(Data.areaId, Place.create(Data.areaId));// 创建一个资源点位置
			}

		} else {
			workInternal.resetProgress(workData);

			if (Data.areaId >= Data.areaList.size())
				workInternal.exitWork(workData);
		}
	}

	@Override
	public void enterWork(WorkData workData) {
		JobConfig config = jobConfigCache.jobMap.get(workData.getJobType().getJobId());
		workData.setTotal(config.getUnitTotal());
	}

	@Override
	public void exitWork(WorkData workData) {

	}

}
