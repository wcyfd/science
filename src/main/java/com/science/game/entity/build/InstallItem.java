package com.science.game.entity.build;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.Build;
import com.science.game.entity.Item;
import com.science.game.entity.config.ModuleConfig;

import lombok.Getter;

/**
 * 用来记录道具
 * 
 * @author aimfd
 *
 */
public class InstallItem {

	private Item item;

	private Build build;
	@Getter
	private ModuleConfig proto;

	public static InstallItem create(int onlyId) {
		InstallItem item = new InstallItem();
		item.proto = ConfigCache.module.moduleMap.get(onlyId);
		return item;
	}

	/**
	 * 获取道具
	 * 
	 * @return
	 */
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * 关联的建筑
	 * 
	 * @return
	 */
	public Build getBuild() {
		return build;
	}

	public void setBuild(Build build) {
		this.build = build;
	}

}
