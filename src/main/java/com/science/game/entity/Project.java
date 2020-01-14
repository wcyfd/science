package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.entity.project.BuildData;

import lombok.Getter;

public class Project {
	private static AtomicInteger ID = new AtomicInteger();
	private int id;
	@Getter
	private BuildData buildData;

	public Project() {
		this.id = ID.incrementAndGet();
		this.buildData = new BuildData();
	}

}
