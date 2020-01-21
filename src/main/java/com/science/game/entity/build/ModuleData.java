package com.science.game.entity.build;

import java.util.HashMap;
import java.util.Map;

import com.science.game.entity.ProgressData;

import lombok.Getter;
import lombok.Setter;

/**
 * 模块数据
 * 
 * @author heyue
 *
 */
public class ModuleData {
	@Getter
	private int id;
	@Getter
	@Setter
	private boolean finish;
	@Getter
	private Map<Integer, InstallItem> installItems = new HashMap<>();
	@Getter
	private Map<Integer, ProgressData> progressMap = new HashMap<>();

	public ModuleData(int id) {
		this.id = id;
	}

	public InstallItem getInstallItemById(int id) {
		return installItems.get(id);
	}

	public ProgressData getProgressByModuleId(int id) {
		return progressMap.get(id);
	}

}
