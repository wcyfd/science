package com.science.game.service.build;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.config.ModuleConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.build.ModuleWorkData;
import com.science.game.entity.config.ModuleConfig;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.build.module.ApplyBuildModule;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;

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

	@Autowired
	private ApplyBuildModule applyBuildModule;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void applyBuild(int buildId) {
		applyBuildModule.applyBuild(buildId);
	}

	@Override
	public void build(int vid, int moduleId) {

		Village v = villageInternal.getVillage(vid);

		v.getBuildData().setModuleId(moduleId);
		int buildOnlyId = v.getBuildData().getBuildOnlyId();
		WorkData workData = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId).getModuleData()
				.getWorkDataByModuleId(moduleId);
		if (workInternal.isWorkComplete(workData)) {
			log.info("该模块已经完成了vid={}, moduleId={}", vid, moduleId);
			return;
		}

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

		workInternal.beginWork(v.getWorkData(), JobType.BUILD, this);
	}

	@Override
	public void workLoop(WorkData workData) {
		Village v = villageInternal.getVillage(workData.getVid());
		int moduleId = v.getBuildData().getModuleId();
		int buildOnlyId = v.getBuildData().getBuildOnlyId();
		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId);

		if (isBuildComplete(build)) {// 建筑已经完成
			successFunc(build);
		} else {
			if (moduleId == 0)
				// 工作等待
				return;

			// 工作流程
			ModuleWorkData moduleWorkData = build.getModuleData().getWorkDataByModuleId(moduleId);

			// TODO 消耗道具

			if (!workInternal.isWorkComplete(moduleWorkData)) {
				// 添加工作量
				workInternal.addWorkProgress(moduleWorkData, 5);
				// 工作是否完成
				if (workInternal.isWorkComplete(moduleWorkData)) {
					v.getBuildData().setModuleId(0);
					// 建筑是否完成
					if (isBuildComplete(build)) {
						successFunc(build);
					}
				}
			}
		}

	}

	private boolean isBuildComplete(Build build) {
		return false;
	}

	private void successFunc(Build build) {
		build.getTeamData().getMembers()
				.forEach(vid -> workInternal.exitWork(villageInternal.getVillage(vid).getWorkData()));

		new HashSet<>(placeInternal.getPlace(PlaceType.BUILD, build.getId()).getVillageIds()).stream()
				.forEach(vid -> placeInternal.exit(villageInternal.getVillage(vid)));
	}

	@Override
	public void enterWork(WorkData workData) {
		int vid = workData.getVid();
		Village v = villageInternal.getVillage(vid);
		v.getBuildData().setModuleId(0);
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
