package com.science.game;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.service.AbstractService;

import game.quick.window.GameHandler;
import game.quick.window.GameWindows;

@Component
public class ScienceHandler implements GameHandler {

	@Override
	public void execute(GameWindows arg0, String arg1) {
		Data.cmd = arg1;
		AbstractService.dispatchCmd(arg1);
	}

}
