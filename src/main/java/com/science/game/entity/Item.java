package com.science.game.entity;

import com.science.game.entity.config.ItemConfig;

import lombok.Data;

@Data
public class Item {
	private ItemConfig proto;
	private int num;
}
