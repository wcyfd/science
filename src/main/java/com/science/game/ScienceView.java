package com.science.game;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ConfigCache;
import com.science.game.cache.config.ItemConfigCache;
import com.science.game.cache.config.JobConfigCache;
import com.science.game.cache.config.PlaceConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.ProgressData;
import com.science.game.entity.Scene;
import com.science.game.entity.Village;
import com.science.game.entity.build.InstallItem;
import com.science.game.entity.build.ModuleData;
import com.science.game.entity.config.ItemConfig;
import com.science.game.entity.config.ItemConfig.ItemType;
import com.science.game.entity.scene.BuildData;
import com.science.game.entity.scene.ClimateData;
import com.science.game.entity.scene.PlaceData;
import com.science.game.service.AbstractService;
import com.science.game.service.ServiceInterface;
import com.science.game.service.lab.LabInternal;

import game.quick.window.IView;

@Component
public class ScienceView implements IView, ApplicationContextAware {

	private StringBuilder sb = new StringBuilder();
	private StringBuilder cmd = new StringBuilder();

	@Autowired
	private PlaceConfigCache placeConfigCache;

	@Autowired
	private ItemConfigCache itemConfigCache;

	@Autowired
	private JobConfigCache jobConfigCache;

	@Autowired
	private DataCenter dataCenter;

	@Override
	public String render() {
		sb.delete(0, sb.length());
		sb.append(cmd).append("\n");
		sb.append("===========\n");
		sb.append(ScienceHandler.getCmd()).append("\n");
		sb.append("===========\n");
		Scene scene = dataCenter.getScene();
		village(scene);
		item(scene);
		area(scene);
		think(scene);
		build(scene);
		place(scene);
		climate(scene);

		return sb.toString();
	}

	private void br() {
		sb.append("\n");
	}

	private void t() {
		sb.append("\t");
	}

	private void village(Scene scene) {
		sb.append("村民");
		br();

		scene.getVillageData().getVillages().values().forEach(v -> village_detail(v));
		br();
	}

	private void village_detail(Village v) {
		sb.append(v.getId()).append(" ");
		sb.append("工作 [")
				.append(v.getWorkData().getJobType() == null
						|| !jobConfigCache.jobMap.containsKey(v.getWorkData().getJobType().getJobId()) ? "空闲"
								: jobConfigCache.jobMap.get(v.getWorkData().getJobType().getJobId()).getJob())
				.append("(").append(v.getWorkData().getCurrent()).append("/").append(v.getWorkData().getTotal())
				.append(")").append(" callback=").append(v.getWorkData().getWork() != null).append("]");
		t();
		sb.append("装备[");
		for (Item item : v.getItemData().getEquips().values()) {
			sb.append(item.getId()).append(item.getProto().getName()).append(" ").append("寿命=").append(item.getAge())
					.append(",").append("数量=").append(item.getNum());
			t();
		}
		sb.append("]");
		t();
		sb.append("生产 [")
				.append(itemConfigCache.itemMap.containsKey(v.getProductData().getItemId())
						? itemConfigCache.itemMap.get(v.getProductData().getItemId()).getName()
						: null)
				.append("]");
		t();
		sb.append("研发 [ ").append(v.getDevelopData().getItemId());
		sb.append(" 熟练度:");
		for (Map.Entry<Integer, AtomicInteger> entrySet : v.getDevelopData().getPracticeMap().entrySet()) {
			sb.append(entrySet.getKey()).append(":").append(entrySet.getValue()).append(" ");
		}
		sb.append("]");
		t();
		sb.append("所在地 [").append(v.getPlaceData().getPlaceType()).append(" ")
				.append(v.getPlaceData().getPlace() != null ? v.getPlaceData().getPlace().getPlaceId() : null)
				.append("]");
		t();
		sb.append("建造 [").append("buildOnlyId=").append(v.getBuildData().getBuildOnlyId()).append(",模块id=")
				.append(v.getBuildData().getModuleId()).append("]");
		br();
	}

