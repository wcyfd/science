package com.science.game;

import com.science.game.service.Service;
import com.science.game.service.item.ItemService;
import com.science.game.service.item.ItemServiceImpl;
import com.science.game.service.nature.NatureService;
import com.science.game.service.nature.NatureServiceImpl;
import com.science.game.service.village.VillageService;
import com.science.game.service.village.VillageServiceImpl;

import game.quick.window.GameWindows;

/**
 * Hello world!
 *
 */
public class App {
	public static GameWindows win = new GameWindows();

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		Service.regist(ItemServiceImpl.class, ItemService.class);
		Service.regist(VillageServiceImpl.class, VillageService.class);
		Service.regist(NatureServiceImpl.class, NatureService.class);
		Service.initServices();

		win = new GameWindows();
		win.setHandler(new ScienceHandler());
		win.setView(new ScienceView());

		win.start();
		win.startConsoleThread();

		GameWindows cmdWin = new GameWindows();
		cmdWin.setView(new CommandView());
		cmdWin.start();
	}

	public static void render() {
		win.render();
	}

}
