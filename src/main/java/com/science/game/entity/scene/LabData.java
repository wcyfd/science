package com.science.game.entity.scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class LabData {

	@Getter
	private Set<Integer> sciences = new HashSet<>();
	// 研发点
	private Map<Integer, AtomicInteger> developPoint = new ConcurrentHashMap<>();

	private Map<Integer, List<Integer>> developVillages = new ConcurrentHashMap<Integer, List<Integer>>();

	private List<Integer> thinkList = new ArrayList<>();

	/**
	 * 参与开发的所有村民
	 * 
	 * @return
	 */
	public Map<Integer, List<Integer>> getDevelopVillages() {
		return developVillages;
	}

	public Map<Integer, AtomicInteger> getDevelopPoint() {
		return developPoint;
	}

	/**
	 * 等待开发的列表
	 * 
	 * @return
	 */
	public List<Integer> getThinkList() {
		return thinkList;
	}

}
