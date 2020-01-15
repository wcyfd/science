package com.science.game.service.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Build;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;
import com.science.game.entity.scene.BuildData;
import com.science.game.service.AbstractService;
import com.science.game.service.place.PlaceInternal;
import com.science.game.service.village.VillageInternal;
import com.science.game.service.work.WorkInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BuildServiceImpl extends AbstractService implements BuildService, BuildInternal {

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
	public void build(int vid, int moduleId) {

		placeInternal.createIfAbsent(PlaceType.BUILD, 0);

		villageInternal.getVillage(vid);
	}

	@Override
	public void startBuild(int buildId) {
		Build build = Build.create(buildId);

		BuildData buildData = dataCenter.getScene().getBuildData();
		buildData.getOnlyIdBuildMap().put(build.getId(), build);
		Map<Integer, Map<Integer, Build>> typeMap = buildData.getTypeBuildMap();
		if (!typeMap.containsKey(buildId)) {
			typeMap.putIfAbsent(buildId, new HashMap<>(16));
		}
		Map<Integer, Build> buildMap = typeMap.get(buildId);
		buildMap.put(build.getId(), build);
	}

	@Override
	public void joinTeam(int vid, int buildOnlyId) {
		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId);
		if (build == null) {
			log.error("没有该建造物buildOnlyId={}", buildOnlyId);
			return;
		}
		Village v = villageInternal.getVillage(vid);
		v.getBuildData().setBuildOnlyId(buildOnlyId);

		build.getTeamData().getMembers().add(vid);
		placeInternal.enter(v, PlaceType.BUILD, build.getId());
	}

	@Override
	public void exitTeam(int vid) {
		Village v = villageInternal.getVillage(vid);
		int buildOnlyId = v.getBuildData().getBuildOnlyId();
		Build build = dataCenter.getScene().getBuildData().getOnlyIdBuildMap().get(buildOnlyId);
		if (build == null) {
			log.error("没有该建造物buildOnlyId={}", buildOnlyId);
			return;
		}
		build.getTeamData().getMembers().remove(vid);
		placeInternal.exit(v);
	}

}
