package com.science.game;

import org.springframework.stereotype.Component;

import com.science.game.entity.Scene;
import com.science.game.service.AbstractService;

import game.quick.window.GameHandler;
import game.quick.window.GameWindows;

@Component
public class ScienceHandler implements GameHandler {

	private static String cmd = null;

	public static String getCmd() {
		return cmd;
	}

	@Override
	public void execute(GameWindows arg0, String arg1) {
		cmd = arg1;
		AbstractService.dispatchCmd(arg1);
	}

}
