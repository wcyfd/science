package com.science.game.entity.build;

import java.util.HashMap;
import java.util.Map;

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

	public ModuleData(int id) {
		this.id = id;
	}

	public InstallItem getModuleId(int id) {
		return installItems.get(id);
	}

}
