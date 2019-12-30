package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.config.ResConfig;

public class Res extends DutyPosition {

	private static AtomicInteger ID = new AtomicInteger();
	private int id;
	private ResConfig proto;

	public static Res create(int resId) {
		Res res = new Res();
		res.id = ID.incrementAndGet();
		res.proto = ConfigCache.res.resMap.get(resId);
		return res;
	}

	public ResConfig getProto() {
		return proto;
	}

	public int getId() {
		return id;
	}

}
