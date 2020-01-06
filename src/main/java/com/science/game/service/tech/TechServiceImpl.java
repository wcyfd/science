package com.science.game.service.tech;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.service.AbstractService;
import com.science.game.service.tech.module.ThinkModule;

@Service
public class TechServiceImpl extends AbstractService implements TechService, TechInternal {

	@Autowired
	private ThinkModule thinkModule;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void think(int vid) {
		thinkModule.think(vid);
	}

}
