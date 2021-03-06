package com.science.game.entity.scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

public class LabData {

	@Getter
	private Set<Integer> sciences = new HashSet<>();
	// 研发点
	private Map<Integer, AtomicInteger> developPoint = new ConcurrentHashMap<>();
	// 尝试次数
	private Map<Integer, AtomicInteger> tryCountMap = new ConcurrentHashMap<>();
	// 开发团队
	private Map<Integer, Set<Integer>> teamMap = new ConcurrentHashMap<>();

	private List<Integer> ideaList = new ArrayList<>();

	public Map<Integer, AtomicInteger> getDevelopPoint() {
		return developPoint;
	}

	/**
	 * 等待开发的列表
	 * 
	 * @return
	 */
	public List<Integer> getIdeaList() {
		return ideaList;
	}

	public Map<Integer, AtomicInteger> getTryCountMap() {
		return tryCountMap;
	}

	/**
	 * 所有的开发团队
	 * 
	 * @return Map<道具id,Set<村民id>>
	 */
	public Map<Integer, Set<Integer>> getTeamMap() {
		return teamMap;
	}

}
