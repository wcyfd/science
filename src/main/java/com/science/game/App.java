package com.science.game;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import game.quick.window.GameWindows;

/**
 * Hello world!
 *
 */
public class App {
	public static GameWindows win = null;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//		Service.regist(ItemServiceImpl.class, ItemService.class);
//		Service.regist(VillageServiceImpl.class, VillageService.class);
//		Service.regist(NatureServiceImpl.class, NatureService.class);
//		Service.initServices();
//
//		win = GameWindows.Builder.create().setHandler(new ScienceHandler()).setView(new ScienceView()).build()
//				.startConsoleThread();
//
//		GameWindows.Builder.create().setView(new CommandView()).build();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		
	}

}
