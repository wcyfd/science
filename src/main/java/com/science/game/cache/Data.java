package com.science.game.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.entity.Village;

import game.quick.window.Task;

public class Data {
	public static String cmd = null;

	public static Map<Integer, ScheduledFuture<?>> villageFutures = new ConcurrentHashMap<>();
	public static Map<Integer, Village> villages = new ConcurrentHashMap<>();

	public static Map<Integer, List<Item>> itemMap = new ConcurrentHashMap<>();
	public static Map<Integer, Item> itemIdMap = new ConcurrentHashMap<Integer, Item>();

	/** 这个数据结构是用于记录当前研发成功的道具，因为失传后道具还是存在的，所以要单独记录 */
	public static Set<Integer> scienceMap = new HashSet<>();

	// 地点
	public static Map<Integer, Place> resPlace = new HashMap<>();
	public static Map<Integer, Place> itemPlace = new HashMap<>();

	public static List<Integer> areaList = new ArrayList<>();
	public static int areaId = -1;
	// 等待思考的列表
	public static List<Integer> thinkList = new ArrayList<>();

	// 研发点
	public static Map<Integer, Integer> developPoint = new ConcurrentHashMap<>();
	public static Map<Integer, List<Integer>> developVillages = new ConcurrentHashMap<Integer, List<Integer>>();
}
