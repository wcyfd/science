package com.science.game.service.build.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ModuleConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.PlaceType;
import com.science.game.entity.build.InstallItem;
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
		int buildId = build.getId();
		buildData.getOnlyIdBuildMap().put(build.getId(), build);// 加入唯一id表
		Map<Integer, Map<Integer, Build>> typeMap = buildData.getTypeBuildMap();// 加入类型表
		if (!typeMap.containsKey(buildId)) {
			typeMap.putIfAbsent(buildId, new HashMap<>(16));
		}
		Map<Integer, Build> buildMap = typeMap.get(buildId);
		buildMap.put(build.getId(), build);
	}

	/**
	 * 初始化建筑数据
	 * 
	 * @param build
	 */
	private void initBuild(Build build) {
		List<ModuleConfig> moduleConfigList = moduleConfigCache.buildModuleMap.get(build.getProto().getBuildId());
		for (ModuleConfig config : moduleConfigList) {
			InstallItem installItem = InstallItem.create(build.getProto().getBuildId(), config.getModuleId());
			installItem.setBuild(build);
			installItem.getCurrent().set(0);
			installItem.setTotal(config.getTotal());

			build.getModuleData().getInstallItems().put(config.getModuleId(), installItem);
		}
	}
}
