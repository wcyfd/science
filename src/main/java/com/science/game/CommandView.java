package com.science.game;

import java.lang.reflect.Method;
import java.util.List;

import com.science.game.service.Service;

import game.quick.window.IView;

public class CommandView implements IView {

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
}
