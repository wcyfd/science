package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.build.ModuleData;
import com.science.game.entity.build.TeamData;
import com.science.game.entity.config.BuildConfig;
import com.science.game.entity.scene.ClimateData;

import lombok.Getter;

/**
 * 建筑物
 * 
 * @author heyue
 *
 */
public class Build {
	private static AtomicInteger ID = new AtomicInteger();
	@Getter
	private int id;

	@Getter
	private BuildConfig proto;
	@Getter
	private ModuleData moduleData;
	@Getter
	private TeamData teamData;

	public static Build create(int buildId) {
		Build build = new Build();
		build.id = ID.incrementAndGet();
		build.proto = ConfigCache.build.buildMap.get(buildId);

		return build;
	}

	private Build() {
		this.moduleData = new ModuleData(id);
		this.teamData = new TeamData(id);
	}
}
