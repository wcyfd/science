package com.science.game.service.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.scene.BuildData;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;
import com.science.game.service.work.WorkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BuildServiceImpl extends AbstractService implements BuildService, BuildInternal, IWork {

	@Autowired
	private VillageInternal villageInternal;

	@Autowired
	private WorkInternal workInternal;

	@Autowired
	private PlaceInternal placeInternal;

	@Autowired
	private DataCenter dataCenter;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void applyBuild(int buildId) {
		Build build = Build.create(buildId);

		BuildData buildData = dataCenter.getScene().getBuildData();
		buildData.getOnlyIdBuildMap().put(build.getId(), build);// 加入唯一id表
		Map<Integer, Map<Integer, Build>> typeMap = buildData.getTypeBuildMap();// 加入类型表
		if (!typeMap.containsKey(buildId)) {
			typeMap.putIfAbsent(buildId, new HashMap<>(16));
		}
		Map<Integer, Build> buildMap = typeMap.get(buildId);
		buildMap.put(build.getId(), build);

		placeInternal.createIfAbsent(PlaceType.BUILD, 0);// 申请一个建造位置
	}

	@Override
	public void build(int vid, int moduleId) {

		Village v = villageInternal.getVillage(vid);
		v.getBuildData().setModuleId(moduleId);
	}

	@Override
	public void joinTeam(int vid, int buildOnlyId) {

		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId);
		if (build == null) {
			log.error("没有该建造物buildOnlyId={}", buildOnlyId);
			return;
		}

		Village v = villageInternal.getVillage(vid);

		v.getBuildData().setBuildOnlyId(buildOnlyId);// 注册建筑
		build.getTeamData().getMembers().add(vid);// 加入团队

		placeInternal.enter(v, PlaceType.BUILD, build.getId());// 进入位置

		// TODO 加工作类型
		workInternal.beginWork(v.getWorkData(), JobType.NULL, this);
	}

	@Override
	public void enterWork(WorkData workData) {
		int vid = workData.getVid();
		Village v = villageInternal.getVillage(vid);
		v.getBuildData().setModuleId(0);
	}

	@Override
	public void workLoop(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		int moduleId = v.getBuildData().getModuleId();
		int buildOnlyId = v.getBuildData().getBuildOnlyId();

		if (moduleId == 0) {
			// 工作等待
			return;
		}
		// TODO 添加工作流程

	}

	@Override
	public void exitWork(WorkData workData) {
		int vid = workData.getVid();
		Village v = villageInternal.getVillage(vid);
		int buildOnlyId = v.getBuildData().getBuildOnlyId();
		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId);
		if (build == null) {
			log.error("没有该建造物buildOnlyId={},退出团队失败", buildOnlyId);
			return;
		}
		build.getTeamData().getMembers().remove(vid);
		placeInternal.exit(v);
	}

}
