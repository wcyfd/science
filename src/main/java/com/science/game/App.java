package com.science.game;

import java.io.IOException;

import com.science.game.cache.config.ConfigCache;
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
	public static GameWindows win = null;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		Service.regist(ItemServiceImpl.class, ItemService.class);
		Service.regist(VillageServiceImpl.class, VillageService.class);
		Service.regist(NatureServiceImpl.class, NatureService.class);
		Service.initServices();

		ConfigLoader.load("./config/item.csv", ConfigCache.item);
		ConfigLoader.load("./config/res.csv", ConfigCache.res);
		ConfigLoader.load("./config/consist.csv", ConfigCache.consist);
		System.out.println(ConfigCache.consist.consist);

		win = GameWindows.Builder.create().setHandler(new ScienceHandler()).setView(new ScienceView()).build()
				.startConsoleThread();

		GameWindows.Builder.create().setView(new CommandView()).build();

	}

}
