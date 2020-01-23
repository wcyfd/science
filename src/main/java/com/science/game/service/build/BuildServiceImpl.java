package com.science.game.service.build;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.config.ModuleConfigCache;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.Item;
import com.science.game.entity.JobType;
import com.science.game.entity.PlaceType;
import com.science.game.entity.ProgressData;
import com.science.game.entity.Village;
import com.science.game.entity.build.InstallItem;
import com.science.game.entity.build.ModuleData;
import com.science.game.entity.config.ModuleConfig;
import com.science.game.entity.village.WorkData;
import com.science.game.service.AbstractService;
import com.science.game.service.build.module.ApplyBuildModule;
import com.science.game.service.damage.DamageInternal;
import com.science.game.service.damage.IDamage;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.IWork;
import com.science.game.service.work.WorkInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BuildServiceImpl extends AbstractService implements BuildService, BuildInternal, IWork, IDamage<Build> {

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

	@Autowired
	private ModuleConfigCache moduleConfigCache;

	@Autowired
	private DamageInternal damageInternal;

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "apply":
			apply(i.i());
			break;
		case "build":
			build(i.i(), i.i(), i.i());
			break;
		}

	}

	@Override
	public void apply(int buildId) {
		applyBuildModule.applyBuild(buildId);
	}

	@Override
	public void build(int vid, int buildOnlyId, int moduleId) {

		Village v = villageInternal.getVillage(vid);

		ProgressData progressData = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId)
				.getModuleData().getProgressByModuleId(moduleId);
		if (workInternal.isWorkComplete(progressData)) {
			log.info("该模块已经完成了vid={}, moduleId={}", vid, moduleId);
			return;
		}

		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId);

		workInternal.exitWork(v.getWorkData());

		v.getBuildData().setBuildOnlyId(buildOnlyId);// 注册建筑
		v.getBuildData().setModuleId(moduleId);

		build.getTeamData().getMembers().add(vid);// 加入团队

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
		ProgressData modulePD = build.getModuleData().getProgressByModuleId(moduleId);

		WorkData workData = v.getWorkData();

		if (!workInternal.isWorkComplete(modulePD)) {
			// 检查道具是否完备,完备才可以开始作业
			if (this.tryFillItem(build, moduleId)) {
				// 添加工作量
				workInternal.addWorkProgress(modulePD, 1);
				workInternal.addWorkProgress(workData, 1);
				// 思考
				villageInternal.think(v.getId());

				// 工作是否完成
				if (workInternal.isWorkComplete(modulePD)) {
					v.getBuildData().setModuleId(0);
					workInternal.resetProgress(workData);
					workInternal.exitWork(workData);
					// 建筑是否完成
					buildInternal.checkComplete(build);
					if (build.getModuleData().isFinish()) {
						successFunc(build);
					}

				}
			}
		}
	}

	/**
	 * 填充道具,如果没有填充过的话
	 * 
	 * @param build
	 * @param moduleId
	 * @return 材料备齐返回 true
	 */
	private boolean tryFillItem(Build build, int moduleId) {

		int buildId = build.getProto().getBuildId();
		boolean fill = true;

		Map<Integer, ModuleConfig> itemMap = moduleConfigCache.moduleItemMap.get(buildId).get(moduleId);
		for (Map.Entry<Integer, ModuleConfig> entrySet : itemMap.entrySet()) {
			ModuleConfig config = entrySet.getValue();
			int onlyId = config.getOnlyId();

			// 获得安装位置
			InstallItem installItem = build.getModuleData().getInstallItemById(onlyId);
			if (installItem.getItem() == null) {
				// 扣除道具
				config.getNeedItemId();
				List<Item> list = itemInternal.extractItem(config.getNeedItemId(), 1);
				if (list.size() != 0) {// 如果抽取道具成功
					installItem.setItem(list.get(0));
				} else {
					log.info("道具抽取不足 buildId={},itemId={},moduleId={}", installItem.getBuild().getId(),
							config.getNeedItemId(), config.getModuleId());
					fill = false;
				}
			}
		}

		return fill;

	}

	@Override
	public void checkComplete(Build build) {
		ModuleData moduleData = build.getModuleData();
		for (ProgressData data : moduleData.getProgressMap().values()) {
			if (!workInternal.isWorkComplete(data)) {
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

		damageInternal.regist(build, this);

	}

	@Override
	public void damageLoop(Build build) {
		
	}

	@Override
	public void enterWork(WorkData workData) {
		int vid = workData.getVid();
		Village v = villageInternal.getVillage(vid);
		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(v.getBuildData().getBuildOnlyId());
		ProgressData progressData = build.getModuleData().getProgressByModuleId(v.getBuildData().getModuleId());

		workData.getCurrent().set(progressData.getCurrent().get());
		workData.setTotal(progressData.getTotal());

		placeInternal.enter(v, PlaceType.BUILD, build.getId());// 进入位置
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

		v.getBuildData().setBuildOnlyId(0);// 注册建筑
		v.getBuildData().setModuleId(0);
		build.getTeamData().getMembers().remove(vid);

		placeInternal.exit(v);
	}

	@Override
	public void repair(int vid, int buildOnlyId, int moduleId) {
		// TODO Auto-generated method stub

	}

}
