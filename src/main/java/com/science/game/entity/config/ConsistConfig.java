package com.science.game.entity.config;

import lombok.Data;

@Data
public class ConsistConfig {
	private int itemId;// 道具id
	private int needItemId;// 需要的道具
	private int count;// 需要的数量
}
