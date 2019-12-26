package com.science.game;

import java.lang.reflect.Method;
import java.util.List;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Village;
import com.science.game.service.Service;

import game.quick.window.IView;

public class ScienceView implements IView {

	private StringBuilder sb = new StringBuilder();

	@Override
	public String render() {
		sb.delete(0, sb.length());
//		command();
//		sb.append("\n");
		village();
		sb.append("\n");
		item();
		sb.append("\n");
		sb.append("resource:").append(Data.resource);

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

	private void village() {
		sb.append("Village").append("\n");
		for (Village village : Data.villages.values()) {
			sb.append(village.toString()).append("\t");
		}
	}

	private void item() {
		sb.append("Item").append("\n");
		for (Item item : Data.itemMap.values()) {
			sb.append(item.getItemId()).append(item.getName()).append(item.getNum()).append("\n");
		}
	}
}