	private void item(Scene scene) {
		sb.append("道具");
		br();
		for (Map.Entry<Integer, List<Item>> entrySet : scene.getItemData().getAllItemsByItemId().entrySet()) {
			List<Item> items = entrySet.getValue();
			ItemConfig config = itemConfigCache.itemMap.get(entrySet.getKey());
			sb.append(entrySet.getKey()).append(" ").append(config.getName()).append(" ")
					.append(config.getType() == ItemType.ITEM ? items.size() : items.get(0).getNum()).append(" [");
			int maxLength = 20;
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
			sb.append("]");
			t();
		}
		br();
	}

	private void area(Scene scene) {
		sb.append("地区");
		t();
		int size = scene.getPlaceData().getAreaId();
		for (int i = 0; i < size; i++) {
			sb.append(i).append(" ")
					.append(placeConfigCache.placeMap.get(scene.getPlaceData().getAreaList().get(i)).getName())
					.append(" ");
		}
		sb.append("\n");
	}

	private void think(Scene scene) {
		sb.append("实验室");
		t();
		for (Integer id : scene.getLabData().getIdeaList()) {
			sb.append(id).append(" ").append(itemConfigCache.itemMap.get(id).getName()).append(" ");
		}
		br();
	}

	private void build(Scene scene) {
		sb.append("建筑");
		br();
		BuildData buildData = scene.getBuildData();
		Map<Integer, Map<Integer, Build>> typeBuildMap = buildData.getTypeBuildMap();
		for (Map.Entry<Integer, Map<Integer, Build>> typeBuildEntry : typeBuildMap.entrySet()) {
			int type = typeBuildEntry.getKey();
			sb.append("建筑类型=").append(ConfigCache.build.buildMap.get(type).getName());
			br();
			for (Map.Entry<Integer, Build> buildIdEntry : typeBuildEntry.getValue().entrySet()) {
				int onlyId = buildIdEntry.getKey();
				Build build = buildIdEntry.getValue();
				sb.append("id:").append(onlyId).append("[参与人员:");
				sb.append(build.getTeamData().getMembers()).append("]");
				t();
				sb.append("建造状态=").append(build.getModuleData().isFinish() ? "完成" : "建设中");
				br();
				ModuleData moduleData = build.getModuleData();

				Map<Integer, ProgressData> progressDataMap = moduleData.getProgressMap();
				for (Map.Entry<Integer, ProgressData> entrySet : progressDataMap.entrySet()) {
					int moduleId = entrySet.getKey();
					ProgressData data = entrySet.getValue();
					sb.append("模块编号=").append(moduleId).append(" (").append(data.getCurrent().get()).append("/")
							.append(data.getTotal()).append(")");
					t();
				}
				br();

				for (InstallItem item : moduleData.getInstallItems().values()) {
					sb.append(item.getProto().getOnlyId()).append(" ")
							.append(item.getItem() == null ? null : item.getItem().getProto().getName());
					t();
				}

				br();

			}
		}
		br();
	}

	private void place(Scene scene) {
		sb.append("空间");
		br();
		PlaceData placeData = scene.getPlaceData();

		for (PlaceType placeType : PlaceType.values()) {
			Map<Integer, Place> placeMap = placeData.getPlaceMapByType(placeType);
			sb.append(placeType);
			t();
			for (Place place : placeMap.values()) {
				sb.append(place.getPlaceId()).append(":").append(place.getVillageIds());
				t();
			}
			br();
		}
	}

	private void climate(Scene scene) {
		sb.append("气候");
		t();
		ClimateData climateData = scene.getClimateData();
		for (Map.Entry<Integer, AtomicInteger> entrySet : climateData.getParams().entrySet()) {
			int id = entrySet.getKey();
			int val = entrySet.getValue().get();
			String name = ConfigCache.climate.map.get(id).getName();
			sb.append(name).append("=").append(val);
			t();
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
