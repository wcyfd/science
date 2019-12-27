package com.science.game.entity.config;

import lombok.Data;

@Data
public class ItemConfig {
	public enum ItemType {
		RES, ITEM
	}

	private int itemId;
	private String name;
	private ItemType type;
}
