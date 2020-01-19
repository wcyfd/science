package com.science.game.service.forest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.I;
import com.science.game.entity.JobType;
import com.science.game.service.AbstractService;
import com.science.game.service.collect.CollectInternal;

@Service
public class ForestServiceImpl extends AbstractService implements ForestService {

	@Autowired
	private CollectInternal collectInternal;

	@Override
	public void chop(int vid, int placeId) {
		collectInternal.collect(vid, JobType.CHOP, placeId);
	}

	@Override
	protected void dispatch(String cmd, I i) {
		switch (cmd) {
		case "chop":
			chop(i.i(),i.i());
			break;
		}
	}
}
