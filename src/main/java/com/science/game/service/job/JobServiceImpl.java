package com.science.game.service.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Place;
import com.science.game.service.AbstractService;
import com.science.game.service.job.module.AssartModule;
import com.science.game.service.job.module.CollectModule;
import com.science.game.service.job.module.DevelopModule;
import com.science.game.service.job.module.StopModule;

@Service
public class JobServiceImpl extends AbstractService implements JobService {

	@Autowired
	private DevelopModule developModule;

	@Autowired
	private CollectModule collectModule;

	@Autowired
	private StopModule stopModule;

	@Autowired
	private AssartModule assartModule;

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "assart":
			assart(Integer.valueOf(args.get(0)));
			break;
		case "stop":
			stop(Integer.valueOf(args.get(0)));
			break;
		case "collect":
			collect(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
			break;
		case "develop":
			develop(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
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
	public void product() {

	}

}
