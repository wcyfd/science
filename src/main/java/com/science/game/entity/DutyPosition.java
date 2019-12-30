package com.science.game.entity;

import java.util.ArrayList;
import java.util.List;

public class DutyPosition {
	private List<Integer> vid = new ArrayList<>();

	/**
	 * 获取工作中的村民的id
	 * 
	 * @return
	 */
	public List<Integer> getPosition() {
		return this.vid;
	}

}
