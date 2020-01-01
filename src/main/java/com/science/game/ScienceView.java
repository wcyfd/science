package com.science.game;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Village;
import com.science.game.service.AbstractService;
import com.science.game.service.ServiceInterface;

import game.quick.window.IView;

@Component
public class ScienceView implements IView, ApplicationContextAware {

	private StringBuilder sb = new StringBuilder();
	private StringBuilder cmd = new StringBuilder();

	@Autowired
	private PlaceConfigCache placeConfigCache;

	@Override
	public String render() {
		sb.delete(0, sb.length());
		sb.append(cmd).append("\n");
		sb.append("===========\n");
		sb.append(Data.cmd).append("\n");
		sb.append("===========\n");
		village();
		sb.append("\n");
		item();
		sb.append("\n");
		area();
		sb.append("\n");

		return sb.toString();
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
			sb.append(item.getProto().getItemId()).append(" ").append(item.getProto().getName()).append(" ")
					.append(item.getNum()).append("\n");
		}
	}

	private void area() {
		sb.append("area\n");
		for (int i = 0; i < Data.areaId; i++) {
			sb.append(i).append(" ").append(placeConfigCache.placeMap.get(Data.areaList.get(i)).getName()).append(" ");
		}
		sb.append("\n");
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		cmd.append("Command").append("\n");
		Map<String, AbstractService> services = ctx.getBeansOfType(AbstractService.class);
		for (AbstractService service : services.values()) {
			Class<?>[] clazzes = service.getClass().getInterfaces();
			for (Class<?> c : clazzes) {
				if (ServiceInterface.class.isAssignableFrom(c)) {
					String name = c.getSimpleName().toLowerCase().replace("service", "");
					Method[] methods = c.getMethods();
					if (methods.length != 0) {
						cmd.append(name).append("\t");
						for (Method method : methods) {
							cmd.append(method.getName().toLowerCase()).append("  ");
						}
						cmd.append("\n");
					}

				}
			}
		}

	}
}
