package com.science.game.service.forest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "chop":
			chop(getInt(args, 0), getInt(args, 1));
			break;
		}
	}
}
