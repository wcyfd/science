package com.science.game.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.science.game.entity.Item;
import com.science.game.entity.Village;

public class Data {

	public static Map<Integer, Village> villages = new HashMap<>();
	public static Map<Integer, Item> itemMap = new HashMap<>();

	public static ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(10);
	public static Map<Integer, ScheduledFuture<?>> villageFutures = new HashMap<>();
	public static int resource;
}
