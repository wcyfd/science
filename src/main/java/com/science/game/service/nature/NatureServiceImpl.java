package com.science.game.service.nature;

import java.util.List;

import com.science.game.service.Service;
import com.science.game.service.job.AssartJob;

public class NatureServiceImpl extends Service implements NatureService {

	private AssartJob assartJob = new AssartJob();

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "assart":
			assart(Integer.valueOf(args.get(0)));
			break;
		}
	}

	@Override
	public void assart(int id) {
		assartJob.workForever(id);
	}

}
