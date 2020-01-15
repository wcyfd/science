package com.science.game.entity.build;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

public class TeamData {
	@Getter
	private int id;
	@Getter
	private Set<Integer> members = new HashSet<>();

	public TeamData(int id) {
		this.id = id;
	}
}
