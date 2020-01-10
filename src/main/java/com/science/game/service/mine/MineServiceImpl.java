package com.science.game.service.mine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.entity.JobType;
import com.science.game.service.AbstractService;
import com.science.game.service.collect.CollectInternal;

@Service
public class MineServiceImpl extends AbstractService implements MineService {

	@Autowired
	private CollectInternal collectInternal;

	@Override
	public void dig(int vid, int placeId) {
		collectInternal.collect(vid, JobType.DIG, placeId);
	}

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "dig":
			dig(getInt(args, 0), getInt(args, 1));
			break;
		}
	}

}
