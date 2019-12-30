package com.science.game.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.science.game.entity.Item;
import com.science.game.entity.Res;
import com.science.game.entity.Village;

public class Data {
	public static String cmd = null;

	public static Map<Integer, Village> villages = new HashMap<>();
	public static Map<Integer, Item> itemMap = new HashMap<>();

	public static Map<Integer, ScheduledFuture<?>> villageFutures = new ConcurrentHashMap<>();
	public static List<Res> areaList = new ArrayList<>();
	public static int areaId = -1;

}
