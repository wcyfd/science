package com.science.game;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.cache.config.ConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.entity.Item;
import com.science.game.entity.Village;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;
import com.science.game.entity.config.JobConfig;
import com.science.game.service.AbstractService;
import com.science.game.service.ServiceInterface;

import game.quick.window.IView;

@Component
public class ScienceView implements IView, ApplicationContextAware {

	private StringBuilder sb = new StringBuilder();
	private StringBuilder cmd = new StringBuilder();

	@Autowired
	private PlaceConfigCache placeConfigCache;

	@Autowired
	private ItemConfigCache itemConfigCache;

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
		think();
		sb.append("\n");

		return sb.toString();
	}

	private void village() {
		sb.append("Village").append("\n");
		Map<Integer, JobConfig> jobMap = ConfigCache.job.jobMap;
		for (Village v : Data.villages.values()) {
			sb.append(v.getId()).append(" ");
			JobConfig jobConfig = jobMap.get(v.getJobData().getJobType().getJobId());
			sb.append(jobConfig == null ? null : jobConfig.getJob());
			sb.append(" (").append(v.getJobData().getCurrent()).append("/").append(v.getJobData().getTotal())
					.append(")");
			sb.append("[");
			for (Item item : v.getEquips().values()) {
				if (item != null) {
					sb.append(item.getId()).append(" ");
					sb.append(item.getProto().getName()).append(",");
				}
			}
			sb.append("]");
			sb.append("\t");
		}
	}

	private void item() {
		sb.append("Item").append("\n");
		for (Map.Entry<Integer, List<Item>> entrySet : Data.itemMap.entrySet()) {
			List<Item> items = entrySet.getValue();
			ItemConfig config = itemConfigCache.itemMap.get(entrySet.getKey());
			sb.append(entrySet.getKey()).append(" ").append(config.getName()).append(" ")
					.append(config.getType() == ItemType.ITEM ? items.size() : items.get(0).getNum()).append(" [");
			int maxLength = 10;
			int idx = 0;
			for (Item item : items) {
				if (idx < maxLength) {
					sb.append(item.getId());
				} else {
					sb.append("...");
					break;
				}
				sb.append(",");
				idx++;
			}
			sb.append("]\n");
		}
	}

	private void area() {
		sb.append("area\n");
		for (int i = 0; i < Data.areaId; i++) {
			sb.append(i).append(" ").append(placeConfigCache.placeMap.get(Data.areaList.get(i)).getName()).append(" ");
		}
		sb.append("\n");
	}

	private void think() {
		sb.append("think\n");
		for (Integer id : Data.thinkList) {
			sb.append(id).append(" ").append(itemConfigCache.itemMap.get(id).getName()).append(" ");
		}
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
