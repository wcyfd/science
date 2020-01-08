package com.science.game.entity.config;

import lombok.Data;

@Data
public class JobConfig {
	private int id;
	private String job;
	// 单位生产时长
	private long unitTotal;
	// 单位产量
	private int unitVelocity;
}
