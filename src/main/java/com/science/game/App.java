package com.science.game;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.science.game.service.Service;
import com.science.game.service.item.ItemService;
import com.science.game.service.item.ItemServiceImpl;
import com.science.game.service.nature.NatureService;
import com.science.game.service.nature.NatureServiceImpl;
import com.science.game.service.village.VillageService;
import com.science.game.service.village.VillageServiceImpl;

import game.quick.window.GameHandler;
import game.quick.window.GameWindows;
import game.quick.window.IView;

/**
 * Hello world!
 *
 */
public class App {
	private static GameWindows win = new GameWindows();

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		System.out.println("Hello World!");
		Service.regist(ItemServiceImpl.class, ItemService.class);
		Service.regist(VillageServiceImpl.class, VillageService.class);
		Service.regist(NatureServiceImpl.class, NatureService.class);
		Service.initServices();

		win = new GameWindows();
		win.setHandler(new GameHandler() {

			@Override
			public void execute(GameWindows arg0, String arg1) {
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
				}
			}
		});
		win.setView(new ScienceView());
		win.start();
		win.startConsoleThread();
		GameWindows cmdWin = new GameWindows();
		cmdWin.setView(new IView() {

			private StringBuilder sb = new StringBuilder();

			@Override
			public String render() {
				command();
				return sb.toString();
			}

			private void command() {
				sb.append("Command").append("\n");
				List<Class<?>> list = Service.getServices();

				for (Class<?> clazz : list) {
					String name = clazz.getSimpleName().toLowerCase().replace("service", "");
					Method[] methods = clazz.getMethods();
					sb.append(name).append("\t");
					for (Method method : methods) {
						sb.append(method.getName().toLowerCase()).append("  ");
					}
					sb.append("\n");
				}
			}
		});
		cmdWin.start();
	}

	public static void render() {
		win.render();
	}

}
