package com.science.game.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.science.game.DefaultI;
import com.science.game.I;

import game.quick.window.GameWindows;
import game.quick.window.Task;

public abstract class AbstractService {
	private final static Map<String, AbstractService> HANDLE = new HashMap<>();
	@Autowired
	private GameWindows gameWindows;

	// 入参获取值的接口
	private static final DefaultI REQUEST_PARAMS_INTERFACE = new DefaultI();

	public static void dispatchCmd(String cmd) {
		StringTokenizer cmdToken = new StringTokenizer(cmd);
		String code = cmdToken.nextToken(" ");
		if (code != null && HANDLE.containsKey(code)) {
			List<String> args = REQUEST_PARAMS_INTERFACE.getParams();
			injectParams(cmdToken, args);
			cmdToken = null;

			int idx = code.lastIndexOf(".");
			if (idx != -1) {
				String action = new String(code.substring(idx + 1, code.length()));
				AbstractService service = HANDLE.get(code);
				try {
					service.dispatch(action, REQUEST_PARAMS_INTERFACE);
					REQUEST_PARAMS_INTERFACE.cleanParams();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void injectParams(StringTokenizer cmdToken, List<String> args) {
		while (cmdToken.hasMoreTokens()) {
			args.add(cmdToken.nextToken(" "));
		}
	}

	protected abstract void dispatch(String cmd, I params);

	public void initService() {
		registHandle();
		initModule();
		initCache();
	}

	private void registHandle() {
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

	protected void initModule() {

	}

	protected void initCache() {

	}

	public ScheduledFuture<?> delay(Task task, long millisecond) {
		return gameWindows.schedule(task, millisecond, TimeUnit.SECONDS);
	}

	public ScheduledFuture<?> delay(Task task) {
		return gameWindows.schedule(task, 1, TimeUnit.SECONDS);
	}

	protected int getInt(List<String> list, int idx) {
		return Integer.valueOf(list.get(idx));
	}

}
