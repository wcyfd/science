package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.entity.config.ResConfig;

public class Res extends DutyPosition {

	private static AtomicInteger ID = new AtomicInteger();
	private int id;
	private ResConfig proto;

	public static Res create(ResConfig config) {
		Res res = new Res();
		res.id = ID.incrementAndGet();
		res.proto = config;
		return res;
	}

	public ResConfig getProto() {
		return proto;
	}

	public int getId() {
		return id;
	}

}
