package com.science.game;

import java.io.IOException;

import com.science.game.cache.config.ConfigCache;
import com.science.game.service.Service;
import com.science.game.service.item.ItemService;
import com.science.game.service.item.ItemServiceImpl;
import com.science.game.service.job.JobService;
import com.science.game.service.job.JobServiceImpl;
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

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		ConfigLoader.load("./config/item.csv", ConfigCache.item);
		ConfigLoader.load("./config/res.csv", ConfigCache.res);
		ConfigLoader.load("./config/consist.csv", ConfigCache.consist);

		Service.regist(ItemServiceImpl.class, ItemService.class);
		Service.regist(VillageServiceImpl.class, VillageService.class);
		Service.regist(NatureServiceImpl.class, NatureService.class);
		Service.regist(JobServiceImpl.class, JobService.class);
		Service.initServiceCache();

		GameWindows win = GameWindows.Builder.create().setHandler(new ScienceHandler()).setView(new ScienceView())
				.build().startConsoleThread();

		Service.initService(win);

		GameWindows.Builder.create().setView(new CommandView()).build();

	}

}
