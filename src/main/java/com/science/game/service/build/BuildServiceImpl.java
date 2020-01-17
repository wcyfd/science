package com.science.game.service.build;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.Item;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.build.InstallItem;
import com.science.game.entity.build.ModuleData;
import com.science.game.entity.config.ModuleConfig;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.build.module.ApplyBuildModule;
import com.science.game.service.item.ItemInternal;
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

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private BuildInternal buildInternal;

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "apply":
			apply(getInt(args, 0));
			break;
		case "build":
			build(getInt(args, 0), getInt(args, 1));
			break;
		case "join":
			join(getInt(args, 0), getInt(args, 1));
			break;
		}

	}

	@Override
	public void apply(int buildId) {
		applyBuildModule.applyBuild(buildId);
	}

	@Override
	public void build(int vid, int moduleId) {

		Village v = villageInternal.getVillage(vid);

		v.getBuildData().setModuleId(moduleId);
		int buildOnlyId = v.getBuildData().getBuildOnlyId();
		InstallItem installItem = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId)
				.getModuleData().getModuleId(moduleId);
		if (workInternal.isWorkComplete(installItem)) {
			log.info("该模块已经完成了vid={}, moduleId={}", vid, moduleId);
			return;
		}

		v.getBuildData().setModuleId(moduleId);
	}

	@Override
	public void join(int vid, int buildOnlyId) {

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

		if (!build.getModuleData().isFinish()) {// 建筑已经完成
			if (moduleId != 0) {
				building(v, moduleId, build);
			}
		}
	}

	/**
	 * 建设中
	 * 
	 * @param v
	 * @param moduleId
	 * @param build
	 */
	private void building(Village v, int moduleId, Build build) {
		// 工作流程
		InstallItem installItem = build.getModuleData().getModuleId(moduleId);
		// 填充道具
		this.fillItem(installItem);

		if (!workInternal.isWorkComplete(installItem)) {
			// 添加工作量
			workInternal.addWorkProgress(installItem, 5);
			// 工作是否完成
			if (workInternal.isWorkComplete(installItem)) {
				v.getBuildData().setModuleId(0);
				// 建筑是否完成
				buildInternal.checkComplete(build);
				if (build.getModuleData().isFinish()) {
					successFunc(build);
				}
			}
		}
	}

	/**
	 * 填充道具,如果没有填充过的话
	 * 
	 * @param item
	 */
	private void fillItem(InstallItem item) {
		// 填充道具,如果没有填充过的话
		if (item.getItem() != null)
			return;

		ModuleConfig cf = item.getProto();
		List<Item> list = itemInternal.extractItem(cf.getNeedItemId(), 1);
		if (list.size() != 0) {
			item.setItem(list.get(0));
		} else {
			log.info("道具抽取不足 buildId={},itemId={},moduleId={}", item.getBuild().getId(), cf.getNeedItemId(),
					cf.getModuleId());
		}

	}

	@Override
	public void checkComplete(Build build) {
		ModuleData moduleData = build.getModuleData();
		Map<Integer, InstallItem> installItems = moduleData.getInstallItems();
		for (InstallItem item : installItems.values()) {
			if (!workInternal.isWorkComplete(item)) {
				return;
			}
		}

		build.getModuleData().setFinish(true);
	}

	/**
	 * 成功
	 * 
	 * @param build
	 */
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
