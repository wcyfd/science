package com.science.game.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

import game.quick.window.GameWindows;
import game.quick.window.Task;

public abstract class AbstractService implements InitializingBean {
	private final static Map<String, AbstractService> HANDLE = new HashMap<>();
	private GameWindows gameWindows;

	public void setGameWindows(GameWindows win) {
		this.gameWindows = win;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Class<?>[] clazzes = this.getClass().getInterfaces();
		for (Class<?> clazz : clazzes) {
			if (ServiceInterface.class.isAssignableFrom(clazz)) {
				Method[] methods = clazz.getMethods();

				String name = clazz.getSimpleName().toLowerCase().replace("service", "");

				for (Method method : methods) {
					String methodName = method.getName().toLowerCase();
					String handleName = name + "." + methodName;
					HANDLE.put(handleName, this);
				}
			}
		}
	}

	public static void dispatchCmd(String cmd) {
		StringTokenizer cmdToken = new StringTokenizer(cmd);
		String code = cmdToken.nextToken(" ");
		if (code != null && HANDLE.containsKey(code)) {
			List<String> args = new LinkedList<String>();
			while (cmdToken.hasMoreTokens()) {
				args.add(cmdToken.nextToken(" "));
			}
			cmdToken = null;

			int idx = code.lastIndexOf(".");
			if (idx != -1) {
				String action = new String(code.substring(idx + 1, code.length()));
				AbstractService service = HANDLE.get(code);
				try {
					service.dispatch(action, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected abstract void dispatch(String cmd, List<String> args);

	public void initCache() {

	}

	protected ScheduledFuture<?> delay(Task task, long delay, TimeUnit unit) {
		return gameWindows.schedule(task, delay, unit);
	}

}