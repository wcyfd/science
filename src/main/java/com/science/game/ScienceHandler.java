package com.science.game;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.science.game.cache.Data;
import com.science.game.service.Service;

import game.quick.window.GameHandler;
import game.quick.window.GameWindows;

public class ScienceHandler implements GameHandler {

	@Override
	public void execute(GameWindows arg0, String arg1) {
		Data.cmd = arg1;
		try {
			StringTokenizer token = new StringTokenizer(arg1);
			String cmd = token.nextToken(" ");
			List<String> args = new LinkedList<>();
			while (token.hasMoreTokens()) {
				String arg = token.nextToken();
				args.add(arg);
			}
			Service.dispatchCmd(cmd, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
