package com.science.game.service.build.module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ModuleConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.PlaceType;
import com.science.game.entity.ProgressData;
import com.science.game.entity.build.InstallItem;
import com.science.game.entity.build.ModuleData;
import com.science.game.entity.config.ModuleConfig;
import com.science.game.entity.scene.BuildData;
import com.science.game.service.place.PlaceInternal;

@Component
public class ApplyBuildModule {

	@Autowired
	private PlaceInternal placeInternal;

	@Autowired
	private DataCenter dataCenter;

	@Autowired
	private ModuleConfigCache moduleConfigCache;

	public void applyBuild(int buildId) {

		Build build = Build.create(buildId);

		registBuild(build);

		initBuild(build);

		placeInternal.createIfAbsent(PlaceType.BUILD, build.getId());// 申请一个建造位置

	}

	/**
	 * 注册建筑
	 * 
	 * @param build
	 * @param buildData
	 */
	private void registBuild(Build build) {
		BuildData buildData = dataCenter.getScene().getBuildData();
		int buildType = build.getProto().getBuildId();
		buildData.getOnlyIdBuildMap().put(build.getId(), build);// 加入唯一id表
		Map<Integer, Map<Integer, Build>> typeMap = buildData.getTypeBuildMap();// 加入类型表
		if (!typeMap.containsKey(buildType)) {
			typeMap.putIfAbsent(buildType, new ConcurrentHashMap<>(16));
		}
		Map<Integer, Build> buildMap = typeMap.get(buildType);
		buildMap.put(build.getId(), build);
	}

	/**
	 * 初始化建筑数据
	 * 
	 * @param build
	 */
	private void initBuild(Build build) {
		Map<Integer, Map<Integer, ModuleConfig>> map = moduleConfigCache.moduleItemMap
				.get(build.getProto().getBuildId());
		ModuleData moduleData = build.getModuleData();

		for (Map.Entry<Integer, Map<Integer, ModuleConfig>> moduleEntrySet : map.entrySet()) {
			Map<Integer, ModuleConfig> installItemMap = moduleEntrySet.getValue();
			for (ModuleConfig config : installItemMap.values()) {
				InstallItem installItem = InstallItem.create(config.getOnlyId());
				installItem.setBuild(build);

				// 添加进度
				moduleData.getInstallItems().put(config.getOnlyId(), installItem);
				// 添加模块进度
				if (!moduleData.getProgressMap().containsKey(config.getModuleId())) {
					ProgressData progressData = new ProgressData();
					progressData.getCurrent().set(0);
					progressData.setTotal(config.getTotal());
					moduleData.getProgressMap().putIfAbsent(config.getModuleId(), progressData);
				}
			}
		}
//
//		for (Map.Entry<Integer, Integer> entrySet : map.entrySet()) {
//			int moduleId = entrySet.getKey();
//			int total = entrySet.getValue();
//
//			InstallItem installItem = InstallItem.create(build.getProto().getBuildId(), config.getModuleId());
//			installItem.setBuild(build);
//			installItem.getCurrent().set(0);
//			installItem.setTotal(total);
//
//			build.getModuleData().getInstallItems().put(config.getModuleId(), installItem);
//		}
	}
}
