package com.science.game.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import game.quick.window.GameWindows;
import game.quick.window.Task;

public abstract class Service {
	private final static Map<String, Service> HANDLE = new HashMap<>();
	private final static Map<Class<?>, Service> CLASS_MODULE_MAP = new HashMap<>();
	private static GameWindows gameWindows;

	public static void regist(Class<? extends Service> classImpl, Class<?> classInterface)
			throws InstantiationException, IllegalAccessException {
		Service service = classImpl.newInstance();
		CLASS_MODULE_MAP.put(classInterface, service);

		Method[] methods = classInterface.getMethods();

		String name = classInterface.getSimpleName().toLowerCase().replace("service", "");

		for (Method method : methods) {
			String methodName = method.getName().toLowerCase();
			String handleName = name + "." + methodName;
			HANDLE.put(handleName, service);
		}
	}

	public static void dispatchCmd(String cmd, List<String> args) {
		Service service = HANDLE.get(cmd);
		if (service != null) {

			StringTokenizer token = new StringTokenizer(cmd);
			token.nextToken(".");
			String action = token.nextToken(".");
			service.dispatch(action, args);
		}
	}

	protected abstract void dispatch(String cmd, List<String> args);

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		return (T) CLASS_MODULE_MAP.get(clazz);
	}

	public static List<Class<?>> getServices() {
		return new ArrayList<>(CLASS_MODULE_MAP.keySet());
	}

	public static void initServiceCache() {
		for (Service service : CLASS_MODULE_MAP.values()) {
			service.initCache();
		}
	}

	public static void initService(GameWindows gameWindows) {
		Service.gameWindows = gameWindows;
	}

	protected void initCache() {

	}

	protected ScheduledFuture<?> delay(Task task, long delay, TimeUnit unit) {
		return gameWindows.schedule(task, delay, unit);
	}

}
