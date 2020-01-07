package com.science.game.service.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.service.AbstractService;
import com.science.game.service.job.module.AssartModule;
import com.science.game.service.job.module.CollectModule;
import com.science.game.service.job.module.DevelopModule;
import com.science.game.service.job.module.PreStartJobModule;
import com.science.game.service.job.module.ProductModule;
import com.science.game.service.job.module.StopModule;

@Service
public class JobServiceImpl extends AbstractService implements JobService, JobInternal {

	@Autowired
	private DevelopModule developModule;

	@Autowired
	private CollectModule collectModule;

	@Autowired
	private StopModule stopModule;

	@Autowired
	private AssartModule assartModule;

	@Autowired
	private PreStartJobModule preStartJobModule;

	@Autowired
	private ProductModule productModule;

	@Override
	protected void dispatch(String cmd, List<String> list) {
		switch (cmd) {
		case "assart":
			assart(getInt(list, 0));
			break;
		case "stop":
			stop(getInt(list, 0));
			break;
		case "collect":
			collect(getInt(list, 0), getInt(list, 1));
			break;
		case "develop":
			develop(getInt(list, 0), getInt(list, 1));
			break;
		case "product":
			product(getInt(list, 0), getInt(list, 1));
			break;
		}

	}

	@Override
	public void initCache() {
		Data.resPlace.put(1, Place.create(1));// 开荒地区
	}

	@Override
	public void assart(int vid) {
		assartModule.assart(vid, this);
	}

	@Override
	public void collect(int vid, int areaId) {
		collectModule.collect(vid, areaId, this);
	}

	@Override
	public void stop(int vid) {
		stopModule.stop(vid);
	}

	@Override
	public void develop(int vid, int itemId) {
		developModule.develop(vid, itemId, this);
	}

	@Override
	public void product(int vid, int itemId) {
		productModule.product(vid, itemId, this);
	}

	@Override
	public void preStartJob(int vid, PlaceType type, int id, JobType jobId) {
		preStartJobModule.preStartJob(vid, type, id, jobId);
	}

}
